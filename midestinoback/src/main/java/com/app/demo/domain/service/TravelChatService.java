package com.app.demo.domain.service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.app.demo.domain.dto.ChatMessageDTO;
import com.app.demo.domain.dto.ChatRequestDTO;
import com.app.demo.domain.dto.SuggestedPlanDTO;
import com.app.demo.domain.response.ChatResponse;
import com.app.demo.integration.google.groq.GroqClient;
import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.entity.ValoracionPlan;
import com.app.demo.persistence.repository.ClienteRepository;
import com.app.demo.persistence.repository.PlanEmpresaRepository;
import com.app.demo.persistence.repository.ValoracionPlanRepository;

@Service
public class TravelChatService {

    private static final Locale CO_LOCALE = Locale.forLanguageTag("es-CO");
    private static final String CIUDAD_POR_DEFECTO = "Cartagena";

    private final PlanEmpresaRepository planEmpresaRepository;
    private final ValoracionPlanRepository valoracionPlanRepository;
    private final ClienteRepository clienteRepository;
    private final GroqClient groqClient;

    public TravelChatService(
            PlanEmpresaRepository planEmpresaRepository,
            ValoracionPlanRepository valoracionPlanRepository,
            ClienteRepository clienteRepository,
            GroqClient groqClient) {
        this.planEmpresaRepository = planEmpresaRepository;
        this.valoracionPlanRepository = valoracionPlanRepository;
        this.clienteRepository = clienteRepository;
        this.groqClient = groqClient;
    }

    public ChatResponse generarRespuesta(ChatRequestDTO request) {
        try {
            // Detectar si la pregunta es sobre planes turísticos o es una pregunta general
            boolean esPreguntaSobrePlanes = esPreguntaRelacionadaConPlanes(request.getMensajeUsuario());

            // Si es una pregunta general (clima, noticias, cultura, etc.)
            if (!esPreguntaSobrePlanes) {
                return generarRespuestaGeneral(request);
            }

            // Si es sobre planes, mantener la lógica actual
            BigDecimal presupuesto = obtenerPresupuesto(request);
            String ciudadDestino = normalizarCiudad(request.getCiudadDestino());

            List<PlanEmpresa> planesFiltrados = filtrarPlanes(presupuesto,
                    request.getNumeroPersonas(),
                    request.getIntereses(),
                    ciudadDestino);

            if (planesFiltrados.isEmpty()) {
                String destinoLabel = StringUtils.hasText(ciudadDestino) ? ciudadDestino : "las ciudades disponibles";
                return ChatResponse.builder()
                        .message("No se encontraron planes disponibles en " + destinoLabel
                                + " con los filtros proporcionados")
                        .valid(false)
                        .respuesta(
                                "Actualmente no hay planes disponibles que coincidan con tu búsqueda en " + destinoLabel
                                        + ". Intenta ampliar el presupuesto, cambiar la duración o los intereses.")
                        .planesSugeridos(List.of())
                        .build();
            }

            List<SuggestedPlanDTO> planesSugeridos = planesFiltrados.stream()
                    .limit(6)
                    .map(this::mapToSuggestedPlan)
                    .toList();

            String prompt = construirPrompt(
                    request,
                    presupuesto,
                    planesSugeridos,
                    ciudadDestino,
                    request.getHistorialConversacion());

            String respuestaModelo = groqClient.generateResponse(prompt);

            return ChatResponse.builder()
                    .message("Respuesta generada exitosamente")
                    .valid(true)
                    .respuesta(respuestaModelo)
                    .planesSugeridos(planesSugeridos)
                    .build();
        } catch (Exception ex) {
            return ChatResponse.builder()
                    .message("Error al generar respuesta: " + ex.getMessage())
                    .valid(false)
                    .respuesta("No pude generar el itinerario en este momento. Intenta nuevamente más tarde.")
                    .planesSugeridos(List.of())
                    .build();
        }
    }

