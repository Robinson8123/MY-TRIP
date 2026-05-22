import 'package:flutter/material.dart';
import '../models/recomendacion_model.dart';
import '../services/recomendacion_service.dart';

class RecomendacionProvider extends ChangeNotifier {
  final _service = RecomendacionService();

  List<Recomendacion> _recomendaciones = [];
  bool _cargando = false;
  String? _error;
  int? _clienteCargado;

  List<Recomendacion> get recomendaciones => _recomendaciones;
  bool get cargando => _cargando;
  String? get error => _error;

  Future<void> cargar(int clienteId) async {
    if (_clienteCargado == clienteId && _recomendaciones.isNotEmpty) return;

    _cargando = true;
    _error = null;
    notifyListeners();

    try {
      _recomendaciones = await _service.obtenerRecomendaciones(clienteId);
      _clienteCargado = clienteId;
    } catch (_) {
      _error = 'No se pudieron cargar recomendaciones';
      _recomendaciones = [];
    } finally {
      _cargando = false;
      notifyListeners();
    }
  }

  void limpiar() {
    _recomendaciones = [];
    _clienteCargado = null;
    _error = null;
    notifyListeners();
  }
}
