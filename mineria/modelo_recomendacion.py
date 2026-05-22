"""
Sistema de Recomendación Híbrido - Mi Destino
Minería de Datos II - VIII Semestre

Tipo de problema: Filtrado colaborativo + basado en contenido (sistema de recomendación)
Variables predictoras: puntuacion, tipoSitio, precio, valoracionPromedio
Variable objetivo: planes relevantes para el usuario
"""

import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
from sklearn.preprocessing import LabelEncoder, MinMaxScaler
from sklearn.neural_network import MLPRegressor
from sklearn.model_selection import train_test_split
from sklearn.metrics import mean_absolute_error, mean_squared_error


# ── 1. CARGA Y PREPARACIÓN DE DATOS ──────────────────────────────────────────

def cargar_datos_demo():
    """Carga dataset real del proyecto Mi Destino desde archivos CSV."""
    import os
    base_dir = os.path.dirname(os.path.abspath(__file__))
    val_path  = os.path.join(base_dir, "dataset", "valoraciones.csv")
    plan_path = os.path.join(base_dir, "dataset", "planes.csv")

    if os.path.exists(val_path) and os.path.exists(plan_path):
        valoraciones = pd.read_csv(val_path)[['cliente_id', 'plan_id', 'puntuacion']]
        planes = pd.read_csv(plan_path).rename(columns={'valoracion_prom': 'valoracion_prom'})
        return valoraciones, planes

    # Fallback mínimo si los CSV no están disponibles
    valoraciones = pd.DataFrame({
        'cliente_id': [1,1,2,2,3,3,4,4,5,5],
        'plan_id':    [1,3,1,6,3,19,7,15,13,17],
        'puntuacion': [5,5,4,5,5,5,4,5,5,5]
    })
    planes = pd.DataFrame({
        'plan_id':         [1,3,6,7,13,15,17,19],
        'nombre':          ['Murallas Cartagena','Snorkel Rosario','Isla Barú',
                            'Piedra del Peñol','Street Food','Arte Urbano',
                            'Taller Ceviche','Buceo Ballenas'],
        'tipo_sitio':      ['Cultural','Aventura','Relax','Aventura',
                            'Gastronómico','Cultural','Gastronómico','Aventura'],
        'precio':          [320000,450000,520000,340000,220000,180000,280000,620000],
        'valoracion_prom': [4.7,4.8,4.9,4.7,4.5,4.6,4.7,4.9],
        'ciudad':          ['Cartagena','Cartagena','Cartagena','Medellín',
                            'Bogotá','Bogotá','Bogotá','Buenaventura']
    })
    return valoraciones, planes


def preprocesar_datos(valoraciones, planes):
    """Preprocesamiento y transformación de datos."""
    # Matriz usuario-item
    matriz = valoraciones.pivot_table(
        index='cliente_id', columns='plan_id', values='puntuacion'
    ).fillna(0)

    # Feature matrix para content-based
    le = LabelEncoder()
    scaler = MinMaxScaler()

    planes_feat = planes.copy()
    planes_feat['tipo_encoded'] = le.fit_transform(planes_feat['tipo_sitio'])
    planes_feat[['precio_norm', 'valoracion_norm']] = scaler.fit_transform(
        planes_feat[['precio', 'valoracion_prom']]
    )

    features = planes_feat[['tipo_encoded', 'precio_norm', 'valoracion_norm']].values
    return matriz, features, planes_feat


# ── 2. FILTRADO COLABORATIVO (Item-Based) ────────────────────────────────────

def filtrado_colaborativo(matriz, cliente_id, top_n=5):
    """
    Similitud coseno entre usuarios para recomendar planes.
    Retorna lista de (plan_id, score) ordenados por relevancia.
    """
    if cliente_id not in matriz.index:
        return []

    sim_usuarios = cosine_similarity(matriz)
    sim_df = pd.DataFrame(sim_usuarios, index=matriz.index, columns=matriz.index)

    similitudes = sim_df[cliente_id].drop(cliente_id).sort_values(ascending=False)
    planes_vistos = set(matriz.columns[matriz.loc[cliente_id] > 0])

    scores = {}
    pesos = {}
    for otro_uid, sim in similitudes.items():
        if sim <= 0:
            continue
        for plan_id in matriz.columns:
            if plan_id in planes_vistos:
                continue
            rating = matriz.loc[otro_uid, plan_id]
            if rating > 0:
                scores[plan_id] = scores.get(plan_id, 0) + sim * rating
                pesos[plan_id]  = pesos.get(plan_id, 0) + sim

    recomendaciones = [
        (pid, scores[pid] / pesos[pid])
        for pid in scores if pesos[pid] > 0
    ]
    return sorted(recomendaciones, key=lambda x: -x[1])[:top_n]


