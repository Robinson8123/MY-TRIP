import { useContext, useEffect, useState } from "react";
import { TemplateMainCliente } from "../templates/TemplateMainCliente";
import { UsuarioContext } from "../context/UsuarioContext";
import { formatearAMonedaColombia } from "../helpers/herramientas";

const MINERIA_URL = "https://confident-truth-production.up.railway.app";

export const RecomendacionesIA = () => {
  const { usuarioActivo } = useContext(UsuarioContext);
  const [recomendaciones, setRecomendaciones] = useState([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const clienteId = usuarioActivo?.idCliente || 1;

    const fetchRecomendaciones = async () => {
      setCargando(true);
      setError(null);
      try {
        const res = await fetch(`${MINERIA_URL}/recomendaciones/${clienteId}`);
        if (!res.ok) throw new Error("Error al obtener recomendaciones");
        const data = await res.json();
        setRecomendaciones(data);
      } catch (e) {
        setError("No se pudieron cargar las recomendaciones. Intenta de nuevo.");
      } finally {
        setCargando(false);
      }
    };

    fetchRecomendaciones();
  }, [usuarioActivo?.idCliente]);

  const fuenteColor = (fuente) => {
    if (fuente === "red_neuronal") return "bg-purple-100 text-purple-700";
    if (fuente === "colaborativo")  return "bg-blue-100 text-blue-700";
    return "bg-green-100 text-green-700";
  };

  const fuenteLabel = (fuente) => {
    if (fuente === "red_neuronal") return "🧠 Red Neuronal";
    if (fuente === "colaborativo")  return "👥 Colaborativo";
    return "📋 Contenido";
  };

  return (
    <TemplateMainCliente titulo="Recomendaciones IA">
      <section className="py-4">
        {/* Encabezado */}
        <div className="mb-6 p-4 bg-gradient-to-r from-purple-600 to-blue-600 rounded-xl text-white">
          <div className="flex items-center gap-3 mb-2">
            <span className="text-3xl">🧠</span>
            <div>
              <h3 className="text-lg font-bold">Motor de Recomendaciones — Red Neuronal</h3>
              <p className="text-sm text-purple-100">
                Servicio de Minería de Datos · MLPRegressor + Filtrado Colaborativo
              </p>
            </div>
          </div>
          <p className="text-sm text-purple-100">
            Planes seleccionados para ti basados en tus preferencias y el comportamiento de usuarios similares.
          </p>
        </div>

        {/* Estados */}
        {cargando && (
          <div className="flex flex-col items-center justify-center py-16 gap-4">
            <div className="w-12 h-12 border-4 border-purple-500 border-t-transparent rounded-full animate-spin" />
            <p className="text-gray-500 text-sm">Consultando red neuronal...</p>
          </div>
        )}

        {error && !cargando && (
          <div className="text-center py-12">
            <p className="text-red-500 mb-4">{error}</p>
            <button
              onClick={() => window.location.reload()}
              className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700"
            >
              Reintentar
            </button>
          </div>
        )}

        {!cargando && !error && recomendaciones.length === 0 && (
          <div className="text-center py-12 text-gray-500">
            <span className="text-4xl block mb-3">🔍</span>
            <p>No hay recomendaciones disponibles aún.</p>
            <p className="text-sm mt-1">Califica algunos planes para mejorar las sugerencias.</p>
          </div>
        )}

        {/* Tarjetas */}
        {!cargando && !error && recomendaciones.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {recomendaciones.map((rec, index) => (
              <div
                key={rec.plan_id}
                className="bg-white rounded-xl shadow-md hover:shadow-lg transition-shadow border border-gray-100 overflow-hidden"
              >
                {/* Posición */}
                <div className="bg-gradient-to-r from-purple-500 to-blue-500 px-4 py-2 flex justify-between items-center">
                  <span className="text-white font-bold text-sm">#{index + 1} Recomendado</span>
                  <span className={`text-xs px-2 py-1 rounded-full font-medium ${fuenteColor(rec.fuente)}`}>
                    {fuenteLabel(rec.fuente)}
                  </span>
                </div>

                <div className="p-4">
                  <h4 className="font-semibold text-gray-800 text-base mb-1">{rec.nombre}</h4>
                  <p className="text-sm text-gray-500 mb-3">{rec.tipo_sitio}</p>

                  {/* Score barra */}
                  <div className="mb-3">
                    <div className="flex justify-between text-xs text-gray-500 mb-1">
                      <span>Score de relevancia</span>
                      <span className="font-semibold text-purple-600">{rec.score.toFixed(3)}</span>
                    </div>
                    <div className="w-full bg-gray-100 rounded-full h-2">
                      <div
                        className="bg-gradient-to-r from-purple-500 to-blue-500 h-2 rounded-full"
                        style={{ width: `${Math.min(rec.score * 25, 100)}%` }}
                      />
                    </div>
                  </div>

                  {/* Ciudad si existe */}
                  {rec.ciudad && (
                    <p className="text-xs text-gray-400 flex items-center gap-1">
                      <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                      {rec.ciudad}
                    </p>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}

        {/* Enlace directo al servicio */}
        <div className="mt-8 p-4 bg-gray-50 rounded-xl border border-gray-200 flex flex-col sm:flex-row items-center justify-between gap-3">
          <div>
            <p className="text-sm font-medium text-gray-700">Servicio de Minería de Datos</p>
            <p className="text-xs text-gray-400">{MINERIA_URL}</p>
          </div>
          <a
            href={`${MINERIA_URL}/docs`}
            target="_blank"
            rel="noopener noreferrer"
            className="px-4 py-2 bg-purple-600 text-white text-sm font-medium rounded-lg hover:bg-purple-700 transition-colors whitespace-nowrap"
          >
            Ver API Docs
          </a>
        </div>
      </section>
    </TemplateMainCliente>
  );
};
