import 'package:flutter/material.dart';
import '../models/cliente_model.dart';
import '../services/auth_service.dart';

class AuthProvider extends ChangeNotifier {
  final _service = AuthService();

  Cliente? _cliente;
  bool _cargando = false;
  String? _error;

  Cliente? get cliente => _cliente;
  bool get cargando => _cargando;
  String? get error => _error;
  bool get estaAutenticado => _cliente != null;

  Future<void> cargarSesionGuardada() async {
    _cliente = await _service.obtenerSesionGuardada();
    notifyListeners();
  }

  Future<bool> iniciarSesion(String email, String contrasena) async {
    _cargando = true;
    _error = null;
    notifyListeners();
    try {
      _cliente = await _service.iniciarSesion(email, contrasena);
      return true;
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
      return false;
    } finally {
      _cargando = false;
      notifyListeners();
    }
  }

  Future<bool> registrar(Cliente nuevoCliente) async {
    _cargando = true;
    _error = null;
    notifyListeners();
    try {
      await _service.registrar(nuevoCliente);
      return true;
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
      return false;
    } finally {
      _cargando = false;
      notifyListeners();
    }
  }

  Future<void> cerrarSesion() async {
    await _service.cerrarSesion();
    _cliente = null;
    notifyListeners();
  }
}