# ── 3. FILTRADO BASADO EN CONTENIDO ──────────────────────────────────────────

def filtrado_contenido(valoraciones, planes_feat, features, cliente_id, top_n=5):
    """Recomienda planes similares al historial del usuario por atributos."""
    hist = valoraciones[valoraciones['cliente_id'] == cliente_id]
    if hist.empty:
        return []

    planes_vistos = set(hist['plan_id'])
    hist_features = features[planes_feat['plan_id'].isin(planes_vistos)]
    if len(hist_features) == 0:
        return []

    # Perfil del usuario = promedio ponderado por rating
    ratings = hist.set_index('plan_id')['puntuacion']
    pesos = np.array([ratings.get(pid, 0) for pid in planes_feat['plan_id']
                      if pid in planes_vistos])
    if pesos.sum() == 0:
        return []

    perfil = np.average(hist_features, axis=0, weights=pesos)
    sim = cosine_similarity([perfil], features)[0]

    candidatos = [
        (int(planes_feat.iloc[i]['plan_id']), float(sim[i]))
        for i in range(len(planes_feat))
        if int(planes_feat.iloc[i]['plan_id']) not in planes_vistos
    ]
    return sorted(candidatos, key=lambda x: -x[1])[:top_n]


# ── 4. RED NEURONAL (MLPRegressor) ───────────────────────────────────────────

def red_neuronal(valoraciones, planes_feat, features, cliente_id, top_n=6):
    """
    Red neuronal MLP que predice el rating de un cliente para planes no vistos.
    Arquitectura: capas ocultas (64, 32, 16) con activación ReLU.
    Entradas: [usuario_encoded, tipo_encoded, precio_norm, valoracion_norm]
    Salida: puntuacion predicha (1-5)
    """
    if len(valoraciones) < 5:
        return []

    le_user = LabelEncoder()
    valoraciones = valoraciones.copy()
    valoraciones['user_enc'] = le_user.fit_transform(valoraciones['cliente_id'])

    X, y = [], []
    for _, row in valoraciones.iterrows():
        plan_row = planes_feat[planes_feat['plan_id'] == row['plan_id']]
        if plan_row.empty:
            continue
        idx = plan_row.index[0]
        feat = list(features[planes_feat.index.get_loc(idx)])
        X.append([row['user_enc']] + feat)
        y.append(row['puntuacion'])

    if len(X) < 5:
        return []

    X, y = np.array(X), np.array(y)
    scaler = MinMaxScaler()
    X_scaled = scaler.fit_transform(X)

    mlp = MLPRegressor(
        hidden_layer_sizes=(64, 32, 16),
        activation='relu',
        max_iter=500,
        random_state=42,
        early_stopping=True,
        validation_fraction=0.15,
        n_iter_no_change=10,
    )
    mlp.fit(X_scaled, y)

    planes_vistos = set(valoraciones[valoraciones['cliente_id'] == cliente_id]['plan_id'])
    if cliente_id in le_user.classes_:
        user_enc = le_user.transform([cliente_id])[0]
    else:
        user_enc = 0

    predicciones = []
    for i, row in planes_feat.iterrows():
        pid = int(row['plan_id'])
        if pid in planes_vistos:
            continue
        idx = planes_feat.index.get_loc(i)
        feat = list(features[idx])
        x = np.array([[user_enc] + feat])
        x_scaled = scaler.transform(x)
        score = float(mlp.predict(x_scaled)[0])
        predicciones.append((pid, score))

    return sorted(predicciones, key=lambda x: -x[1])[:top_n]


# ── 5. SISTEMA HÍBRIDO ────────────────────────────────────────────────────────

