import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../config/app_config.dart';
import '../config/app_theme.dart';
import '../models/recomendacion_model.dart';
import '../providers/auth_provider.dart';
import '../providers/cart_provider.dart';
import '../providers/plans_provider.dart';
import '../providers/recomendacion_provider.dart';
import '../widgets/plan_card.dart';
import 'cart_screen.dart';
import 'chat_screen.dart';
import 'plan_detail_screen.dart';
import 'profile_screen.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  int _tabIndex = 0;
  final _searchCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      context.read<PlansProvider>().cargarPlanes();
      final clienteId = context.read<AuthProvider>().cliente?.idCliente;
      if (clienteId != null) {
        context.read<RecomendacionProvider>().cargar(clienteId);
      }
    });
  }

  @override
  void dispose() {
    _searchCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final pages = [
      _PlanesTab(searchCtrl: _searchCtrl),
      const CartScreen(),
      const ChatScreen(),
      const ProfileScreen(),
    ];

    return Scaffold(
      body: pages[_tabIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _tabIndex,
        onDestinationSelected: (i) => setState(() => _tabIndex = i),
        destinations: [
          const NavigationDestination(
            icon: Icon(Icons.explore_outlined),
            selectedIcon: Icon(Icons.explore),
            label: 'Explorar',
          ),
          NavigationDestination(
            icon: Badge(
              label: Text('${context.watch<CartProvider>().cantidad}'),
              isLabelVisible: context.watch<CartProvider>().cantidad > 0,
              child: const Icon(Icons.shopping_bag_outlined),
            ),
            selectedIcon: const Icon(Icons.shopping_bag),
            label: 'Carrito',
          ),
          const NavigationDestination(
            icon: Icon(Icons.chat_bubble_outline),
            selectedIcon: Icon(Icons.chat_bubble),
            label: 'Asistente IA',
          ),
          const NavigationDestination(
            icon: Icon(Icons.person_outline),
            selectedIcon: Icon(Icons.person),
            label: 'Perfil',
          ),
        ],
      ),
    );
  }
}

class _PlanesTab extends StatelessWidget {
  final TextEditingController searchCtrl;

  const _PlanesTab({required this.searchCtrl});

  @override
  Widget build(BuildContext context) {
    final plans = context.watch<PlansProvider>();
    final auth = context.watch<AuthProvider>();
    final recomendaciones = context.watch<RecomendacionProvider>();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Mi Destino'),
        actions: [
          IconButton(
            icon: const Icon(Icons.refresh),
            onPressed: () {
              context.read<PlansProvider>().cargarPlanes();
              final id = auth.cliente?.idCliente;
              if (id != null) context.read<RecomendacionProvider>().cargar(id);
            },
          ),
        ],
      ),
      body: CustomScrollView(
        slivers: [
          // Encabezado con saludo y buscador
          SliverToBoxAdapter(
            child: Container(
              width: double.infinity,
              padding: const EdgeInsets.fromLTRB(20, 20, 20, 20),
              decoration: const BoxDecoration(
                color: AppTheme.primary,
                borderRadius:
                    BorderRadius.vertical(bottom: Radius.circular(24)),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    '¡Hola, ${auth.cliente?.nombreCompleto.split(' ').first ?? 'Viajero'}!',
                    style: const TextStyle(
                        color: Colors.white,
                        fontSize: 20,
                        fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 4),
                  const Text(
                    '¿A dónde quieres ir hoy?',
                    style: TextStyle(color: Colors.white70),
                  ),
                  const SizedBox(height: 16),
                  TextField(
                    controller: searchCtrl,
                    onChanged: context.read<PlansProvider>().buscar,
                    decoration: InputDecoration(
                      hintText: 'Buscar destinos...',
                      filled: true,
                      fillColor: Colors.white,
                      prefixIcon: const Icon(Icons.search),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                      contentPadding: const EdgeInsets.symmetric(vertical: 0),
                    ),
                  ),
                ],
              ),
            ),
          ),

          // Sección "Recomendados para ti"
          if (auth.estaAutenticado)
            SliverToBoxAdapter(
              child: _RecomendadosSection(
                recomendaciones: recomendaciones.recomendaciones,
                cargando: recomendaciones.cargando,
              ),
            ),

          // Título "Todos los planes"
          const SliverToBoxAdapter(
            child: Padding(
              padding: EdgeInsets.fromLTRB(16, 16, 16, 8),
              child: Text(
                'Todos los planes',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
            ),
          ),

          // Grid de planes
          if (plans.cargando)
            const SliverFillRemaining(
              child: Center(child: CircularProgressIndicator()),
            )
          else if (plans.error != null)
            SliverFillRemaining(
              child: Center(
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    const Icon(Icons.wifi_off, size: 48, color: Colors.grey),
                    const SizedBox(height: 12),
                    Text(plans.error!,
                        style: const TextStyle(color: Colors.grey)),
                    const SizedBox(height: 12),
                    ElevatedButton(
                      onPressed: () =>
                          context.read<PlansProvider>().cargarPlanes(),
                      child: const Text('Reintentar'),
                    ),
                  ],
                ),
              ),
            )
          else if (plans.planes.isEmpty)
            const SliverFillRemaining(
              child: Center(
                child: Text('No se encontraron planes',
                    style: TextStyle(color: Colors.grey)),
              ),
            )
          else
            SliverPadding(
              padding: const EdgeInsets.fromLTRB(16, 0, 16, 16),
              sliver: SliverGrid(
                gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                  crossAxisCount: 2,
                  childAspectRatio: 0.72,
                  crossAxisSpacing: 12,
                  mainAxisSpacing: 12,
                ),
                delegate: SliverChildBuilderDelegate(
                  (context, i) {
                    final plan = plans.planes[i];
                    return PlanCard(
                      plan: plan,
                      imagenUrl: plans.getImagenUrl(plan.id),
                      onTap: () => Navigator.of(context).push(
                        MaterialPageRoute(
                          builder: (_) => PlanDetailScreen(plan: plan),
                        ),
                      ),
                    );
                  },
                  childCount: plans.planes.length,
                ),
              ),
            ),
        ],
      ),
    );
  }
}

