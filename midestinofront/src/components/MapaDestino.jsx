import { useEffect } from "react";
import { MapContainer, TileLayer, Marker, Popup } from "react-leaflet";
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import { PropTypes } from "prop-types";

// Corrige el icono por defecto de Leaflet en bundlers como Vite/Webpack
delete L.Icon.Default.prototype._getIconUrl;
L.Icon.Default.mergeOptions({
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
});

export const MapaDestino = ({ lat, lng, nombre, direccion, precio }) => {
  const posicion = [lat ?? 10.3910, lng ?? -75.4794]; // Cartagena como fallback

  useEffect(() => {
    // Fuerza redibujado cuando el contenedor cambia de tamaño (responsive)
    window.dispatchEvent(new Event("resize"));
  }, [lat, lng]);

  if (!lat || !lng) {
    return (
      <div className="w-full h-48 flex items-center justify-center bg-gray-100 rounded-xl text-gray-500 text-sm">
        Ubicación no disponible
      </div>
    );
  }

  return (
    <div className="w-full rounded-xl overflow-hidden shadow-md" style={{ height: "clamp(200px, 40vw, 400px)" }}>
      <MapContainer
        center={posicion}
        zoom={15}
        scrollWheelZoom={false}
        style={{ height: "100%", width: "100%" }}
        className="z-0"
      >
        <TileLayer
          attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
          url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
        />
        <Marker position={posicion}>
          <Popup>
            <div className="text-sm">
              <p className="font-semibold">{nombre}</p>
              {direccion && <p className="text-gray-600">{direccion}</p>}
              {precio && (
                <p className="text-green-600 font-medium mt-1">
                  ${Number(precio).toLocaleString("es-CO")} COP
                </p>
              )}
            </div>
          </Popup>
        </Marker>
      </MapContainer>
    </div>
  );
};

MapaDestino.propTypes = {
  lat: PropTypes.number,
  lng: PropTypes.number,
  nombre: PropTypes.string,
  direccion: PropTypes.string,
  precio: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
};
