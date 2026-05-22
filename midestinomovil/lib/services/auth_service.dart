import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../config/app_config.dart';
import '../models/cliente_model.dart';

class AuthService {
  static const _keyClienteId = 'cliente_id';
  static const _keyClienteJson = 'cliente_json';

  Future<Cliente> iniciarSesion(String email, String contrasena) async {
    final response = await http.post(
      Uri.parse('${AppConfig.clienteUrl}/iniciar-sesion'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'email': email, 'contrasena': contrasena}),
    );

    final data = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 200 && data['valid'] == true) {
      final cliente = Cliente.fromJson(data['cliente'] as Map<String, dynamic>);
      await _guardarSesion(cliente);
      return cliente;
    }
    throw Exception(data['message'] ?? 'Credenciales incorrectas');
  }

  Future<Cliente> registrar(Cliente cliente) async {
    final response = await http.post(
      Uri.parse('${AppConfig.clienteUrl}/agregar'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode(cliente.toJson()),
    );

    final data = jsonDecode(response.body) as Map<String, dynamic>;

    if (response.statusCode == 201 && data['valid'] == true) {
      return Cliente.fromJson(data['cliente'] as Map<String, dynamic>);
    }
    throw Exception(data['message'] ?? 'Error al registrar usuario');
  }

  Future<void> _guardarSesion(Cliente cliente) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setInt(_keyClienteId, cliente.idCliente ?? 0);
    await prefs.setString(_keyClienteJson, jsonEncode(cliente.toJson()));
  }

  Future<Cliente?> obtenerSesionGuardada() async {
    final prefs = await SharedPreferences.getInstance();
    final json = prefs.getString(_keyClienteJson);
    if (json == null) return null;
    return Cliente.fromJson(jsonDecode(json) as Map<String, dynamic>);
  }

  Future<void> cerrarSesion() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove(_keyClienteId);
    await prefs.remove(_keyClienteJson);
  }
}
