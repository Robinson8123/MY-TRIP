import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/app_config.dart';
import '../models/recomendacion_model.dart';

class RecomendacionService {
  Future<List<Recomendacion>> obtenerRecomendaciones(int clienteId) async {
    final response = await http.get(
      Uri.parse('${AppConfig.baseUrl}/recomendaciones/$clienteId'),
    );

    if (response.statusCode == 200) {
      final lista = jsonDecode(response.body) as List<dynamic>;
      return lista
          .map((e) => Recomendacion.fromJson(e as Map<String, dynamic>))
          .toList();
    }
    throw Exception('Error al cargar recomendaciones');
  }
}
