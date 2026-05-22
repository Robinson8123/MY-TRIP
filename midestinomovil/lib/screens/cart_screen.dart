import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../config/app_theme.dart';
import '../providers/auth_provider.dart';
import '../providers/cart_provider.dart';
import '../providers/plans_provider.dart';

class CartScreen extends StatefulWidget {
  const CartScreen({super.key});

  @override
  State<CartScreen> createState() => _CartScreenState();
}

class _CartScreenState extends State<CartScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final clienteId = context.read<AuthProvider>().cliente?.idCliente;
      if (clienteId != null) {
        context.read<CartProvider>().cargarCarrito(clienteId);
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    final cart = context.watch<CartProvider>();
    final auth = context.watch<AuthProvider>();
    final plans = context.read<PlansProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Mi carrito')),
      body: cart.cargando
          ? const Center(child: CircularProgressIndicator())
          : cart.items.isEmpty
              ? Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(Icons.shopping_bag_outlined,
                          size: 80, color: Colors.grey.shade300),
                      const SizedBox(height: 16),
                      const Text(
                        'Tu carrito está vacío',
                        style:
                            TextStyle(fontSize: 18, color: AppTheme.textGrey),
                      ),
                    ],
                  ),
                )
              : Column(
                  children: [
                    Expanded(
                      child: ListView.builder(
                        padding: const EdgeInsets.all(16),
                        itemCount: cart.items.length,
                        itemBuilder: (_, i) {
                          final item = cart.items[i];
                          return Card(
                            margin: const EdgeInsets.only(bottom: 12),
                            child: ListTile(
                              contentPadding: const EdgeInsets.all(12),
                              leading: ClipRRect(
                                borderRadius: BorderRadius.circular(8),
                                child: Image.network(
                                  plans.getImagenUrl(item.planEmpresa.id),
                                  width: 60,
                                  height: 60,
                                  fit: BoxFit.cover,
                                  errorBuilder: (_, __, ___) => Container(
                                    width: 60,
                                    height: 60,
                                    color: const Color(0xFFE5E7EB),
                                    child: const Icon(Icons.landscape,
                                        color: Colors.grey),
                                  ),
                                ),
                              ),
                              title: Text(
                                item.planEmpresa.nombre,
                                style: const TextStyle(
                                    fontWeight: FontWeight.bold),
                              ),
                              subtitle: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  if (item.planEmpresa.ciudad != null)
                                    Text(item.planEmpresa.ciudad!,
                                        style: const TextStyle(
                                            color: AppTheme.textGrey,
                                            fontSize: 12)),
                                  Text(
                                    item.planEmpresa.precioFormateado,
                                    style: const TextStyle(
                                      color: AppTheme.secondary,
                                      fontWeight: FontWeight.bold,
                                    ),
                                  ),
                                ],
                              ),
                              trailing: IconButton(
                                icon: const Icon(Icons.delete_outline,
                                    color: Colors.red),
                                onPressed: () => cart.eliminar(
                                    item.id,
                                    auth.cliente!.idCliente!),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
                    // Total
                    Container(
                      padding: const EdgeInsets.all(20),
                      decoration: const BoxDecoration(
                        color: Colors.white,
                        boxShadow: [
                          BoxShadow(
                              color: Colors.black12,
                              blurRadius: 8,
                              offset: Offset(0, -2))
                        ],
                      ),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: [
                          Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text('Total',
                                  style: TextStyle(color: AppTheme.textGrey)),
                              Text(
                                '\$${cart.total.toStringAsFixed(0)}',
                                style: const TextStyle(
                                  fontSize: 22,
                                  fontWeight: FontWeight.bold,
                                  color: AppTheme.primary,
                                ),
                              ),
                            ],
                          ),
                          ElevatedButton.icon(
                            onPressed: () => _mostrarDialogoPago(context),
                            icon: const Icon(Icons.payment),
                            label: const Text('Pagar'),
                            style: ElevatedButton.styleFrom(
                                minimumSize: const Size(130, 48)),
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
    );
  }

  void _mostrarDialogoPago(BuildContext context) {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text('Procesando pago'),
        content: const Text(
            'El proceso de pago se completa a través de la plataforma web de Mi Destino. '
            'Te redirigiremos al checkout en tu navegador.'),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Cancelar'),
          ),
          ElevatedButton(
            onPressed: () => Navigator.of(context).pop(),
            child: const Text('Entendido'),
          ),
        ],
      ),
    );
  }
}
