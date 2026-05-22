import 'package:flutter/material.dart';
import 'package:flutter_rating_bar/flutter_rating_bar.dart';
import 'package:provider/provider.dart';
import '../config/app_theme.dart';
import '../models/plan_model.dart';
import '../providers/auth_provider.dart';
import '../providers/cart_provider.dart';
import '../providers/plans_provider.dart';
import '../services/plan_service.dart';

class PlanDetailScreen extends StatefulWidget {
  final PlanEmpresa plan;

  const PlanDetailScreen({super.key, required this.plan});

  @override
  State<PlanDetailScreen> createState() => _PlanDetailScreenState();
}

class _PlanDetailScreenState extends State<PlanDetailScreen> {
  final _planService = PlanService();
  List<ValoracionPlan> _resenas = [];
  bool _cargandoResenas = false;
  double _miPuntuacion = 0;
  final _comentarioCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _cargarResenas();
  }

  @override
  void dispose() {
    _comentarioCtrl.dispose();
    super.dispose();
  }

  Future<void> _cargarResenas() async {
    setState(() => _cargandoResenas = true);
    try {
      _resenas = await _planService.obtenerResenas(widget.plan.id);
    } catch (_) {}
    if (mounted) setState(() => _cargandoResenas = false);
  }

  Future<void> _agregarAlCarrito() async {
    final auth = context.read<AuthProvider>();
    final cart = context.read<CartProvider>();

    if (auth.cliente == null) return;

    final ok = await cart.agregar(
      clienteId: auth.cliente!.idCliente!,
      planId: widget.plan.id,
      nombrePlan: widget.plan.nombre,
      precio: widget.plan.precio,
    );

    if (!mounted) return;
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(ok
            ? '¡${widget.plan.nombre} agregado al carrito!'
            : cart.error ?? 'Error al agregar'),
        backgroundColor: ok ? Colors.green : Colors.red,
      ),
    );
  }

  Future<void> _enviarValoracion() async {
    if (_miPuntuacion == 0) return;
    final auth = context.read<AuthProvider>();
    if (auth.cliente == null) return;

    try {
      await _planService.agregarValoracion(
        planId: widget.plan.id,
        clienteId: auth.cliente!.idCliente!,
        puntuacion: _miPuntuacion.toInt(),
        comentario: _comentarioCtrl.text.trim().isEmpty
            ? null
            : _comentarioCtrl.text.trim(),
      );
      _comentarioCtrl.clear();
      setState(() => _miPuntuacion = 0);
      await _cargarResenas();
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('¡Reseña enviada!'),
            backgroundColor: Colors.green,
          ),
        );
      }
    } catch (e) {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text(e.toString().replaceFirst('Exception: ', '')),
            backgroundColor: Colors.red,
          ),
        );
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    final imagenUrl =
        context.read<PlansProvider>().getImagenUrl(widget.plan.id);

    return Scaffold(
      body: CustomScrollView(
        slivers: [
          // AppBar con imagen
          SliverAppBar(
            expandedHeight: 260,
            pinned: true,
            flexibleSpace: FlexibleSpaceBar(
              title: Text(
                widget.plan.nombre,
                style: const TextStyle(
                    shadows: [Shadow(blurRadius: 8, color: Colors.black)]),
              ),
              background: Image.network(
                imagenUrl,
                fit: BoxFit.cover,
                errorBuilder: (_, __, ___) => Container(
                  color: AppTheme.primary.withValues(alpha: 0.3),
                  child: const Icon(Icons.landscape,
                      size: 80, color: Colors.white54),
                ),
              ),
            ),
          ),
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // Precio y rating
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        widget.plan.precioFormateado,
                        style: const TextStyle(
                          fontSize: 26,
                          fontWeight: FontWeight.bold,
                          color: AppTheme.secondary,
                        ),
                      ),
                      if (widget.plan.valoracionPromedio != null)
                        Chip(
                          avatar: const Icon(Icons.star,
                              color: Colors.amber, size: 18),
                          label: Text(
                            widget.plan.valoracionPromedio!.toStringAsFixed(1),
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                        ),
                    ],
                  ),
                  const SizedBox(height: 12),
                  // Info chips
                  Wrap(
                    spacing: 8,
                    runSpacing: 8,
                    children: [
                      if (widget.plan.ciudad != null)
                        _InfoChip(
                            icon: Icons.location_on,
                            text: widget.plan.ciudad!),
                      if (widget.plan.tipoSitio != null)
                        _InfoChip(
                            icon: Icons.category_outlined,
                            text: widget.plan.tipoSitio!),
                      if (widget.plan.horario != null)
                        _InfoChip(
                            icon: Icons.schedule,
                            text: widget.plan.horario!),
                      if (widget.plan.personasDisponibles != null)
                        _InfoChip(
                            icon: Icons.group,
                            text:
                                '${widget.plan.personasDisponibles} personas'),
                    ],
                  ),
                  if (widget.plan.informacionGeneral != null) ...[
                    const SizedBox(height: 20),
                    const Text(
                      'Descripción',
                      style: TextStyle(
                          fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Text(
                      widget.plan.informacionGeneral!,
                      style: const TextStyle(
                          color: AppTheme.textGrey, height: 1.6),
                    ),
                  ],
                  if (widget.plan.metodosPagoAceptados != null) ...[
                    const SizedBox(height: 16),
                    Row(
                      children: [
                        const Icon(Icons.payment,
                            size: 16, color: AppTheme.textGrey),
                        const SizedBox(width: 6),
                        Expanded(
                          child: Text(
                            'Pago: ${widget.plan.metodosPagoAceptados}',
                            style: const TextStyle(color: AppTheme.textGrey),
                          ),
                        ),
                      ],
                    ),
                  ],
                  const SizedBox(height: 24),
                  // Botón agregar al carrito
                  ElevatedButton.icon(
                    onPressed: _agregarAlCarrito,
                    icon: const Icon(Icons.shopping_bag_outlined),
                    label: const Text('Agregar al carrito',
                        style: TextStyle(fontSize: 16)),
                  ),
                  const SizedBox(height: 32),
                  // Sección reseñas
                  const Text(
                    'Reseñas',
                    style: TextStyle(
                        fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 12),
                  // Formulario para dejar reseña
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          const Text('Deja tu valoración:',
                              style: TextStyle(fontWeight: FontWeight.w600)),
                          const SizedBox(height: 8),
                          RatingBar.builder(
                            initialRating: _miPuntuacion,
                            minRating: 1,
                            itemSize: 32,
                            itemBuilder: (_, __) => const Icon(Icons.star,
                                color: Colors.amber),
                            onRatingUpdate: (r) =>
                                setState(() => _miPuntuacion = r),
                          ),
                          const SizedBox(height: 8),
                          TextField(
                            controller: _comentarioCtrl,
                            maxLines: 2,
                            decoration: const InputDecoration(
                              hintText: 'Comentario (opcional)',
                            ),
                          ),
                          const SizedBox(height: 8),
                          Align(
                            alignment: Alignment.centerRight,
                            child: ElevatedButton(
                              onPressed:
                                  _miPuntuacion > 0 ? _enviarValoracion : null,
                              style: ElevatedButton.styleFrom(
                                  minimumSize: const Size(120, 40)),
                              child: const Text('Enviar'),
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 12),
                  // Lista de reseñas
                  if (_cargandoResenas)
                    const Center(child: CircularProgressIndicator())
                  else if (_resenas.isEmpty)
                    const Padding(
                      padding: EdgeInsets.symmetric(vertical: 16),
                      child: Text('Aún no hay reseñas.',
                          style: TextStyle(color: Colors.grey)),
                    )
                  else
                    ListView.separated(
                      physics: const NeverScrollableScrollPhysics(),
                      shrinkWrap: true,
                      itemCount: _resenas.length,
                      separatorBuilder: (_, __) => const Divider(),
                      itemBuilder: (_, i) {
                        final r = _resenas[i];
                        return ListTile(
                          contentPadding: EdgeInsets.zero,
                          leading: CircleAvatar(
                            backgroundColor: AppTheme.primary,
                            child: Text(
                              (r.nombreCliente ?? 'U')[0].toUpperCase(),
                              style: const TextStyle(color: Colors.white),
                            ),
                          ),
                          title: Text(r.nombreCliente ?? 'Usuario'),
                          subtitle: r.comentario != null
                              ? Text(r.comentario!)
                              : null,
                          trailing: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              const Icon(Icons.star,
                                  color: Colors.amber, size: 16),
                              Text('${r.puntuacion}'),
                            ],
                          ),
                        );
                      },
                    ),
                  const SizedBox(height: 24),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _InfoChip extends StatelessWidget {
  final IconData icon;
  final String text;

  const _InfoChip({required this.icon, required this.text});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
      decoration: BoxDecoration(
        color: AppTheme.primary.withValues(alpha: 0.08),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 14, color: AppTheme.primary),
          const SizedBox(width: 4),
          Text(text,
              style: const TextStyle(
                  fontSize: 12,
                  color: AppTheme.primary,
                  fontWeight: FontWeight.w500)),
        ],
      ),
    );
  }
}
