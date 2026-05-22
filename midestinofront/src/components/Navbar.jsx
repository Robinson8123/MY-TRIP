import { Link } from "react-router-dom";
import { logo } from "../images";
import { UsuarioContext } from "../context/UsuarioContext";
import { useContext, useEffect, useState } from "react";

export const Navbar = () => {
  const { isUsuarioActivo, usuarioActivo, cerrarSesion } =
    useContext(UsuarioContext);
  const [isScrolled, setIsScrolled] = useState(false);
  const [menuAbierto, setMenuAbierto] = useState(false);

  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 0);
    };
    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, []);

  const cerrarMenu = () => setMenuAbierto(false);

  return (
    <div
      className={`shadow sticky top-0 left-0 w-full z-50 transition-colors duration-500 ${
        isScrolled ? "bg-white" : "bg-transparent"
      }`}
    >
      <nav
        className={`border-gray-200 transition-colors duration-500 ${
          isScrolled ? "bg-white" : "bg-[#ffffff26]"
        }`}
      >
        <div className="flex flex-wrap justify-between items-center mx-auto w-[90%] p-4">
          <Link
            to="/"
            className="flex items-center space-x-3 rtl:space-x-reverse"
            onClick={cerrarMenu}
          >
            <img src={logo} className="h-10 md:h-14" alt="My Trip Logo" />
            <span className="self-center text-xl md:text-2xl font-semibold whitespace-nowrap">
              My Trip
            </span>
          </Link>

          {/* Desktop links */}
          <div className="hidden md:flex items-center gap-2">
            <Link
              to="/descargar-app"
              className="flex items-center gap-1.5 py-2 px-4 bg-green-500 hover:bg-green-600 text-sm text-white font-bold rounded-md transition duration-200"
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5"
                  d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
              </svg>
              App Android
            </Link>
            {isUsuarioActivo ? (
              <>
                <button
                  className="py-2 px-6 bg-blue-500 hover:bg-blue-600 text-sm text-white font-bold rounded-md transition duration-200"
                  onClick={cerrarSesion}
                >
                  Cerrar sesión
                </button>
                <Link
                  to={`/perfil/${
                    usuarioActivo.tipoUsuario !== "Empresa"
                      ? usuarioActivo.idCliente
                      : usuarioActivo.idEmpresa
                  }`}
                  className="p-2 bg-gray-50 hover:bg-gray-300 text-sm text-blue-500 font-bold rounded-full transition duration-200"
                >
                  <svg className="w-6 h-6 text-gray-800" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="none" viewBox="0 0 24 24">
                    <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M7 6H5m2 3H5m2 3H5m2 3H5m2 3H5m11-1a2 2 0 0 0-2-2h-2a2 2 0 0 0-2 2M7 3h11a1 1 0 0 1 1 1v16a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Zm8 7a2 2 0 1 1-4 0 2 2 0 0 1 4 0Z" />
                  </svg>
                </Link>
              </>
            ) : (
              <>
                <Link
                  to="/ingresar"
                  className="py-2 px-6 bg-gray-50 hover:bg-gray-300 text-sm text-blue-500 font-bold rounded-md transition duration-200"
                >
                  Ingresa
                </Link>
                <Link
                  to="tipo-usuario"
                  className="py-2 px-6 bg-blue-500 hover:bg-blue-700 text-sm text-white font-bold rounded-md transition duration-200"
                >
                  Registrate
                </Link>
              </>
            )}
          </div>

          {/* Hamburger button (mobile) */}
          <button
            className="md:hidden flex flex-col justify-center items-center w-10 h-10 rounded-lg focus:outline-none"
            onClick={() => setMenuAbierto(!menuAbierto)}
            aria-label="Menú"
          >
            <span className={`block w-6 h-0.5 bg-gray-800 transition-all duration-300 ${menuAbierto ? "rotate-45 translate-y-1.5" : ""}`} />
            <span className={`block w-6 h-0.5 bg-gray-800 my-1 transition-all duration-300 ${menuAbierto ? "opacity-0" : ""}`} />
            <span className={`block w-6 h-0.5 bg-gray-800 transition-all duration-300 ${menuAbierto ? "-rotate-45 -translate-y-1.5" : ""}`} />
          </button>
        </div>

        {/* Mobile menu */}
        {menuAbierto && (
          <div className="md:hidden bg-white border-t border-gray-200 px-6 py-4 flex flex-col gap-3">
            <Link
              to="/descargar-app"
              className="flex items-center justify-center gap-2 py-2 px-4 bg-green-500 text-white font-bold rounded-md text-center"
              onClick={cerrarMenu}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2.5"
                  d="M4 16v1a3 3 0 003 3h10a3 3 0 003-3v-1m-4-4l-4 4m0 0l-4-4m4 4V4" />
              </svg>
              Descargar App Android
            </Link>
            {isUsuarioActivo ? (
              <>
                <Link
                  to={`/perfil/${
                    usuarioActivo.tipoUsuario !== "Empresa"
                      ? usuarioActivo.idCliente
                      : usuarioActivo.idEmpresa
                  }`}
                  className="py-2 px-4 text-blue-500 font-semibold border border-blue-200 rounded-md text-center"
                  onClick={cerrarMenu}
                >
                  Mi Perfil
                </Link>
                <button
                  className="py-2 px-4 bg-blue-500 text-white font-bold rounded-md"
                  onClick={() => { cerrarSesion(); cerrarMenu(); }}
                >
                  Cerrar sesión
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/ingresar"
                  className="py-2 px-4 text-blue-500 font-semibold border border-blue-200 rounded-md text-center"
                  onClick={cerrarMenu}
                >
                  Ingresa
                </Link>
                <Link
                  to="tipo-usuario"
                  className="py-2 px-4 bg-blue-500 text-white font-bold rounded-md text-center"
                  onClick={cerrarMenu}
                >
                  Registrate
                </Link>
              </>
            )}
          </div>
        )}
      </nav>
    </div>
  );
};
