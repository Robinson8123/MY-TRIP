class AppConfig {
  // 10.0.2.2 = localhost desde emulador Android
  // localhost  = desde Windows desktop / Chrome
  static const String baseUrl = String.fromEnvironment(
    'API_URL',
    defaultValue: 'https://my-trip-production.up.railway.app/api',
  );

  static String get clienteUrl => '$baseUrl/cliente';
  static String get planesUrl => '$baseUrl/planes';
  static String get carritoUrl => '$baseUrl/carritos';
  static String get chatUrl => '$baseUrl/chat';
  static String get imagenPlanUrl => '$planesUrl/{id}/imagen';
}
