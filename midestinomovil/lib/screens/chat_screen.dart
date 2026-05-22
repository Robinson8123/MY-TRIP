import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../config/app_theme.dart';
import '../models/chat_model.dart';
import '../providers/auth_provider.dart';
import '../services/chat_service.dart';
import '../services/notification_service.dart';

class ChatScreen extends StatefulWidget {
  const ChatScreen({super.key});

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final _chatService = ChatService();
  final _mensajeCtrl = TextEditingController();
  final _ciudadCtrl = TextEditingController();
  final _presupuestoCtrl = TextEditingController();
  final _diasCtrl = TextEditingController();
  final _personasCtrl = TextEditingController();
  final _scrollCtrl = ScrollController();

  final List<ChatMessage> _mensajes = [];
  final List<Map<String, String>> _historial = [];
  bool _enviando = false;
  bool _mostrarOpciones = false;

  @override
  void initState() {
    super.initState();
    _mensajes.add(ChatMessage(
      texto: '¡Hola! Soy tu asistente de viajes con IA. 🌍\n'
          'Cuéntame: ¿a dónde quieres viajar? Puedes darme detalles como '
          'ciudad, presupuesto, días y número de personas.',
      esUsuario: false,
    ));
  }

  @override
  void dispose() {
    _mensajeCtrl.dispose();
    _ciudadCtrl.dispose();
    _presupuestoCtrl.dispose();
    _diasCtrl.dispose();
    _personasCtrl.dispose();
    _scrollCtrl.dispose();
    super.dispose();
  }

  Future<void> _enviar() async {
    final texto = _mensajeCtrl.text.trim();
    if (texto.isEmpty || _enviando) return;

    final auth = context.read<AuthProvider>();

    setState(() {
      _mensajes.add(ChatMessage(texto: texto, esUsuario: true));
      _enviando = true;
      _mensajeCtrl.clear();
    });
    _scrollAlFinal();

    try {
      final request = ChatRequest(
        mensajeUsuario: texto,
        clienteId: auth.cliente?.idCliente,
        ciudadDestino: _ciudadCtrl.text.trim().isEmpty
            ? null
            : _ciudadCtrl.text.trim(),
        presupuestoMaximo: double.tryParse(_presupuestoCtrl.text),
        duracionDias: int.tryParse(_diasCtrl.text),
        numeroPersonas: int.tryParse(_personasCtrl.text),
        historialConversacion: List.from(_historial),
      );

      final respuesta = await _chatService.enviarMensaje(request);

      _historial.add({'role': 'user', 'content': texto});
      _historial.add({'role': 'assistant', 'content': respuesta});

      setState(() {
        _mensajes.add(ChatMessage(texto: respuesta, esUsuario: false));
      });

      await NotificationService.notificarItinerarioListo();
    } catch (e) {
      setState(() {
        _mensajes.add(ChatMessage(
          texto: 'Error al contactar el asistente. Verifica tu conexión.',
          esUsuario: false,
        ));
      });
    } finally {
      setState(() => _enviando = false);
      _scrollAlFinal();
    }
  }

