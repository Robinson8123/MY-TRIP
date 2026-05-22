import 'package:flutter/material.dart';
import '../models/plan_model.dart';
import '../services/plan_service.dart';

class PlansProvider extends ChangeNotifier {
  final _service = PlanService();

  List<PlanEmpresa> _planes = [];
  List<PlanEmpresa> _filtrados = [];
  bool _cargando = false;
  String? _error;
  String _busqueda = '';

  List<PlanEmpresa> get planes => _filtrados;
  bool get cargando => _cargando;
  String? get error => _error;

  Future<void> cargarPlanes() async {
    _cargando = true;
    _error = null;
    notifyListeners();
    try {
      _planes = await _service.obtenerTodos();
      _aplicarFiltro();
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
    } finally {
      _cargando = false;
      notifyListeners();
    }
  }

  void buscar(String texto) {
    _busqueda = texto.toLowerCase();
    _aplicarFiltro();
    notifyListeners();
  }

  void _aplicarFiltro() {
    if (_busqueda.isEmpty) {
      _filtrados = List.from(_planes);
    } else {
      _filtrados = _planes.where((p) {
        return p.nombre.toLowerCase().contains(_busqueda) ||
            (p.ciudad?.toLowerCase().contains(_busqueda) ?? false) ||
            (p.tipoSitio?.toLowerCase().contains(_busqueda) ?? false);
      }).toList();
    }
  }

  String getImagenUrl(int planId) => _service.getImagenUrl(planId);
}
