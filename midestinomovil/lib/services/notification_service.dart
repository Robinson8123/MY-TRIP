import 'package:flutter/foundation.dart';
import 'package:flutter_local_notifications/flutter_local_notifications.dart';

class NotificationService {
  static final _plugin = FlutterLocalNotificationsPlugin();
  static const _channelId = 'mi_destino_channel';
  static const _channelName = 'Mi Destino';

  // Solo Android y iOS soportan flutter_local_notifications
  static bool get _soportado =>
      !kIsWeb && (defaultTargetPlatform == TargetPlatform.android ||
          defaultTargetPlatform == TargetPlatform.iOS);

  static Future<void> initialize() async {
    if (!_soportado) return;

    const android = AndroidInitializationSettings('@mipmap/ic_launcher');
    const settings = InitializationSettings(android: android);
    await _plugin.initialize(settings);

    const channel = AndroidNotificationChannel(
      _channelId,
      _channelName,
      description: 'Notificaciones de planes y viajes',
      importance: Importance.high,
    );
    await _plugin
        .resolvePlatformSpecificImplementation<
            AndroidFlutterLocalNotificationsPlugin>()
        ?.createNotificationChannel(channel);
  }

  static Future<void> mostrarNotificacion({
    required String titulo,
    required String cuerpo,
    int id = 0,
  }) async {
    if (!_soportado) return;

    const androidDetails = AndroidNotificationDetails(
      _channelId,
      _channelName,
      channelDescription: 'Notificaciones de planes y viajes',
      importance: Importance.high,
      priority: Priority.high,
      icon: '@mipmap/ic_launcher',
    );
    const details = NotificationDetails(android: androidDetails);
    await _plugin.show(id, titulo, cuerpo, details);
  }

  static Future<void> notificarPlanAgregado(String nombrePlan) =>
      mostrarNotificacion(
        id: 1,
        titulo: '¡Plan agregado al carrito!',
        cuerpo: '$nombrePlan fue añadido a tu carrito de viaje.',
      );

  static Future<void> notificarItinerarioListo() => mostrarNotificacion(
        id: 2,
        titulo: '¡Tu itinerario está listo!',
        cuerpo: 'El asistente de IA generó recomendaciones para tu viaje.',
      );
}
