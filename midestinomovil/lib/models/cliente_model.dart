class Cliente {
  final int? idCliente;
  final String? tipoUsuario;
  final String? fotoPerfil;
  final String nombreCompleto;
  final String? tipoDocumento;
  final String? numeroDocumento;
  final String? numeroTelefono;
  final String email;
  final String nombreUsuario;
  final String? contrasena;
  final double? presupuesto;

  const Cliente({
    this.idCliente,
    this.tipoUsuario,
    this.fotoPerfil,
    required this.nombreCompleto,
    this.tipoDocumento,
    this.numeroDocumento,
    this.numeroTelefono,
    required this.email,
    required this.nombreUsuario,
    this.contrasena,
    this.presupuesto,
  });

  factory Cliente.fromJson(Map<String, dynamic> json) => Cliente(
        idCliente: json['idCliente'],
        tipoUsuario: json['tipoUsuario'],
        fotoPerfil: json['fotoPerfil'],
        nombreCompleto: json['nombreCompleto'] ?? '',
        tipoDocumento: json['tipoDocumento'],
        numeroDocumento: json['numeroDocumento'],
        numeroTelefono: json['numeroTelefono'],
        email: json['email'] ?? '',
        nombreUsuario: json['nombreUsuario'] ?? '',
        contrasena: json['contrasena'],
        presupuesto: (json['presupuesto'] as num?)?.toDouble(),
      );

  Map<String, dynamic> toJson() => {
        if (idCliente != null) 'idCliente': idCliente,
        if (tipoUsuario != null) 'tipoUsuario': tipoUsuario,
        'nombreCompleto': nombreCompleto,
        if (tipoDocumento != null) 'tipoDocumento': tipoDocumento,
        if (numeroDocumento != null) 'numeroDocumento': numeroDocumento,
        if (numeroTelefono != null) 'numeroTelefono': numeroTelefono,
        'email': email,
        'nombreUsuario': nombreUsuario,
        if (contrasena != null) 'contrasena': contrasena,
        if (presupuesto != null) 'presupuesto': presupuesto,
      };
}
