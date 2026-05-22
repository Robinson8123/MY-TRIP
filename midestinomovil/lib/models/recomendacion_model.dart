class Recomendacion {
  final int planId;
  final String nombre;
  final String? tipoSitio;
  final String? ciudad;
  final double precio;
  final double? valoracionPromedio;
  final String? imagenUrl;
  final String? metodoRecomendacion;
  final double? scoreRelevancia;

  const Recomendacion({
    required this.planId,
    required this.nombre,
    this.tipoSitio,
    this.ciudad,
    required this.precio,
    this.valoracionPromedio,
    this.imagenUrl,
    this.metodoRecomendacion,
    this.scoreRelevancia,
  });

  factory Recomendacion.fromJson(Map<String, dynamic> json) => Recomendacion(
        planId: json['planId'] ?? 0,
        nombre: json['nombre'] ?? '',
        tipoSitio: json['tipoSitio'],
        ciudad: json['ciudad'],
        precio: (json['precio'] as num?)?.toDouble() ?? 0.0,
        valoracionPromedio: (json['valoracionPromedio'] as num?)?.toDouble(),
        imagenUrl: json['imagenUrl'],
        metodoRecomendacion: json['metodoRecomendacion'],
        scoreRelevancia: (json['scoreRelevancia'] as num?)?.toDouble(),
      );

  String get precioFormateado =>
      '\$${precio.toStringAsFixed(0).replaceAllMapped(RegExp(r'(\d{1,3})(?=(\d{3})+(?!\d))'), (m) => '${m[1]}.')}';
}
