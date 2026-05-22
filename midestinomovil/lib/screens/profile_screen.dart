import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../config/app_theme.dart';
import '../providers/auth_provider.dart';
import '../providers/cart_provider.dart';
import 'login_screen.dart';

class ProfileScreen extends StatelessWidget {
  const ProfileScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();
    final cliente = auth.cliente;
    final cart = context.watch<CartProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Mi perfil')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            // Avatar
            CircleAvatar(
              radius: 50,
              backgroundColor: AppTheme.primary,
              child: Text(
                (cliente?.nombreCompleto ?? 'U')[0].toUpperCase(),
                style: const TextStyle(
                    fontSize: 40,
                    color: Colors.white,
                    fontWeight: FontWeight.bold),
              ),
            ),
            const SizedBox(height: 16),
            Text(
              cliente?.nombreCompleto ?? '',
              style: const TextStyle(
                  fontSize: 22, fontWeight: FontWeight.bold),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 4),
            Text(
              cliente?.email ?? '',
              style: const TextStyle(color: AppTheme.textGrey),
            ),
            const SizedBox(height: 32),
            // Tarjetas de info
            _InfoCard(
              children: [
                _InfoRow(
                    icon: Icons.person_outline,
                    label: 'Usuario',
                    value: cliente?.nombreUsuario ?? '-'),
                _InfoRow(
                    icon: Icons.phone_outlined,
                    label: 'Teléfono',
                    value: cliente?.numeroTelefono ?? '-'),
                _InfoRow(
                    icon: Icons.badge_outlined,
                    label: 'Documento',
                    value:
                        '${cliente?.tipoDocumento ?? ''} - ${cliente?.numeroDocumento ?? '-'}'),
                if (cliente?.presupuesto != null)
                  _InfoRow(
                    icon: Icons.account_balance_wallet_outlined,
                    label: 'Presupuesto',
                    value:
                        '\$${cliente!.presupuesto!.toStringAsFixed(0)}',
                  ),
              ],
            ),
            const SizedBox(height: 16),
            // Estadísticas
            _InfoCard(
              children: [
                _InfoRow(
                  icon: Icons.shopping_bag_outlined,
                  label: 'Planes en carrito',
                  value: '${cart.cantidad}',
                ),
              ],
            ),
            const SizedBox(height: 32),
            // Cerrar sesión
            OutlinedButton.icon(
              onPressed: () async {
                final confirmar = await showDialog<bool>(
                  context: context,
                  builder: (_) => AlertDialog(
                    title: const Text('Cerrar sesión'),
                    content:
                        const Text('¿Estás seguro de que deseas salir?'),
                    actions: [
                      TextButton(
                        onPressed: () => Navigator.of(context).pop(false),
                        child: const Text('Cancelar'),
                      ),
                      ElevatedButton(
                        onPressed: () => Navigator.of(context).pop(true),
                        style: ElevatedButton.styleFrom(
                            backgroundColor: Colors.red),
                        child: const Text('Salir'),
                      ),
                    ],
                  ),
                );
                if (confirmar != true) return;
                if (!context.mounted) return;
                await context.read<AuthProvider>().cerrarSesion();
                if (!context.mounted) return;
                Navigator.of(context).pushAndRemoveUntil(
                  MaterialPageRoute(builder: (_) => const LoginScreen()),
                  (_) => false,
                );
              },
              icon: const Icon(Icons.logout, color: Colors.red),
              label: const Text(
                'Cerrar sesión',
                style: TextStyle(color: Colors.red),
              ),
              style: OutlinedButton.styleFrom(
                minimumSize: const Size(double.infinity, 52),
                side: const BorderSide(color: Colors.red),
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(12)),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _InfoCard extends StatelessWidget {
  final List<Widget> children;

  const _InfoCard({required this.children});

  @override
  Widget build(BuildContext context) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.symmetric(vertical: 8),
        child: Column(
          children: [
            for (int i = 0; i < children.length; i++) ...[
              children[i],
              if (i < children.length - 1) const Divider(height: 1),
            ],
          ],
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  final IconData icon;
  final String label;
  final String value;

  const _InfoRow(
      {required this.icon, required this.label, required this.value});

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      child: Row(
        children: [
          Icon(icon, size: 20, color: AppTheme.primary),
          const SizedBox(width: 12),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(label,
                    style: const TextStyle(
                        fontSize: 11, color: AppTheme.textGrey)),
                Text(value,
                    style: const TextStyle(fontWeight: FontWeight.w500)),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
