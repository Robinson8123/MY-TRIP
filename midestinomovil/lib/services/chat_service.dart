import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/app_config.dart';
import '../models/chat_model.dart';

class ChatService {
  Future<String> enviarMensaje(ChatRequest request) async {
    final response = await http.post(
      Uri.parse('${AppConfig.chatUrl}/itinerario'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(request.toJson()),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as Map<String, dynamic>;
      return data['respuesta'] as String? ??
          data['message'] as String? ??
          'Sin respuesta del asistente';
    }
    throw Exception('Error al contactar el asistente de IA');
  }
}