    /**
     * Detecta si la pregunta del usuario está relacionada con planes turísticos
     */
    private boolean esPreguntaRelacionadaConPlanes(String mensaje) {
        if (!StringUtils.hasText(mensaje)) {
            return false;
        }

        String mensajeLower = mensaje.toLowerCase();

        // Primero, verificar si es una pregunta general (NO sobre planes)
        String[] palabrasClaveGenerales = {
                "clima", "tiempo", "temperatura", "lluvia", "sol", "nublado",
                "noticia", "noticias", "actualidad", "evento", "eventos",
                "historia", "histórico", "cultura", "cultural",
                "población", "habitantes", "economía", "política",
                "cómo está", "como está", "qué tal", "que tal",
                "cuéntame sobre", "cuentame sobre", "háblame de", "hablame de",
                "información sobre", "informacion sobre", "dime sobre"
        };

        // Si contiene palabras de preguntas generales, NO es sobre planes
        for (String palabra : palabrasClaveGenerales) {
            if (mensajeLower.contains(palabra)) {
                return false;
            }
        }

        // Palabras clave FUERTES que indican pregunta sobre planes turísticos
        String[] palabrasClaveDirectasPlanes = {
                "plan", "planes", "viaje", "viajar", "turismo", "turístico", "turistico",
                "visitar", "conocer", "recorrer", "itinerario",
                "hotel", "hospedaje", "alojamiento", "restaurante", "comer",
                "playa", "actividad", "actividades", "tour", "excursión", "excursion",
                "presupuesto", "precio", "costo", "cuanto cuesta", "cuánto cuesta",
                "reservar", "reserva", "disponible", "disponibilidad",
                "destino", "destinos", "vacaciones", "paseo", "salir", "ir a",
                "qué hacer", "que hacer", "dónde ir", "donde ir", "recomendaciones"
        };

        // Verificar palabras clave directas de planes
        for (String palabra : palabrasClaveDirectasPlanes) {
            if (mensajeLower.contains(palabra)) {
                return true;
            }
        }

        // Solo mencionar una ciudad NO es suficiente para ser pregunta sobre planes
        return false;
    }

    /**
     * Genera una respuesta para preguntas generales (clima, noticias, cultura,
     * etc.)
     */
    private ChatResponse generarRespuestaGeneral(ChatRequestDTO request) {
        try {
            String prompt = construirPromptGeneral(request, List.of());
            String respuestaModelo = groqClient.generateResponse(prompt);

            return ChatResponse.builder()
                    .message("Respuesta generada exitosamente")
                    .valid(true)
                    .respuesta(respuestaModelo)
                    .planesSugeridos(List.of())
                    .build();
        } catch (Exception ex) {
            return ChatResponse.builder()
                    .message("Error al generar respuesta: " + ex.getMessage())
                    .valid(false)
                    .respuesta("No pude generar una respuesta en este momento. Intenta nuevamente más tarde.")
                    .planesSugeridos(List.of())
                    .build();
        }
    }

    /**
     * Construye el prompt para preguntas generales
     */
    private String construirPromptGeneral(ChatRequestDTO request, List<SuggestedPlanDTO> planesDestacados) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(
                "Eres un asistente virtual amigable y conversacional. Mantienes el contexto de toda la conversación y respondes de forma coherente.\n\n");

        // Mostrar el historial PRIMERO para que el modelo tenga contexto
        if (!CollectionUtils.isEmpty(request.getHistorialConversacion())) {
            prompt.append("=== CONTEXTO DE LA CONVERSACIÓN ===\n");
            prompt.append("Lee atentamente los mensajes anteriores para entender de qué están hablando:\n\n");
            request.getHistorialConversacion().stream()
                    .limit(10)
                    .forEach(mensaje -> prompt.append(formatearRolConversacion(mensaje.getRol()))
                            .append(": ")
                            .append(defaultString(mensaje.getMensaje()))
                            .append('\n'));
            prompt.append('\n');
        }

        prompt.append("=== PREGUNTA ACTUAL ===\n");
        prompt.append(defaultString(request.getMensajeUsuario())).append("\n\n");

        prompt.append("=== CÓMO RESPONDER ===\n");
        prompt.append(
                "REGLA CRÍTICA: Antes de responder, identifica el TEMA o CIUDAD mencionada en mensajes anteriores.\n\n");

