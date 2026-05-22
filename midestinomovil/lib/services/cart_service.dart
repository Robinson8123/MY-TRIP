import 'dart:convert';
import 'package:http/http.dart' as http;
import '../config/app_config.dart';
import '../models/carrito_model.dart';

class CartService {
  Future<List<CarritoItem>> obtenerCarrito(int clienteId) async {
    final response = await http.get(
      Uri.parse('${AppConfig.carritoUrl}/cliente/$clienteId'),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body) as Map<String, dynamic>;
      final lista = data['carritosList'] as List<dynamic>? ?? [];
      return lista
          .map((e) => CarritoItem.fromJson(e as Map<String, dynamic>))
          .toList();
    }
    return [];
  }

  Future<void> agregarAlCarrito({
    required int clienteId,
    required int planId,
    required int cantidad,
    required double precioTotal,
  }) async {
    final body = {
      'cliente': {'idCliente': clienteId},
      'planEmpresa': {'id': planId},
      'cantidad': cantidad,
      'precioTotal': precioTotal,
      'fueAprobado': false,
      'eliminado': false,
    };

    final response = await http.post(
      Uri.parse('${AppConfig.carritoUrl}/guardar'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(body),
    );

    if (response.statusCode != 201) {
      final data = jsonDecode(response.body) as Map<String, dynamic>;
      throw Exception(data['message'] ?? 'Error al agregar al carrito');
    }
  }

  Future<void> eliminarDelCarrito(int carritoId) async {
    final response = await http.delete(
      Uri.parse('${AppConfig.carritoUrl}/eliminar/$carritoId'),
    );
    if (response.statusCode != 200) {
      throw Exception('Error al eliminar del carrito');
    }
  }
}