def recomendar_hibrido(cliente_id, valoraciones, planes, top_n=6):
    """Combina red neuronal (MLP) + filtrado colaborativo + basado en contenido."""
    matriz, features, planes_feat = preprocesar_datos(valoraciones, planes)

    nn  = red_neuronal(valoraciones, planes_feat, features, cliente_id, top_n)
    col = filtrado_colaborativo(matriz, cliente_id, top_n)
    con = filtrado_contenido(valoraciones, planes_feat, features, cliente_id, top_n)

    # Pesos: Red Neuronal 50%, Colaborativo 30%, Contenido 20%
    scores_finales = {}
    for pid, s in nn:
        scores_finales[pid] = scores_finales.get(pid, 0) + 0.5 * s
    for pid, s in col:
        scores_finales[pid] = scores_finales.get(pid, 0) + 0.3 * s
    for pid, s in con:
        scores_finales[pid] = scores_finales.get(pid, 0) + 0.2 * s

    top_ids = sorted(scores_finales, key=lambda x: -scores_finales[x])[:top_n]

    nn_ids  = {p for p, _ in nn}
    col_ids = {p for p, _ in col}

    resultado = []
    for pid in top_ids:
        info = planes[planes['plan_id'] == pid].iloc[0]
        if pid in nn_ids:
            fuente = 'red_neuronal'
        elif pid in col_ids:
            fuente = 'colaborativo'
        else:
            fuente = 'contenido'
        resultado.append({
            'plan_id':    int(pid),
            'nombre':     info['nombre'],
            'tipo_sitio': info['tipo_sitio'],
            'score':      round(scores_finales[pid], 4),
            'fuente':     fuente,
            'ciudad':     info.get('ciudad', ''),
        })

    return resultado


# ── 6. EVALUACIÓN DEL MODELO ──────────────────────────────────────────────────

def evaluar_modelo(valoraciones, planes):
    """
    Divide datos en train/test y calcula MAE, RMSE del filtrado colaborativo.
    Métrica principal: MAE (error absoluto medio de predicción de ratings).
    """
    train, test = train_test_split(valoraciones, test_size=0.2, random_state=42)

    matriz_train, _, _ = preprocesar_datos(train, planes)
    sim = cosine_similarity(matriz_train.values)
    sim_df = pd.DataFrame(sim, index=matriz_train.index, columns=matriz_train.index)

    predicciones, reales = [], []
    for _, row in test.iterrows():
        uid, pid, real = row['cliente_id'], row['plan_id'], row['puntuacion']
        if uid not in sim_df.index or pid not in matriz_train.columns:
            continue

        sims = sim_df[uid].drop(uid)
        ratings_otros = matriz_train[pid] if pid in matriz_train.columns else pd.Series(dtype=float)
        comunes = sims.index.intersection(ratings_otros[ratings_otros > 0].index)
        if comunes.empty:
            continue

        num = (sims[comunes] * ratings_otros[comunes]).sum()
        den = sims[comunes].abs().sum()
        if den == 0:
            continue

        pred = num / den
        predicciones.append(pred)
        reales.append(real)

    if not predicciones:
        return {'mae': None, 'rmse': None, 'n_predicciones': 0}

    mae  = mean_absolute_error(reales, predicciones)
    rmse = mean_squared_error(reales, predicciones) ** 0.5
    return {
        'mae':             round(mae, 4),
        'rmse':            round(rmse, 4),
        'n_predicciones':  len(predicciones),
        'cobertura':       f"{len(predicciones)/len(test)*100:.1f}%"
    }


# ── DEMO ──────────────────────────────────────────────────────────────────────

if __name__ == '__main__':
    valoraciones, planes = cargar_datos_demo()

    print("=" * 55)
    print("  SISTEMA DE RECOMENDACIÓN - MI DESTINO")
    print("=" * 55)

    for cid in [1, 3, 99]:
        print(f"\nRecomendaciones para cliente {cid}:")
        recomendaciones = recomendar_hibrido(cid, valoraciones, planes)
        if not recomendaciones:
            print("  → Sin recomendaciones (usuario nuevo)")
        for r in recomendaciones:
            print(f"  [{r['fuente'][:3].upper()}] {r['nombre']} "
                  f"({r['tipo_sitio']}) - score: {r['score']}")

    print("\n" + "=" * 55)
    print("  EVALUACIÓN DEL MODELO")
    print("=" * 55)
    metricas = evaluar_modelo(valoraciones, planes)
    for k, v in metricas.items():
        print(f"  {k}: {v}")