  void _scrollAlFinal() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      if (_scrollCtrl.hasClients) {
        _scrollCtrl.animateTo(
          _scrollCtrl.position.maxScrollExtent,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeOut,
        );
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Row(
          children: [
            Icon(Icons.smart_toy_outlined, size: 20),
            SizedBox(width: 8),
            Text('Asistente de viajes IA'),
          ],
        ),
        actions: [
          IconButton(
            icon: Icon(
              _mostrarOpciones ? Icons.tune : Icons.tune_outlined,
            ),
            tooltip: 'Parámetros del viaje',
            onPressed: () =>
                setState(() => _mostrarOpciones = !_mostrarOpciones),
          ),
        ],
      ),
      body: Column(
        children: [
          // Panel de parámetros (colapsable)
          AnimatedSize(
            duration: const Duration(milliseconds: 250),
            child: _mostrarOpciones
                ? Container(
                    color: Colors.white,
                    padding: const EdgeInsets.fromLTRB(16, 8, 16, 12),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        const Text(
                          'Parámetros del viaje (opcionales)',
                          style: TextStyle(
                              fontWeight: FontWeight.bold,
                              color: AppTheme.primary),
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Expanded(
                              child: _ParamField(
                                ctrl: _ciudadCtrl,
                                label: 'Ciudad',
                                icon: Icons.location_on_outlined,
                              ),
                            ),
                            const SizedBox(width: 8),
                            Expanded(
                              child: _ParamField(
                                ctrl: _diasCtrl,
                                label: 'Días',
                                icon: Icons.calendar_today_outlined,
                                tipo: TextInputType.number,
                              ),
                            ),
                          ],
                        ),
                        const SizedBox(height: 8),
                        Row(
                          children: [
                            Expanded(
                              child: _ParamField(
                                ctrl: _presupuestoCtrl,
                                label: 'Presupuesto',
                                icon: Icons.attach_money,
                                tipo: TextInputType.number,
                              ),
                            ),
                            const SizedBox(width: 8),
                            Expanded(
                              child: _ParamField(
                                ctrl: _personasCtrl,
                                label: 'Personas',
                                icon: Icons.group_outlined,
                                tipo: TextInputType.number,
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  )
                : const SizedBox.shrink(),
          ),
          const Divider(height: 1),
          // Mensajes
          Expanded(
            child: ListView.builder(
              controller: _scrollCtrl,
              padding: const EdgeInsets.all(16),
              itemCount: _mensajes.length,
              itemBuilder: (_, i) => _BurbujaMensaje(mensaje: _mensajes[i]),
            ),
          ),
          // Indicador de escritura
          if (_enviando)
            const Padding(
              padding: EdgeInsets.symmetric(horizontal: 16, vertical: 4),
              child: Row(
                children: [
                  SizedBox(
                    width: 20,
                    height: 20,
                    child: CircularProgressIndicator(strokeWidth: 2),
                  ),
                  SizedBox(width: 8),
                  Text('El asistente está pensando...',
                      style: TextStyle(color: AppTheme.textGrey)),
                ],
              ),
            ),
          // Input
          Container(
            padding: const EdgeInsets.all(12),
            color: Colors.white,
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _mensajeCtrl,
                    maxLines: null,
                    textInputAction: TextInputAction.send,
                    onSubmitted: (_) => _enviar(),
                    decoration: InputDecoration(
                      hintText: '¿A dónde quieres viajar?',
                      filled: true,
                      fillColor: const Color(0xFFF5F7FA),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(24),
                        borderSide: BorderSide.none,
                      ),
                      contentPadding: const EdgeInsets.symmetric(
                          horizontal: 16, vertical: 10),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                FloatingActionButton(
                  mini: true,
                  onPressed: _enviando ? null : _enviar,
                  backgroundColor: AppTheme.primary,
                  child: const Icon(Icons.send, color: Colors.white),
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }
}

class _BurbujaMensaje extends StatelessWidget {
  final ChatMessage mensaje;

  const _BurbujaMensaje({required this.mensaje});

  @override
  Widget build(BuildContext context) {
    final esUsuario = mensaje.esUsuario;

    return Align(
      alignment: esUsuario ? Alignment.centerRight : Alignment.centerLeft,
      child: Container(
        margin: const EdgeInsets.only(bottom: 12),
        constraints:
            BoxConstraints(maxWidth: MediaQuery.of(context).size.width * 0.78),
        padding: const EdgeInsets.symmetric(horizontal: 14, vertical: 10),
        decoration: BoxDecoration(
          color: esUsuario ? AppTheme.primary : Colors.white,
          borderRadius: BorderRadius.only(
            topLeft: const Radius.circular(16),
            topRight: const Radius.circular(16),
            bottomLeft: Radius.circular(esUsuario ? 16 : 4),
            bottomRight: Radius.circular(esUsuario ? 4 : 16),
          ),
          boxShadow: const [
            BoxShadow(color: Colors.black12, blurRadius: 4, offset: Offset(0, 2))
          ],
        ),
        child: Text(
          mensaje.texto,
          style: TextStyle(
            color: esUsuario ? Colors.white : AppTheme.textDark,
            height: 1.5,
          ),
        ),
      ),
    );
  }
}

class _ParamField extends StatelessWidget {
  final TextEditingController ctrl;
  final String label;
  final IconData icon;
  final TextInputType tipo;

  const _ParamField({
    required this.ctrl,
    required this.label,
    required this.icon,
    this.tipo = TextInputType.text,
  });

  @override
  Widget build(BuildContext context) {
    return TextField(
      controller: ctrl,
      keyboardType: tipo,
      decoration: InputDecoration(
        labelText: label,
        prefixIcon: Icon(icon, size: 18),
        isDense: true,
        contentPadding:
            const EdgeInsets.symmetric(horizontal: 10, vertical: 8),
        border: OutlineInputBorder(borderRadius: BorderRadius.circular(8)),
      ),
    );
  }
}
