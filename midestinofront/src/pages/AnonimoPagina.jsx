import { Link } from "react-router-dom";
import { Footer } from "../components/Footer";
import { Navbar } from "../components/Navbar";
import { SectionAnonimo } from "../components/SectionAnonimo";
import { TarjetaPlanesAnonimo } from "../components/TarjetaPlanesPromocional";
import { planesPromocionales } from "../helpers/TarjetasPromocionales";
import { useContext } from "react";
import { UsuarioContext } from "../context/UsuarioContext";
import { CaruselDeInformacion } from "../components/CaruselDeInformacion";

export const AnonimoPagina = () => {
  const { usuarioActivo, isUsuarioActivo } = useContext(UsuarioContext);

  const rutas = {
    Cliente: "/inicio-clientes",
    Empresa: "/inicio-empresas",
    Administrador: "/inicio-administradores",
  };

  const enviarA = !isUsuarioActivo
    ? "/inicio-clientes"
    : rutas[usuarioActivo.tipoUsuario] || "/";

  return (
    <main>
      <Navbar />

      <figure className="absolute w-full h-full top-0 -z-10 bg-cover">
        <img
          src="/src/images/fondo1.jpg"
          alt="fondo"
          className="w-full h-full object-cover filter brightness-75"
        />
      </figure>

      <section className="relative flex flex-col items-start justify-center min-h-screen px-6 md:pl-40">
        <div className="w-full md:w-[40%] px-4 md:px-8 shadow-xl rounded-lg">
          <h1 className="text-4xl sm:text-5xl md:text-7xl m-0 mb-6 font-extrabold text-white leading-tight">
            Bienvenido a <span className="text-blue-400">My Trip</span>
          </h1>
          <p className="pt-4 text-base md:text-xl font-medium text-gray-200">
            <span className="text-white">Descubre</span> la forma más fácil de
            planificar tus vacaciones soñadas. Desde destinos exóticos hasta
            escapadas locales, en My Trip te ayudamos a organizar cada detalle
            para que disfrutes de una experiencia inolvidable. ¡Únete y comienza
            a explorar hoy mismo!
          </p>

          <div className="flex space-x-4 mt-8">
            <Link
              to={enviarA}
              className="py-3 px-6 bg-blue-500 text-white font-semibold rounded-md hover:bg-blue-600 transition duration-200"
            >
              Ver Planes
            </Link>
            <a
              href="#planes"
              id="conocerMasBtn"
              className="py-3 px-6 bg-white text-blue-500 font-semibold rounded-md hover:bg-gray-100 transition duration-200"
            >
              Conocer más
            </a>
          </div>
        </div>
      </section>

      <SectionAnonimo />

      <CaruselDeInformacion />

      <section id="planes" className="px-10 py-40 bg-gray-100">
        <h2 className="text-4xl font-bold text-center text-gray-800 mb-12">
          ¡Tipos de planes que puedes encontrar aqui!
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {planesPromocionales.map((plan, index) => (
            <TarjetaPlanesAnonimo key={index} plan={plan} />
          ))}
        </div>
      </section>

      {/* Descarga App section */}
      <section className="py-16 px-6 bg-gradient-to-r from-blue-600 to-indigo-700">
        <div className="max-w-4xl mx-auto flex flex-col md:flex-row items-center gap-8">
          <div className="text-white flex-1 text-center md:text-left">
            <h2 className="text-3xl font-bold mb-3">Lleva Mi Destino en tu bolsillo</h2>
            <p className="text-blue-100 mb-6">
              Descarga la app Android y explora, compara y compra planes turísticos desde tu celular. Con recomendaciones IA y chatbot integrado.
            </p>
            <Link
              to="/descargar-app"
              className="inline-flex items-center gap-2 bg-white text-blue-700 font-bold px-6 py-3 rounded-xl hover:bg-blue-50 transition-all duration-200 shadow-lg"
            >
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5"
                  d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
              </svg>
              Descargar APK para Android
            </Link>
          </div>
          <div className="flex gap-4 text-white text-center">
            {[
              { val: "30+", label: "Planes" },
              { val: "IA", label: "Recomendaciones" },
              { val: "🤖", label: "Chatbot" },
            ].map((stat) => (
              <div key={stat.label} className="bg-white/20 rounded-xl px-6 py-4">
                <div className="text-2xl font-black">{stat.val}</div>
                <div className="text-xs text-blue-100 mt-1">{stat.label}</div>
              </div>
            ))}
          </div>
        </div>
      </section>

      <Footer />
    </main>
  );
};
