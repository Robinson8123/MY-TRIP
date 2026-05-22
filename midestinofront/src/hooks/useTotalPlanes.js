import axios from "axios";
import { useState } from "react";
import toast from "react-hot-toast";
import { urlGeneral } from "../helpers/apiUrls";
import { useNavigate } from "react-router-dom";
import { formatearAMonedaColombia } from "../helpers/herramientas";

export const useTotalPlanes = ({
  carritoCompras,
  usuarioActivo,
  setCarritoCompras,
  setUsuarioActivo,
}) => {
  const [nombrePlan, setNombrePlan] = useState("");
  const [cantidadPersonas, setCantidadPersonas] = useState(1);
  const [comprando, setComprando] = useState(false);
  const navigate = useNavigate();

  // Calcular precios
  let precioOriginal = 0;
  for (let i = 0; i < carritoCompras.length; i++) {
    precioOriginal += carritoCompras[i].precioTotal;
  }

  const cantidadPlanes = carritoCompras.length;
  let descuento = 0;
  if (cantidadPlanes >= 6) {
    descuento = 0.2;
  } else if (cantidadPlanes >= 3) {
    descuento = 0.1;
  }

  const precioConDescuento = precioOriginal * (1 - descuento);
  const envio = 10000;
  const precioFinal = precioConDescuento + envio;
  const presupuestoEsSuficiente = usuarioActivo.presupuesto < precioFinal;

  const onComprarPlanes = async () => {
    if (comprando) return;

    setComprando(true);

    try {
      // Validaciones básicas
      if (carritoCompras.length === 0) {
        toast.error("❌ Tu carrito está vacío.");
        setComprando(false);
        return;
      }

      if (!nombrePlan || nombrePlan.trim().length < 5) {
        toast.error("📝 Ingresa un nombre para tu plan (mínimo 5 caracteres).");
        setComprando(false);
        return;
      }

      if (presupuestoEsSuficiente) {
        toast.error("💰 Presupuesto insuficiente.");
        setComprando(false);
        return;
      }

      toast.loading("Procesando tu compra...", { id: "comprando" });

     const datosCompra = {
  nombrePlan: nombrePlan.trim(),
  estado: "Comprado",
  precioTotalCompra: Number(precioFinal),
  personasDisponibles: Number(cantidadPersonas),
  fechaCompra: new Date().toISOString().split('T')[0],

  // Listas de Strings
  nombrePlanes: carritoCompras.map(c => c.planEmpresa.nombre || ""),
  tipoSitios: carritoCompras.map(c => c.planEmpresa.tipoSitio || ""),
  direcciones: carritoCompras.map(c => c.planEmpresa.direccion || ""),
  horarios: carritoCompras.map(c => c.planEmpresa.horario || ""),
  correos: carritoCompras.map(c => c.planEmpresa.email || ""),
  ciudades: carritoCompras.map(c => c.planEmpresa.ciudad || ""),
  imagenes: carritoCompras.map(c => c.planEmpresa.imagen_url || ""),
  informacionesGenerales: carritoCompras.map(c => c.planEmpresa.informacionGeneral || ""),
  
  // List<String> según tu entidad
  cantidadesCompradas: carritoCompras.map(c => String(c.cantidad)), 

  // List<Long> según tu entidad (Deben ser números directos, NO objetos)
  telefonos: carritoCompras.map(c => Number(c.planEmpresa.telefono) || 0),
  empresas: carritoCompras.map(c => Number(c.planEmpresa.empresaId)), // ¡Cambiado de objeto a número!
  planesPorEmpresa: carritoCompras.map(c => Number(c.planEmpresa.id)), // ¡Cambiado de objeto a número!

  // List<BigDecimal>
  precios: carritoCompras.map(c => Number(c.precioTotal)),

  // Objeto Cliente (MuchToOne)
  cliente: {
    idCliente: Number(usuarioActivo.idCliente)
  }
};
      console.log("Enviando JSON:", datosCompra);

      const response = await axios.post(`${urlGeneral}/compras/agregar`, datosCompra);
      
      // Manejo de respuesta exitosa
      toast.dismiss("comprando");
      toast.success("🎉 ¡Compra realizada con éxito!");

      const usuarioActualizado = {
        ...usuarioActivo,
        presupuesto: usuarioActivo.presupuesto - precioFinal,
      };

      setUsuarioActivo(usuarioActualizado);
      localStorage.setItem("usuarioActivo", JSON.stringify(usuarioActualizado));
      setCarritoCompras([]);

      setTimeout(() => {
        setComprando(false);
        navigate("/planes-comprados-clientes");
      }, 2000);

    } catch (error) {
      console.error("Error backend:", error.response?.data);
      toast.dismiss("comprando");
      
      // Extraer el mensaje de error real si existe
      const backendMsg = error.response?.data?.message || "Error al procesar la compra";
      toast.error(`❌ ${backendMsg}`);
      setComprando(false);
    }
  };

  const buildDatosCompra = () => ({
    nombrePlan: nombrePlan.trim(),
    estado: "Comprado",
    precioTotalCompra: Number(precioFinal),
    personasDisponibles: Number(cantidadPersonas),
    fechaCompra: new Date().toISOString().split("T")[0],
    nombrePlanes: carritoCompras.map((c) => c.planEmpresa.nombre || ""),
    tipoSitios: carritoCompras.map((c) => c.planEmpresa.tipoSitio || ""),
    direcciones: carritoCompras.map((c) => c.planEmpresa.direccion || ""),
    horarios: carritoCompras.map((c) => c.planEmpresa.horario || ""),
    correos: carritoCompras.map((c) => c.planEmpresa.email || ""),
    ciudades: carritoCompras.map((c) => c.planEmpresa.ciudad || ""),
    imagenes: carritoCompras.map((c) => c.planEmpresa.imagen_url || ""),
    informacionesGenerales: carritoCompras.map((c) => c.planEmpresa.informacionGeneral || ""),
    cantidadesCompradas: carritoCompras.map((c) => String(c.cantidad)),
    telefonos: carritoCompras.map((c) => Number(c.planEmpresa.telefono) || 0),
    empresas: carritoCompras.map((c) => Number(c.planEmpresa.empresaId)),
    planesPorEmpresa: carritoCompras.map((c) => Number(c.planEmpresa.id)),
    precios: carritoCompras.map((c) => Number(c.precioTotal)),
    cliente: { idCliente: Number(usuarioActivo.idCliente) },
  });

  const obtenerDatosCompraParaPago = () => {
    if (carritoCompras.length === 0) {
      toast.error("❌ Tu carrito está vacío.");
      return null;
    }
    if (!nombrePlan || nombrePlan.trim().length < 5) {
      toast.error("📝 Ingresa un nombre para tu plan (mínimo 5 caracteres).");
      return null;
    }
    if (presupuestoEsSuficiente) {
      toast.error("💰 Presupuesto insuficiente.");
      return null;
    }
    return { monto: precioFinal, moneda: "COP" };
  };

  const solicitarDatosPago = async (datosCompra) => {
    const firmaResponse = await axios.post(`${urlGeneral}/pagos/firma`, {
      monto: datosCompra.monto,
      moneda: datosCompra.moneda,
    });

    const data = firmaResponse.data;
    if (data.valid && data.pagoDatos?.referencia) {
      const pendingPurchase = buildDatosCompra();
      sessionStorage.setItem(
        `pendingPurchase_${data.pagoDatos.referencia}`,
        JSON.stringify(pendingPurchase)
      );
    }
    return data;
  };

  return {
    precioFinal,
    onComprarPlanes,
    nombrePlan,
    setNombrePlan,
    presupuestoEsSuficiente,
    precioOriginal,
    precioConDescuento,
    cantidadPlanes,
    envio,
    cantidadPersonas,
    setCantidadPersonas,
    comprando,
    setComprando,
    obtenerDatosCompraParaPago,
    solicitarDatosPago,
  };
}; // Aquí se cierra el hook correctamente