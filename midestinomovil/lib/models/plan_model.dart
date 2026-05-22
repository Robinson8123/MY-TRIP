class PlanEmpresa {
  final int id;
  final String nombre;
  final String? tipoSitio;
  final String? direccion;
  final String? horario;
  final String? email;
  final String? ciudad;
  final String? telefono;
  final double precio;
  final int? cantidadDisponible;
  final int? personasDisponibles;
  final String? informacionGeneral;
  final double? valoracionPromedio;
  final String? metodosPagoAceptados;
  final String? fechaRegistro;
  final bool disponible;
  final int? empresaId;
  final double? lat;
  final double? lng;

  const PlanEmpresa({
    required this.id,
    required this.nombre,
    this.tipoSitio,
    this.direccion,
    this.horario,
    this.email,
    this.ciudad,
    this.telefono,
    required this.precio,
    this.cantidadDisponible,
    this.personasDisponibles,
    this.informacionGeneral,
    this.valoracionPromedio,
    this.metodosPagoAceptados,
    this.fechaRegistro,
    this.disponible = true,
    this.empresaId,
    this.lat,
    this.lng,
  });

  factory PlanEmpresa.fromJson(Map<String, dynamic> json) => PlanEmpresa(
        id: json['id'],
        nombre: json['nombre'] ?? '',
        tipoSitio: json['tipoSitio'],
        direccion: json['direccion'],
        horario: json['horario'],
        email: json['email'],
        ciudad: json['ciudad'],
        telefono: json['telefono'],
        precio: (json['precio'] as num?)?.toDouble() ?? 0.0,
        cantidadDisponible: json['cantidadDisponible'],
        personasDisponibles: json['personasDisponibles'],
        informacionGeneral: json['informacionGeneral'],
        valoracionPromedio: (json['valoracionPromedio'] as num?)?.toDouble(),
        metodosPagoAceptados: json['metodosPagoAceptados'],
        fechaRegistro: json['fechaRegistro'],
        disponible: json['disponible'] ?? true,
        empresaId: json['empresaId'],
        lat: (json['lat'] as num?)?.toDouble(),
        lng: (json['lng'] as num?)?.toDouble(),
      );

  String get precioFormateado =>
      '\$${precio.toStringAsFixed(0).replaceAllMapped(RegExp(r'(\d{1,3})(?=(\d{3})+(?!\d))'), (m) => '${m[1]}.')}';
}

class ValoracionPlan {
  final int? id;
  final int planId;
  final int clienteId;
  final int puntuacion;
  final String? comentario;
  final String? nombreCliente;

  const ValoracionPlan({
    this.id,
    required this.planId,
    required this.clienteId,
    required this.puntuacion,
    this.comentario,
    this.nombreCliente,
  });

  factory ValoracionPlan.fromJson(Map<String, dynamic> json) => ValoracionPlan(
        id: json['id'],
        planId: json['planId'] ?? 0,
        clienteId: json['clienteId'] ?? 0,
        puntuacion: json['puntuacion'] ?? 0,
        comentario: json['comentario'],
        nombreCliente: json['nombreCliente'],
      );
}
