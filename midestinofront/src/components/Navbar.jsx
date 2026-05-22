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

  // Cierra el menú al hacer scroll
  useEffect(() => {
    if (menuAbierto) setMenuAbierto(false);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isScrolled]);

  const perfilHref = isUsuarioActivo
    ? `/perfil/${usuarioActivo.tipoUsuario !== "Empresa" ? usuarioActivo.idCliente : usuarioActivo.idEmpresa}`
    : "/";

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
        <div className="flex items-center justify-between w-[90%] mx-auto py-3">
          {/* Logo */}
          <Link to="/" className="flex items-center space-x-3">
            <img src={logo} className="h-12" alt="My Trip Logo" />
            <span className="self-center text-xl font-semibold whitespace-nowrap">
              My Trip
            </span>
          </Link>

          {/* Botón hamburguesa — visible solo en móvil */}
          <button
            onClick={() => setMenuAbierto(!menuAbierto)}
            className="lg:hidden flex items-center justify-center min-w-[48px] min-h-[48px] rounded-lg bg-gray-100 hover:bg-gray-200 transition-colors"
            aria-label="Abrir menú"
            aria-expanded={menuAbierto}
          >
            {menuAbierto ? (
              <svg className="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            ) : (
              <svg className="w-6 h-6 text-gray-700" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16" />
              </svg>
            )}
          </button>

          {/* Acciones — desktop: inline / móvil: oculto (aparece en dropdown) */}
          <div className="hidden lg:flex items-center gap-2">
            {isUsuarioActivo ? (
              <>
                <button
                  onClick={cerrarSesion}
                  className="min-h-[48px] px-6 bg-blue-500 hover:bg-blue-600 text-sm text-white font-bold rounded-md transition duration-200"
                >
                  Cerrar sesión
                </button>
                <Link
                  to={perfilHref}
                  className="flex items-center justify-center min-w-[48px] min-h-[48px] bg-gray-50 hover:bg-gray-300 text-blue-500 font-bold rounded-full transition duration-200"
                  title="Perfil"
                >
                  <svg className="w-6 h-6 text-gray-800" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                      d="M7 6H5m2 3H5m2 3H5m2 3H5m2 3H5m11-1a2 2 0 0 0-2-2h-2a2 2 0 0 0-2 2M7 3h11a1 1 0 0 1 1 1v16a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Zm8 7a2 2 1 1-4 0 2 2 0 0 1 4 0Z" />
                  </svg>
                </Link>
              </>
            ) : (
              <>
                <Link
                  to="/ingresar"
                  className="flex items-center min-h-[48px] px-6 bg-gray-50 hover:bg-gray-300 text-sm text-blue-500 font-bold rounded-md transition duration-200"
                >
                  Ingresa
                </Link>
                <Link
                  to="tipo-usuario"
                  className="flex items-center min-h-[48px] px-6 bg-blue-500 hover:bg-blue-700 text-sm text-white font-bold rounded-md transition duration-200"
                >
                  Regístrate
                </Link>
              </>
            )}
          </div>
        </div>

        {/* Menú desplegable móvil */}
        {menuAbierto && (
          <div className="lg:hidden bg-white border-t border-gray-100 px-4 pb-4 flex flex-col gap-3">
            {isUsuarioActivo ? (
              <>
                <Link
                  to={perfilHref}
                  onClick={() => setMenuAbierto(false)}
                  className="flex items-center gap-3 min-h-[48px] px-4 bg-gray-50 hover:bg-gray-100 rounded-lg text-gray-700 font-medium transition"
                >
                  <svg className="w-5 h-5 text-gray-600" fill="none" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                    <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                      d="M7 6H5m2 3H5m2 3H5m2 3H5m2 3H5m11-1a2 2 0 0 0-2-2h-2a2 2 0 0 0-2 2M7 3h11a1 1 0 0 1 1 1v16a1 1 0 0 1-1 1H7a1 1 0 0 1-1-1V4a1 1 0 0 1 1-1Zm8 7a2 2 1 1-4 0 2 2 0 0 1 4 0Z" />
                  </svg>
                  Mi perfil
                </Link>
                <button
                  onClick={() => { cerrarSesion(); setMenuAbierto(false); }}
                  className="min-h-[48px] w-full px-4 bg-blue-500 hover:bg-blue-600 text-white font-bold rounded-lg transition duration-200"
                >
                  Cerrar sesión
                </button>
              </>
            ) : (
              <>
                <Link
                  to="/ingresar"
                  onClick={() => setMenuAbierto(false)}
                  className="flex items-center justify-center min-h-[48px] px-4 bg-gray-50 hover:bg-gray-200 text-blue-500 font-bold rounded-lg transition duration-200"
                >
                  Ingresa
                </Link>
                <Link
                  to="tipo-usuario"
                  onClick={() => setMenuAbierto(false)}
                  className="flex items-center justify-center min-h-[48px] px-4 bg-blue-500 hover:bg-blue-700 text-white font-bold rounded-lg transition duration-200"
                >
                  Regístrate
                </Link>
              </>
            )}
          </div>
        )}
      </nav>
    </div>
  );
};