class _RecomendadosSection extends StatelessWidget {
  final List<Recomendacion> recomendaciones;
  final bool cargando;

  const _RecomendadosSection({
    required this.recomendaciones,
    required this.cargando,
  });

  @override
  Widget build(BuildContext context) {
    if (!cargando && recomendaciones.isEmpty) return const SizedBox.shrink();

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
          child: Row(
            children: [
              const Icon(Icons.auto_awesome, size: 18, color: AppTheme.primary),
              const SizedBox(width: 6),
              const Text(
                'Recomendados para ti',
                style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
              ),
              const SizedBox(width: 4),
              Container(
                padding:
                    const EdgeInsets.symmetric(horizontal: 6, vertical: 2),
                decoration: BoxDecoration(
                  color: AppTheme.primary.withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: const Text(
                  'IA',
                  style: TextStyle(
                      fontSize: 10,
                      color: AppTheme.primary,
                      fontWeight: FontWeight.bold),
                ),
              ),
            ],
          ),
        ),
        SizedBox(
          height: 200,
          child: cargando
              ? const Center(child: CircularProgressIndicator())
              : ListView.separated(
                  scrollDirection: Axis.horizontal,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: recomendaciones.length,
                  separatorBuilder: (_, __) => const SizedBox(width: 12),
                  itemBuilder: (context, i) =>
                      _RecomendadoCard(rec: recomendaciones[i]),
                ),
        ),
      ],
    );
  }
}

class _RecomendadoCard extends StatelessWidget {
  final Recomendacion rec;

  const _RecomendadoCard({required this.rec});

  @override
  Widget build(BuildContext context) {
    final imgUrl = '${AppConfig.baseUrl}/planes/${rec.planId}/imagen';

    return GestureDetector(
      onTap: () {
        // Navega al detalle usando solo los datos disponibles del plan
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Abriendo ${rec.nombre}...')),
        );
      },
      child: SizedBox(
        width: 150,
        child: Card(
          clipBehavior: Clip.antiAlias,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Expanded(
                child: Stack(
                  fit: StackFit.expand,
                  children: [
                    Image.network(
                      imgUrl,
                      fit: BoxFit.cover,
                      errorBuilder: (_, __, ___) => Container(
                        color: const Color(0xFFE5E7EB),
                        child: const Icon(Icons.landscape,
                            size: 36, color: Colors.grey),
                      ),
                    ),
                    if (rec.tipoSitio != null)
                      Positioned(
                        top: 6,
                        left: 6,
                        child: Container(
                          padding: const EdgeInsets.symmetric(
                              horizontal: 6, vertical: 3),
                          decoration: BoxDecoration(
                            color: AppTheme.primary.withValues(alpha: 0.9),
                            borderRadius: BorderRadius.circular(6),
                          ),
                          child: Text(
                            rec.tipoSitio!,
                            style: const TextStyle(
                                color: Colors.white,
                                fontSize: 9,
                                fontWeight: FontWeight.bold),
                          ),
                        ),
                      ),
                  ],
                ),
              ),
              Padding(
                padding: const EdgeInsets.all(8),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      rec.nombre,
                      style: const TextStyle(
                          fontWeight: FontWeight.bold, fontSize: 12),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 2),
                    Text(
                      rec.ciudad ?? '',
                      style:
                          const TextStyle(fontSize: 10, color: Colors.grey),
                      maxLines: 1,
                      overflow: TextOverflow.ellipsis,
                    ),
                    const SizedBox(height: 4),
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Text(
                          rec.precioFormateado,
                          style: const TextStyle(
                            color: AppTheme.secondary,
                            fontWeight: FontWeight.bold,
                            fontSize: 11,
                          ),
                        ),
                        if (rec.valoracionPromedio != null)
                          Row(
                            children: [
                              const Icon(Icons.star,
                                  size: 10, color: Colors.amber),
                              Text(
                                rec.valoracionPromedio!.toStringAsFixed(1),
                                style: const TextStyle(fontSize: 10),
                              ),
                            ],
                          ),
                      ],
                    ),
                  ],
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
