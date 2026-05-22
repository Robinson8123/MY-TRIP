"""
Microservicio FastAPI - Sistema de Recomendación Mi Destino
Expone el modelo como REST API para consumo desde Spring Boot.
"""

import os
from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from typing import List, Optional
import pandas as pd
import sqlalchemy
from dotenv import load_dotenv
from modelo_recomendacion import recomendar_hibrido, cargar_datos_demo

load_dotenv()

app = FastAPI(
    title="Mi Destino - Servicio de Recomendaciones",
    description="Sistema de recomendación híbrido (colaborativo + contenido)",
    version="1.0.0"
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

# Conexión a la BD (producción) o datos demo (desarrollo)
DB_URL = os.getenv("MYSQL_URL_PYTHON")


def cargar_datos_bd():
    """Carga datos reales desde MySQL."""
    try:
        engine = sqlalchemy.create_engine(DB_URL)
        valoraciones = pd.read_sql(
            "SELECT cliente_id, plan_empresa_id AS plan_id, puntuacion FROM Valoraciones",
            engine
        )
        planes = pd.read_sql(
            """SELECT id AS plan_id, nombre, tipo_sitio AS tipo_sitio,
               precio, valoracion_promedio AS valoracion_prom, ciudad
               FROM PlanesEmpresas WHERE disponible = 1""",
            engine
        )
        return valoraciones, planes
    except Exception:
        return cargar_datos_demo()


class RecomendacionResponse(BaseModel):
    plan_id: int
    nombre: str
    tipo_sitio: str
    score: float
    fuente: str
    ciudad: Optional[str] = ""


@app.get("/health")
def health():
    return {"status": "ok", "servicio": "recomendaciones"}


@app.get("/recomendaciones/{cliente_id}", response_model=List[RecomendacionResponse])
def obtener_recomendaciones(cliente_id: int, top_n: int = 6):
    """
    Retorna los top_n planes recomendados para el cliente dado.
    Usa filtrado colaborativo + basado en contenido.
    """
    try:
        valoraciones, planes = cargar_datos_bd()
        resultado = recomendar_hibrido(cliente_id, valoraciones, planes, top_n)
        return resultado
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))


@app.get("/metricas")
def metricas_modelo():
    """Retorna MAE y RMSE del modelo sobre datos de prueba."""
    from modelo_recomendacion import evaluar_modelo
    valoraciones, planes = cargar_datos_bd()
    return evaluar_modelo(valoraciones, planes)


if __name__ == "__main__":
    import uvicorn
    port = int(os.getenv("PORT", 8000))
    uvicorn.run(app, host="0.0.0.0", port=port)