        prompt.append("Ejemplos de cómo usar el contexto:\n");
        prompt.append(
                "- Si el usuario preguntó antes sobre 'clima en Cartagena' y ahora pregunta '¿y en Bogotá?', debes entender que sigue preguntando sobre el CLIMA pero ahora en Bogotá.\n");
        prompt.append(
                "- Si mencionaron 'Cartagena' antes y ahora dicen 'el clima', debes dar el clima de Cartagena.\n");
        prompt.append(
                "- Si hablaron de 'noticias' y luego mencionan una ciudad, debes dar noticias de esa ciudad.\n\n");

        prompt.append("Pasos a seguir:\n");
        prompt.append("1. Revisa el historial: ¿De qué ciudad o tema estaban hablando?\n");
        prompt.append("2. Analiza la pregunta actual: ¿Hace referencia implícita a algo del historial?\n");
        prompt.append(
                "3. Si la pregunta es incompleta (como 'y en Bogotá?' o 'el clima'), INFIERE el contexto del historial.\n");
        prompt.append("4. Responde de forma directa usando toda la información disponible.\n");
        prompt.append("5. NO pidas información que ya se mencionó en la conversación.\n");
        prompt.append("6. Mantén un tono amigable y natural.\n");
        prompt.append("7. NO menciones planes turísticos a menos que sea relevante.\n\n");

        prompt.append("Ahora responde la pregunta del usuario usando TODO el contexto disponible:");

