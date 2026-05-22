import { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import axios from "axios";
import { urlGeneral } from "../helpers/apiUrls";
import toast from "react-hot-toast";

// PayU transactionState codes
const ESTADO = {
  4: "approved",
  6: "declined",
  104: "error",
  7: "pending",
};

export const PagoResultado = () => {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [procesando, setProcesando] = useState(true);
  const [resultado, setResultado] = useState(null);

  useEffect(() => {
    const procesar = async () => {
      const referenceCode = searchParams.get("referenceCode");
      const transactionState = Number(searchParams.get("transactionState"));
      const message = searchParams.get("message") || "";

      if (!referenceCode) {
        setResultado({ ok: false, mensaje: "Referencia de pago no encontrada." });
        setProcesando(false);
        return;
      }

      const estado = ESTADO[transactionState] || "unknown";

      if (estado !== "approved") {
        sessionStorage.removeItem(`pendingPurchase_${referenceCode}`);
        setResultado({
          ok: false,
          mensaje:
            estado === "declined"
              ? "Pago rechazado. Por favor intenta con otro método de pago."
              : estado === "pending"
              ? "Tu pago está pendiente de confirmación."
              : `Error en el pago: ${message}`,
        });
        setProcesando(false);
        return;
      }

      const pendingRaw = sessionStorage.getItem(`pendingPurchase_${referenceCode}`);
      if (!pendingRaw) {
        setResultado({ ok: false, mensaje: "Datos de compra no encontrados. Contacta soporte." });
        setProcesando(false);
        return;
      }

      const datosCompra = JSON.parse(pendingRaw);
      sessionStorage.removeItem(`pendingPurchase_${referenceCode}`);

      try {
        const response = await axios.post(`${urlGeneral}/compras/agregar`, datosCompra);
        if (response.data.valid || response.status === 200) {
          setResultado({ ok: true, mensaje: "¡Compra realizada con éxito! Gracias por tu pago." });
          toast.success("🎉 ¡Compra confirmada!");
        } else {
          throw new Error(response.data.message || "Error al procesar la compra");
        }
      } catch (err) {
        setResultado({
          ok: false,
          mensaje: `Pago recibido pero error al registrar compra: ${err.response?.data?.message || err.message}`,
        });
      }

      setProcesando(false);
    };

    procesar();
  }, [searchParams]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 px-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-lg p-8 text-center">
        {procesando ? (
          <>
            <div className="flex justify-center mb-4">
              <svg className="animate-spin h-12 w-12 text-blue-500" viewBox="0 0 24 24">
                <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none" />
                <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z" />
              </svg>
            </div>
            <p className="text-gray-600 font-medium">Procesando tu compra...</p>
          </>
        ) : resultado?.ok ? (
          <>
            <div className="flex justify-center mb-4">
              <div className="bg-green-100 rounded-full p-4">
                <svg className="h-12 w-12 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 13l4 4L19 7" />
                </svg>
              </div>
            </div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">¡Pago exitoso!</h2>
            <p className="text-gray-600 mb-6">{resultado.mensaje}</p>
            <button
              className="w-full bg-green-600 hover:bg-green-700 text-white font-semibold py-3 px-6 rounded-lg transition-all"
              onClick={() => navigate("/planes-comprados-clientes")}
            >
              Ver mis planes comprados
            </button>
          </>
        ) : (
          <>
            <div className="flex justify-center mb-4">
              <div className="bg-red-100 rounded-full p-4">
                <svg className="h-12 w-12 text-red-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
              </div>
            </div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Pago no completado</h2>
            <p className="text-gray-600 mb-6">{resultado?.mensaje}</p>
            <button
              className="w-full bg-blue-600 hover:bg-blue-700 text-white font-semibold py-3 px-6 rounded-lg transition-all"
              onClick={() => navigate("/planes-seleccionados-clientes")}
            >
              Volver al carrito
            </button>
          </>
        )}
      </div>
    </div>
  );
};
