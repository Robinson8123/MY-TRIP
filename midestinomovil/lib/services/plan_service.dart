import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/app_config.dart';
import '../models/plan_model.dart';

class PlanService {
  Future<List<PlanEmpresa>> obtenerTodos() async {
    final response = await http.get(Uri.parse('${AppConfig.planesUrl}/todos'));

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as Map<String, dynamic>;
      final lista = data['planes'] as List<dynamic>? ?? [];
      return lista
          .map((e) => PlanEmpresa.fromJson(e as Map<String, dynamic>))
          .where((p) => p.disponible)
          .toList();
    }
    throw Exception('Error al cargar planes');
  }

  Future<List<ValoracionPlan>> obtenerResenas(int planId) async {
    final response = await http.get(
      Uri.parse('${AppConfig.planesUrl}/$planId/resenas'),
    );

    if (response.statusCode == 200) {
      final lista = jsonDecode(response.body) as List<dynamic>;
      return lista
          .map((e) => ValoracionPlan.fromJson(e as Map<String, dynamic>))
          .toList();
    }
    return [];
  }

  Future<void> agregarValoracion({
    required int planId,
    required int clienteId,
    required int puntuacion,
    String? comentario,
  }) async {
    final uri = Uri.parse(
      '${AppConfig.planesUrl}/$planId/valoracion'
      '?clienteId=$clienteId&puntuacion=$puntuacion'
      '${comentario != null ? '&comentario=${Uri.encodeComponent(comentario)}' : ''}',
    );
    final response = await http.post(uri);
    if (response.statusCode != 200) {
      throw Exception('Error al agregar valoración');
    }
  }

  String getImagenUrl(int planId) =>
      '${AppConfig.planesUrl}/$planId/imagen';
}
