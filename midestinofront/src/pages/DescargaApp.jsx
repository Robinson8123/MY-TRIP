import { Navbar } from "../components/Navbar";
import { Footer } from "../components/Footer";

const API_URL = import.meta.env.VITE_API_URL || "http://localhost:9999/api";

const features = [
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
      </svg>
    ),
    titulo: "Explora destinos",
    desc: "Descubre planes turísticos en Cartagena, Medellín, Bogotá y más ciudades de Colombia.",
  },
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M9.75 17L9 20l-1 1h8l-1-1-.75-3M3 13h18M5 17H3a2 2 0 01-2-2V5a2 2 0 012-2h14a2 2 0 012 2v10a2 2 0 01-2 2h-2" />
      </svg>
    ),
    titulo: "Recomendaciones IA",
    desc: "Red neuronal que aprende tus preferencias y sugiere los mejores planes para ti.",
  },
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z" />
      </svg>
    ),
    titulo: "Chatbot de viajes",
    desc: "Asistente inteligente con LLaMA 3.3 para resolver todas tus dudas sobre destinos.",
  },
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
      </svg>
    ),
    titulo: "Carrito de compras",
    desc: "Agrega planes, gestiona tu presupuesto y paga de forma segura con PayU.",
  },
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
      </svg>
    ),
    titulo: "Mapas interactivos",
    desc: "Visualiza la ubicación exacta de cada plan y planifica tu ruta fácilmente.",
  },
  {
    icon: (
      <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
          d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
      </svg>
    ),
    titulo: "Perfil personalizado",
    desc: "Gestiona tu cuenta, historial de compras y preferencias desde un solo lugar.",
  },
];

const pasos = [
  { num: "1", texto: "Descarga el archivo APK pulsando el botón." },
  { num: "2", texto: 'Activa "Instalar apps desconocidas" en Ajustes → Seguridad.' },
  { num: "3", texto: "Abre el APK desde tu explorador de archivos." },
  { num: "4", texto: "Presiona Instalar y disfruta Mi Destino." },
];

export const DescargaApp = () => {
  const apkUrl = `${API_URL}/descarga/apk`;

  return (
    <main className="bg-gray-50 min-h-screen">
      <Navbar />

      {/* Hero */}
      <section className="bg-gradient-to-br from-blue-600 via-blue-700 to-indigo-800 text-white py-20 px-6">
        <div className="max-w-5xl mx-auto flex flex-col md:flex-row items-center gap-12">
          <div className="flex-1 text-center md:text-left">
            <span className="inline-block bg-white/20 text-white text-sm font-semibold px-4 py-1 rounded-full mb-4">
              App Android — v1.0
            </span>
            <h1 className="text-4xl md:text-5xl font-extrabold leading-tight mb-4">
              Mi Destino <br />
              <span className="text-blue-200">en tu bolsillo</span>
            </h1>
            <p className="text-blue-100 text-lg mb-8 max-w-md mx-auto md:mx-0">
              Descarga la app móvil y lleva la plataforma de turismo inteligente
              a donde quieras. Disponible para Android.
            </p>
            <a
              href={apkUrl}
              download="MiDestino.apk"
              className="inline-flex items-center gap-3 bg-white text-blue-700 font-bold text-lg px-8 py-4 rounded-xl shadow-lg hover:bg-blue-50 active:scale-95 transition-all duration-200"
            >
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5"
                  d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
              </svg>
              Descargar APK
            </a>
            <p className="text-blue-200 text-sm mt-3">
              163 MB — Android 7.0+
            </p>
          </div>

          {/* Phone mockup */}
          <div className="flex-shrink-0">
            <div className="relative w-48 h-80 bg-gray-900 rounded-3xl shadow-2xl border-4 border-gray-700 flex items-center justify-center mx-auto">
              <div className="absolute top-3 w-16 h-1.5 bg-gray-700 rounded-full" />
              <div className="w-full h-full rounded-2xl overflow-hidden flex flex-col bg-gradient-to-b from-blue-500 to-indigo-600 p-4 pt-8">
                <div className="text-white text-center">
                  <div className="text-3xl font-black mb-1">MT</div>
                  <div className="text-xs font-semibold opacity-80">Mi Destino</div>
                </div>
                <div className="mt-4 space-y-2">
                  {["Explorar planes", "Mi carrito", "Recomendaciones IA", "Chat IA"].map((item) => (
                    <div key={item} className="bg-white/20 rounded-lg px-3 py-2 text-white text-xs font-medium">
                      {item}
                    </div>
                  ))}
                </div>
              </div>
              <div className="absolute bottom-2 w-10 h-1 bg-gray-600 rounded-full" />
            </div>
          </div>
        </div>
      </section>

      {/* Features */}
      <section className="py-16 px-6">
        <div className="max-w-5xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-3">
            Todo lo que incluye la app
          </h2>
          <p className="text-center text-gray-500 mb-12">
            Las mismas funciones de la web, optimizadas para tu celular.
          </p>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {features.map((f, i) => (
              <div key={i} className="bg-white rounded-xl p-6 shadow-sm border border-gray-100 hover:shadow-md transition-shadow">
                <div className="w-12 h-12 bg-blue-50 text-blue-600 rounded-xl flex items-center justify-center mb-4">
                  {f.icon}
                </div>
                <h3 className="font-bold text-gray-800 mb-1">{f.titulo}</h3>
                <p className="text-sm text-gray-500 leading-relaxed">{f.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Steps */}
      <section className="bg-white py-16 px-6 border-t border-gray-100">
        <div className="max-w-3xl mx-auto">
          <h2 className="text-3xl font-bold text-center text-gray-800 mb-3">
            ¿Cómo instalarla?
          </h2>
          <p className="text-center text-gray-500 mb-12">
            En 4 pasos sencillos ya tienes Mi Destino en tu Android.
          </p>
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
            {pasos.map((p) => (
              <div key={p.num} className="flex items-start gap-4">
                <div className="w-10 h-10 flex-shrink-0 bg-blue-600 text-white font-bold rounded-full flex items-center justify-center text-lg">
                  {p.num}
                </div>
                <p className="text-gray-700 mt-1.5 leading-relaxed">{p.texto}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA final */}
      <section className="py-16 px-6 bg-gradient-to-r from-blue-600 to-indigo-700 text-white text-center">
        <h2 className="text-3xl font-bold mb-3">¿Listo para explorar Colombia?</h2>
        <p className="text-blue-100 mb-8">Descarga Mi Destino y comienza tu aventura hoy.</p>
        <a
          href={apkUrl}
          download="MiDestino.apk"
          className="inline-flex items-center gap-3 bg-white text-blue-700 font-bold text-lg px-8 py-4 rounded-xl shadow-lg hover:bg-blue-50 active:scale-95 transition-all duration-200"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5"
              d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
          </svg>
          Descargar APK — gratis
        </a>
      </section>

      <Footer />
    </main>
  );
};
