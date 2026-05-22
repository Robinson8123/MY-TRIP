import 'plan_model.dart';

class CarritoItem {
  final int id;
  final PlanEmpresa planEmpresa;
  final int cantidad;
  final double precioTotal;
  final bool fueAprobado;

  const CarritoItem({
    required this.id,
    required this.planEmpresa,
    required this.cantidad,
    required this.precioTotal,
    this.fueAprobado = false,
  });

  factory CarritoItem.fromJson(Map<String, dynamic> json) => CarritoItem(
        id: json['id'],
        planEmpresa: PlanEmpresa.fromJson(json['planEmpresa'] ?? {}),
        cantidad: json['cantidad'] ?? 1,
        precioTotal: (json['precioTotal'] as num?)?.toDouble() ?? 0.0,
        fueAprobado: json['fueAprobado'] ?? false,
      );
}