        return prompt.toString();
    }

    private BigDecimal obtenerPresupuesto(ChatRequestDTO request) {
        if (request.getPresupuestoMaximo() != null) {
            return request.getPresupuestoMaximo();
        }

        if (request.getClienteId() == null) {
            return null;
        }

        return clienteRepository.findById(request.getClienteId())
                .map(Cliente::getPresupuesto)
                .orElse(null);
    }

    private String normalizarCiudad(String ciudad) {
        return StringUtils.hasText(ciudad) ? ciudad.trim() : null;
    }

    private List<PlanEmpresa> filtrarPlanes(BigDecimal presupuesto, Integer personas, List<String> intereses,
            String ciudad) {
        List<PlanEmpresa> planes = obtenerPlanesBase(ciudad);

        return planes.stream()
                .filter(plan -> presupuesto == null
                        || plan.getPrecio() == null
                        || plan.getPrecio().compareTo(presupuesto) <= 0)
                .filter(plan -> personas == null
                        || plan.getPersonasDisponibles() == null
                        || plan.getPersonasDisponibles() >= personas)
                .filter(plan -> coincideConIntereses(plan, intereses))
                .sorted(Comparator.comparing(PlanEmpresa::getPrecio, Comparator.nullsLast(Comparator.naturalOrder())))
                .collect(Collectors.toList());
    }

    private List<PlanEmpresa> obtenerPlanesBase(String ciudad) {
        List<PlanEmpresa> planes = new ArrayList<>();

        if (StringUtils.hasText(ciudad)) {
            planes.addAll(planEmpresaRepository.findByDisponibleTrueAndEmpresa_CiudadIgnoreCase(ciudad));
            planes.addAll(planEmpresaRepository.findByDisponibleTrueAndEmpresa_CiudadContainingIgnoreCase(ciudad));
            planes.addAll(planEmpresaRepository.findByDisponibleTrueAndDireccionContainingIgnoreCase(ciudad));
        } else {
            planes.addAll(planEmpresaRepository.findAll().stream()
                    .filter(PlanEmpresa::isDisponible)
                    .toList());
        }

        if (planes.isEmpty()) {
            planes.addAll(planEmpresaRepository.findByDisponibleTrueAndCiudadIgnoreCase(CIUDAD_POR_DEFECTO));
        }

        Set<Long> ids = new HashSet<>();
        List<PlanEmpresa> deduplicados = new ArrayList<>();
        for (PlanEmpresa plan : planes) {
            if (plan != null && plan.getId() != null && plan.isDisponible() && ids.add(plan.getId())) {
                deduplicados.add(plan);
            }
        }

        return deduplicados;
    }

    private boolean coincideConIntereses(PlanEmpresa plan, List<String> intereses) {
        if (CollectionUtils.isEmpty(intereses)) {
            return true;
        }

        String tipoSitio = StringUtils.hasText(plan.getTipoSitio()) ? plan.getTipoSitio().toLowerCase() : "";
        String informacion = StringUtils.hasText(plan.getInformacionGeneral())
                ? plan.getInformacionGeneral().toLowerCase()
                : "";

        return intereses.stream()
                .filter(StringUtils::hasText)
                .map(String::toLowerCase)
                .anyMatch(interes -> tipoSitio.contains(interes) || informacion.contains(interes));
    }

    private SuggestedPlanDTO mapToSuggestedPlan(PlanEmpresa plan) {
        List<String> comentarios = valoracionPlanRepository.findByPlanEmpresaId(plan.getId())
                .stream()
                .map(ValoracionPlan::getComentario)
                .filter(StringUtils::hasText)
                .limit(3)
                .toList();

        return SuggestedPlanDTO.builder()
                .id(plan.getId())
                .nombrePlan(plan.getNombre())
                .tipoSitio(plan.getTipoSitio())
                .direccion(plan.getDireccion())
                .horario(plan.getHorario())
                .precio(plan.getPrecio())
                .valoracionPromedio(plan.getValoracionPromedio())
                .empresaNombre(plan.getEmpresa() != null ? plan.getEmpresa().getNombre() : null)
                .telefonoContacto(plan.getTelefono())
                .emailContacto(plan.getEmail())
                .metodosPagoAceptados(plan.getMetodosPagoAceptados())
                .informacionGeneral(plan.getInformacionGeneral())
                .comentariosDestacados(comentarios)
                .ciudad(plan.getCiudad())
                .ciudadEmpresa(plan.getEmpresa() != null ? plan.getEmpresa().getCiudad() : null)
                .build();
    }

    private String construirPrompt(ChatRequestDTO request, BigDecimal presupuesto, List<SuggestedPlanDTO> planes,
            String ciudadDestino, List<ChatMessageDTO> historialConversacion) {
        StringBuilder prompt = new StringBuilder();

        prompt.append(
                "Eres un asistente turístico experto en destinos turísticos de Colombia (Cartagena y otras ciudades). "
                        + "Debes usar exclusivamente la información real proporcionada en la sección 'PLANES DISPONIBLES'. "
                        + "Tu objetivo es crear itinerarios personalizados, sugerir actividades, playas, restaurantes cercanos y resolver dudas logísticas (transporte, horarios, precios) usando esos datos.\n\n");

        prompt.append("DATOS DEL USUARIO:\n");
        prompt.append("- Mensaje: ").append(defaultString(request.getMensajeUsuario())).append('\n');
        String ciudadTexto = StringUtils.hasText(ciudadDestino) ? ciudadDestino : "No especificado";
        prompt.append("- Ciudad destino: ").append(ciudadTexto).append('\n');
        if (presupuesto != null) {
            prompt.append("- Presupuesto máximo estimado: ").append(formatearCop(presupuesto)).append('\n');
        }
        if (request.getDuracionDias() != null) {
            prompt.append("- Duración del viaje (días): ").append(request.getDuracionDias()).append('\n');
        }
        if (request.getNumeroPersonas() != null) {
            prompt.append("- Número de personas: ").append(request.getNumeroPersonas()).append('\n');
        }
        if (!CollectionUtils.isEmpty(request.getIntereses())) {
            prompt.append("- Intereses declarados: ").append(String.join(", ", request.getIntereses())).append('\n');
        }

        if (!CollectionUtils.isEmpty(historialConversacion)) {
            prompt.append('\n').append("HISTORIAL RECIENTE DEL CHAT:\n");
            historialConversacion.stream()
                    .limit(8)
                    .forEach(mensaje -> prompt.append("-")
                            .append(" ")
                            .append(formatearRolConversacion(mensaje.getRol()))
                            .append(": ")
                            .append(defaultString(mensaje.getMensaje()))
                            .append('\n'));
        }

        prompt.append('\n').append("PLANES DISPONIBLES (información real de la base de datos):\n");
        for (int i = 0; i < planes.size(); i++) {
            SuggestedPlanDTO plan = planes.get(i);
            prompt.append(i + 1).append(". Nombre: ").append(defaultString(plan.getNombrePlan())).append('\n');
            prompt.append("   - Tipo: ").append(defaultString(plan.getTipoSitio())).append('\n');
            prompt.append("   - Dirección: ").append(defaultString(plan.getDireccion())).append('\n');
            prompt.append("   - Ciudad del plan: ")
                    .append(defaultString(plan.getCiudad())).append('\n');
            if (StringUtils.hasText(plan.getCiudadEmpresa())) {
                prompt.append("   - Ciudad de la empresa: ")
                        .append(defaultString(plan.getCiudadEmpresa())).append('\n');
            }
            prompt.append("   - Horario: ").append(defaultString(plan.getHorario())).append('\n');
            prompt.append("   - Precio por persona o plan: ")
                    .append(plan.getPrecio() != null ? formatearCop(plan.getPrecio()) : "No reportado").append('\n');
            if (plan.getValoracionPromedio() != null) {
                prompt.append("   - Valoración promedio: ")
                        .append(String.format(Locale.US, "%.1f", plan.getValoracionPromedio()))
                        .append(" / 5\n");
            }
            prompt.append("   - Empresa responsable: ").append(defaultString(plan.getEmpresaNombre())).append('\n');
            prompt.append("   - Contacto: ").append(defaultString(plan.getTelefonoContacto())).append(" | ")
                    .append(defaultString(plan.getEmailContacto())).append('\n');
            prompt.append("   - Métodos de pago: ").append(defaultString(plan.getMetodosPagoAceptados())).append('\n');
            prompt.append("   - Información adicional: ").append(defaultString(plan.getInformacionGeneral()))
                    .append('\n');

            if (!CollectionUtils.isEmpty(plan.getComentariosDestacados())) {
                prompt.append("   - Comentarios de clientes:\n");
                plan.getComentariosDestacados().forEach(comentario -> prompt.append("       * ")
                        .append(comentario)
                        .append('\n'));
            }
            prompt.append('\n');
        }

        prompt.append("INSTRUCCIONES DE RESPUESTA:\n" +
                "1. Responde siempre en español neutro y con tono cercano.\n" +
                "2. Empieza con un resumen del viaje propuesto en uno o dos párrafos.\n" +
                "3. Crea un itinerario día por día acorde a la duración indicada (si no se indica, propone 3 días) incluyendo horarios aproximados y justificando por qué cada actividad encaja con los gustos o intereses.\n"
                +
                "4. Incluye sugerencias complementarias: restaurantes o playas cercanas, opciones de transporte local, consejos de horarios, costos aproximados y requisitos (reservas, documentos, etc.). Usa siempre los datos de los planes para estimar costos.\n"
                +
                "5. Si los planes pertenecen a ciudades diferentes, aclara explícitamente la ciudad a la que corresponde cada actividad.\n"
                +
                "6. Si el usuario hace preguntas específicas, respóndelas directamente usando los datos anteriores.\n" +
                "7. Cierra con próximos pasos claros (por ejemplo, reservar un plan concreto, confirmar medios de pago, etc.).\n"
                +
                "8. Si la información disponible no es suficiente para responder algún punto, acláralo y propone alternativas realistas usando únicamente la información disponible.\n"
                +
                "9. No inventes nombres ni datos que no estén listados en 'PLANES DISPONIBLES'.\n");

        prompt.append('\n').append(
                "Genera ahora la respuesta para el usuario teniendo en cuenta su mensaje y los datos reales proporcionados.");

        return prompt.toString();
    }

    private String defaultString(String value) {
        return StringUtils.hasText(value) ? value : "No disponible";
    }

    private String formatearRolConversacion(String rol) {
        if (!StringUtils.hasText(rol)) {
            return "Desconocido";
        }

        return switch (rol.toLowerCase()) {
            case "user", "usuario", "cliente" -> "Usuario";
            case "assistant", "bot", "asistente" -> "Asistente";
            default -> Character.toUpperCase(rol.charAt(0)) + rol.substring(1).toLowerCase();
        };
    }

    private String formatearCop(BigDecimal valor) {
        if (valor == null) {
            return "No disponible";
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(CO_LOCALE);
        format.setMaximumFractionDigits(0);
        return format.format(valor);
    }
}
