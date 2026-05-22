import 'package:flutter/material.dart';
import '../models/carrito_model.dart';
import '../services/cart_service.dart';
import '../services/notification_service.dart';

class CartProvider extends ChangeNotifier {
  final _service = CartService();

  List<CarritoItem> _items = [];
  bool _cargando = false;
  String? _error;

  List<CarritoItem> get items => _items;
  bool get cargando => _cargando;
  String? get error => _error;
  int get cantidad => _items.length;

  double get total =>
      _items.fold(0.0, (sum, item) => sum + item.precioTotal);

  Future<void> cargarCarrito(int clienteId) async {
    _cargando = true;
    _error = null;
    notifyListeners();
    try {
      _items = await _service.obtenerCarrito(clienteId);
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
    } finally {
      _cargando = false;
      notifyListeners();
    }
  }

  Future<bool> agregar({
    required int clienteId,
    required int planId,
    required String nombrePlan,
    required double precio,
  }) async {
    try {
      await _service.agregarAlCarrito(
        clienteId: clienteId,
        planId: planId,
        cantidad: 1,
        precioTotal: precio,
      );
      await cargarCarrito(clienteId);
      await NotificationService.notificarPlanAgregado(nombrePlan);
      return true;
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
      notifyListeners();
      return false;
    }
  }

  Future<void> eliminar(int carritoId, int clienteId) async {
    try {
      await _service.eliminarDelCarrito(carritoId);
      await cargarCarrito(clienteId);
    } catch (e) {
      _error = e.toString().replaceFirst('Exception: ', '');
      notifyListeners();
    }
  }
}
