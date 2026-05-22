import { PropTypes } from "prop-types";
import { useRef, useState } from "react";
import toast from "react-hot-toast";

export const BotonPago = ({
    disabled,
    comprando,
    setComprando,
    obtenerDatosCompraParaPago,
    solicitarDatosPago,
    onPagoFallido,
}) => {
    // Referencia para el formulario oculto de PayU
    const formRef = useRef(null);
    const [datosPayU, setDatosPayU] = useState(null);

    const iniciarPago = async () => {
        if (disabled || comprando) return;

        const datosCompra = obtenerDatosCompraParaPago();
        if (!datosCompra) return;

        try {
            setComprando(true);
            toast.loading("Preparando checkout con PayU...", { id: "pago-payu" });

            // Esta función llama a tu PagoService en Spring Boot para generar la firma
            const response = await solicitarDatosPago(datosCompra);

            if (!response || !response.valid) {
                throw new Error(response?.message || "No se recibieron datos de pago válidos");
            }

            const { pagoDatos } = response;

            // Actualizamos el estado para llenar el formulario oculto
            setDatosPayU(pagoDatos);
            toast.dismiss("pago-payu");

            // Pequeña pausa para asegurar que el DOM se actualice con los nuevos inputs y enviamos
            setTimeout(() => {
                if (formRef.current) {
                    formRef.current.submit();
                }
            }, 500);

        } catch (error) {
            const backendMsg = error.response?.data?.message || error.message;
            onPagoFallido(`❌ ${backendMsg}`);
            toast.dismiss("pago-payu");
            setComprando(false);
        }
    };

    return (
        <>
            {/* Formulario Oculto para PayU */}
            {datosPayU && (
                <form ref={formRef} method="post" action={datosPayU.urlPayU}>
                    <input name="merchantId" type="hidden" value={datosPayU.merchantId} />
                    <input name="accountId" type="hidden" value={datosPayU.accountId} />
                    <input name="description" type="hidden" value={datosPayU.descripcion} />
                    <input name="referenceCode" type="hidden" value={datosPayU.referencia} />
                    <input name="amount" type="hidden" value={datosPayU.monto} />
                    <input name="tax" type="hidden" value="0" />
                    <input name="taxReturnBase" type="hidden" value="0" />
                    <input name="currency" type="hidden" value={datosPayU.moneda} />
                    <input name="signature" type="hidden" value={datosPayU.firma} />
                    <input name="test" type="hidden" value="1" /> {/* 1 para Sandbox */}
                    <input name="responseUrl" type="hidden" value="http://localhost:5173/pago/resultado" />
                    <input name="confirmationUrl" type="hidden" value="https://tu-backend.com/api/pagos/webhook" />
                </form>
            )}

            <button
                className={`w-full inline-flex items-center justify-center gap-2 text-white font-semibold rounded-lg text-base px-5 py-3 transition-all hover:shadow-lg disabled:opacity-50 disabled:cursor-not-allowed ${comprando ? "bg-gray-400 cursor-wait" : "bg-blue-600 hover:bg-blue-700"
                    }`}
                onClick={iniciarPago}
                disabled={disabled || comprando}
            >
                {comprando ? (
                    <>
                        <svg className="animate-spin h-5 w-5" viewBox="0 0 24 24">
                            <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none"></circle>
                            <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                        </svg>
                        Redirigiendo a PayU...
                    </>
                ) : (
                    <>
                        <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 10h18M7 15h.01M11 15h2m-6 4h10a2 2 0 002-2V7a2 2 0 00-2-2H7a2 2 0 00-2 2v10a2 2 0 002 2z" />
                        </svg>
                        Pagar con PayU
                    </>
                )}
            </button>
        </>
    );
};

BotonPago.propTypes = {
    disabled: PropTypes.bool.isRequired,
    comprando: PropTypes.bool.isRequired,
    setComprando: PropTypes.func.isRequired,
    obtenerDatosCompraParaPago: PropTypes.func.isRequired,
    solicitarDatosPago: PropTypes.func.isRequired,
    onPagoFallido: PropTypes.func.isRequired,
};
