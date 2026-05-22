import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import '../models/cliente_model.dart';
import '../providers/auth_provider.dart';
import '../widgets/custom_text_field.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({super.key});

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();
  final _nombreCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _usuarioCtrl = TextEditingController();
  final _passCtrl = TextEditingController();
  final _telefonoCtrl = TextEditingController();
  final _presupuestoCtrl = TextEditingController();
  String _tipoDoc = 'CC';

  @override
  void dispose() {
    _nombreCtrl.dispose();
    _emailCtrl.dispose();
    _usuarioCtrl.dispose();
    _passCtrl.dispose();
    _telefonoCtrl.dispose();
    _presupuestoCtrl.dispose();
    super.dispose();
  }

  Future<void> _registrar() async {
    if (!_formKey.currentState!.validate()) return;

    final auth = context.read<AuthProvider>();
    final cliente = Cliente(
      nombreCompleto: _nombreCtrl.text.trim(),
      email: _emailCtrl.text.trim(),
      nombreUsuario: _usuarioCtrl.text.trim(),
      contrasena: _passCtrl.text,
      numeroTelefono: _telefonoCtrl.text.trim(),
      tipoDocumento: _tipoDoc,
      tipoUsuario: 'CLIENTE',
      presupuesto: double.tryParse(_presupuestoCtrl.text),
    );

    final ok = await auth.registrar(cliente);
    if (!mounted) return;

    if (ok) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('¡Cuenta creada! Ahora inicia sesión.'),
          backgroundColor: Colors.green,
        ),
      );
      Navigator.of(context).pop();
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(auth.error ?? 'Error al registrar'),
          backgroundColor: Colors.red,
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    final auth = context.watch<AuthProvider>();

    return Scaffold(
      appBar: AppBar(title: const Text('Crear cuenta')),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              CustomTextField(
                label: 'Nombre completo',
                controller: _nombreCtrl,
                prefixIcon: Icons.person_outline,
                validator: (v) =>
                    v == null || v.isEmpty ? 'Campo requerido' : null,
              ),
              const SizedBox(height: 16),
              CustomTextField(
                label: 'Correo electrónico',
                controller: _emailCtrl,
                keyboardType: TextInputType.emailAddress,
                prefixIcon: Icons.email_outlined,
                validator: (v) =>
                    v == null || v.isEmpty ? 'Campo requerido' : null,
              ),
              const SizedBox(height: 16),
              CustomTextField(
                label: 'Nombre de usuario',
                controller: _usuarioCtrl,
                prefixIcon: Icons.alternate_email,
                validator: (v) =>
                    v == null || v.isEmpty ? 'Campo requerido' : null,
              ),
              const SizedBox(height: 16),
              CustomTextField(
                label: 'Contraseña',
                controller: _passCtrl,
                obscureText: true,
                prefixIcon: Icons.lock_outline,
                validator: (v) =>
                    v == null || v.length < 6 ? 'Mínimo 6 caracteres' : null,
              ),
              const SizedBox(height: 16),
              CustomTextField(
                label: 'Teléfono',
                controller: _telefonoCtrl,
                keyboardType: TextInputType.phone,
                prefixIcon: Icons.phone_outlined,
              ),
              const SizedBox(height: 16),
              // Tipo de documento
              Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text(
                    'Tipo de documento',
                    style: TextStyle(
                        fontWeight: FontWeight.w600,
                        fontSize: 14,
                        color: Color(0xFF374151)),
                  ),
                  const SizedBox(height: 6),
                  DropdownButtonFormField<String>(
                    initialValue: _tipoDoc,
                    decoration: const InputDecoration(),
                    items: const [
                      DropdownMenuItem(value: 'CC', child: Text('Cédula de ciudadanía')),
                      DropdownMenuItem(value: 'TI', child: Text('Tarjeta de identidad')),
                      DropdownMenuItem(value: 'CE', child: Text('Cédula extranjera')),
                      DropdownMenuItem(value: 'PA', child: Text('Pasaporte')),
                    ],
                    onChanged: (v) => setState(() => _tipoDoc = v!),
                  ),
                ],
              ),
              const SizedBox(height: 16),
              CustomTextField(
                label: 'Presupuesto (COP)',
                controller: _presupuestoCtrl,
                keyboardType: TextInputType.number,
                prefixIcon: Icons.attach_money,
              ),
              const SizedBox(height: 28),
              ElevatedButton(
                onPressed: auth.cargando ? null : _registrar,
                child: auth.cargando
                    ? const SizedBox(
                        height: 20,
                        width: 20,
                        child: CircularProgressIndicator(
                            color: Colors.white, strokeWidth: 2),
                      )
                    : const Text('Registrarse',
                        style: TextStyle(fontSize: 16)),
              ),
              const SizedBox(height: 24),
            ],
          ),
        ),
      ),
    );
  }
}
