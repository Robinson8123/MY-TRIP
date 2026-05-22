class ChatMessage {
  final String texto;
  final bool esUsuario;
  final DateTime timestamp;

  ChatMessage({
    required this.texto,
    required this.esUsuario,
    DateTime? timestamp,
  }) : timestamp = timestamp ?? DateTime.now();
}

class ChatRequest {
  final String mensajeUsuario;
  final double? presupuestoMaximo;
  final int? duracionDias;
  final List<String> intereses;
  final int? numeroPersonas;
  final int? clienteId;
  final String? ciudadDestino;
  final List<Map<String, String>> historialConversacion;

  const ChatRequest({
    required this.mensajeUsuario,
    this.presupuestoMaximo,
    this.duracionDias,
    this.intereses = const [],
    this.numeroPersonas,
    this.clienteId,
    this.ciudadDestino,
    this.historialConversacion = const [],
  });

  Map<String, dynamic> toJson() => {
        'mensajeUsuario': mensajeUsuario,
        if (presupuestoMaximo != null) 'presupuestoMaximo': presupuestoMaximo,
        if (duracionDias != null) 'duracionDias': duracionDias,
        if (intereses.isNotEmpty) 'intereses': intereses,
        if (numeroPersonas != null) 'numeroPersonas': numeroPersonas,
        if (clienteId != null) 'clienteId': clienteId,
        if (ciudadDestino != null && ciudadDestino!.isNotEmpty)
          'ciudadDestino': ciudadDestino,
        if (historialConversacion.isNotEmpty)
          'historialConversacion': historialConversacion,
      };
}
