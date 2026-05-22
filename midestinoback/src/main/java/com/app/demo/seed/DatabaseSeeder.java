package com.app.demo.seed;

import com.app.demo.persistence.entity.Cliente;
import com.app.demo.persistence.entity.Empresa;
import com.app.demo.persistence.entity.PlanEmpresa;
import com.app.demo.persistence.repository.ClienteRepository;
import com.app.demo.persistence.repository.EmpresaRepository;
import com.app.demo.persistence.repository.PlanEmpresaRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("seed")
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

        private final EmpresaRepository empresaRepository;
        private final ClienteRepository clienteRepository;
        private final PlanEmpresaRepository planEmpresaRepository;

        @Override
        public void run(String... args) {
        log.info("Iniciando seed de base de datos");

                Map<String, Empresa> empresas = seedEmpresas();
                seedClientes();
                seedAdmin();
                seedPlanes(empresas);

                log.info("Seed completado satisfactoriamente");
                System.exit(0);
        }

        private Map<String, Empresa> seedEmpresas() {
                Map<String, Empresa> empresasPorNombre = new HashMap<>();

                for (Empresa empresaSeed : empresasSeed()) {
                        Empresa empresa = empresaRepository.findByNombre(empresaSeed.getNombre())
                                        .orElseGet(() -> empresaRepository.save(empresaSeed));
                        empresasPorNombre.put(empresa.getNombre(), empresa);
                }

                return empresasPorNombre;
        }

        private void seedClientes() {
                for (Cliente clienteSeed : clientesSeed()) {
                        if (clienteRepository.existsByEmail(clienteSeed.getEmail())) {
                                continue;
                        }
                        clienteRepository.save(clienteSeed);
                }
        }

        private void seedAdmin() {
                Cliente admin = adminSeed();
                clienteRepository.findByEmail(admin.getEmail())
                                .orElseGet(() -> clienteRepository.save(admin));
        }

        private void seedPlanes(Map<String, Empresa> empresas) {
                List<PlanEmpresa> planes = new ArrayList<>();

                for (PlanSeed planSeed : planSeeds()) {
                        Empresa empresa = empresas.get(planSeed.empresaNombre());
                        if (empresa == null) {
                                log.warn("No se encontró la empresa '{}' para el plan '{}'", planSeed.empresaNombre(),
                                                planSeed.nombre());
                                continue;
                        }

                        if (planEmpresaRepository.existsByNombre(planSeed.nombre())) {
                                continue;
                        }

                        PlanEmpresa plan = PlanEmpresa.builder()
                                        .nombre(planSeed.nombre())
                                        .tipoSitio(planSeed.tipoSitio())
                                        .direccion(planSeed.direccion())
                                        .horario(planSeed.horario())
                                        .email(planSeed.email())
                                        .ciudad(planSeed.ciudad())
                                        .telefono(planSeed.telefono())
                                        .precio(planSeed.precio())
                                        .cantidadDisponible(planSeed.cantidadDisponible())
                                        .personasDisponibles(planSeed.personasDisponibles())
                                        .informacionGeneral(planSeed.informacionGeneral())
                                        .imagen(planSeed.imagenUrl())
                                        .fechaRegistro(planSeed.fechaRegistro())
                                        .valoracionPromedio(planSeed.valoracionPromedio())
                                        .metodosPagoAceptados(planSeed.metodosPago())
                                        .disponible(true)
                                        .empresa(empresa)
                                        .build();

                        planes.add(plan);
                }

                if (!planes.isEmpty()) {
                        planEmpresaRepository.saveAll(planes);
                }
        }

        private List<Empresa> empresasSeed() {
                return List.of(
                                Empresa.builder()
                                                .nombre("Caribe Aventuras SAS")
                                                .razonSocial("Caribe Aventuras SAS")
                                                .nit("901234567-1")
                                                .sector("Turismo")
                                                .fechaFundacion("2010-05-12")
                                                .direccion("Cra 2 #5-45, Centro Histórico")
                                                .ciudad("Cartagena")
                                                .telefono("3001234567")
                                                .correo("contacto@caribeaventuras.co")
                                                .web("https://caribeaventuras.co")
                                                .numeroRegistroMercantil("123456789")
                                                .fechaRegistro("2024-01-15")
                                                .entidadRegistro("Cámara de Comercio de Cartagena")
                                                .tipoSociedad("SAS")
                                                .nombreRepresentanteLegal("Valentina Herrera")
                                                .numeroDocumentoRepresentanteLegal("1020304050")
                                                .cargoPropietario("Gerente General")
                                                .nombrePropietarioPrincipal("Valentina Herrera")
                                                .tipoUsuario("Empresa")
                                                .contrasena("caribe123")
                                                .empresaFueAceptada(true)
                                                .empresaFueAceptadaPorAdmin(true)
                                                .empresaTuvoRespuesta(true)
                                                .build(),
                                Empresa.builder()
                                                .nombre("Andes Trekking Co")
                                                .razonSocial("Andes Trekking Co SAS")
                                                .nit("901987654-2")
                                                .sector("Turismo de aventura")
                                                .fechaFundacion("2013-08-19")
                                                .direccion("Calle 10 #43B-30")
                                                .ciudad("Medellín")
                                                .telefono("3009876543")
                                                .correo("hola@andestrekking.co")
                                                .web("https://andestrekking.co")
                                                .numeroRegistroMercantil("234567891")
                                                .fechaRegistro("2024-02-10")
                                                .entidadRegistro("Cámara de Comercio de Medellín")
                                                .tipoSociedad("SAS")
                                                .nombreRepresentanteLegal("Juan Esteban Ríos")
                                                .numeroDocumentoRepresentanteLegal("1030507090")
                                                .cargoPropietario("Director Ejecutivo")
                                                .nombrePropietarioPrincipal("Juan Esteban Ríos")
                                                .tipoUsuario("Empresa")
                                                .contrasena("andes321")
                                                .empresaFueAceptada(true)
                                                .empresaFueAceptadaPorAdmin(true)
                                                .empresaTuvoRespuesta(true)
                                                .build(),
                                Empresa.builder()
                                                .nombre("Sabores Urbanos LTDA")
                                                .razonSocial("Sabores Urbanos LTDA")
                                                .nit("900654321-7")
                                                .sector("Gastronomía y cultura")
                                                .fechaFundacion("2015-03-22")
                                                .direccion("Carrera 7 #60-45")
                                                .ciudad("Bogotá")
                                                .telefono("3115556677")
                                                .correo("info@saboresurbanos.co")
                                                .web("https://saboresurbanos.co")
                                                .numeroRegistroMercantil("345678912")
                                                .fechaRegistro("2024-03-05")
                                                .entidadRegistro("Cámara de Comercio de Bogotá")
                                                .tipoSociedad("LTDA")
                                                .nombreRepresentanteLegal("Mariana Torres")
                                                .numeroDocumentoRepresentanteLegal("1012345678")
                                                .cargoPropietario("Gerente General")
                                                .nombrePropietarioPrincipal("Mariana Torres")
                                                .tipoUsuario("Empresa")
                                                .contrasena("sabores2024")
                                                .empresaFueAceptada(true)
                                                .empresaFueAceptadaPorAdmin(true)
                                                .empresaTuvoRespuesta(true)
                                                .build(),
                                Empresa.builder()
                                                .nombre("Pacífico Dive Tours")
                                                .razonSocial("Pacífico Dive Tours SAS")
                                                .nit("900112233-4")
                                                .sector("Turismo ecológico")
                                                .fechaFundacion("2012-11-02")
                                                .direccion("Malecón Bahía de Málaga")
                                                .ciudad("Buenaventura")
                                                .telefono("3127788990")
                                                .correo("reservas@pacificodive.co")
                                                .web("https://pacificodive.co")
                                                .numeroRegistroMercantil("456789123")
                                                .fechaRegistro("2024-02-28")
                                                .entidadRegistro("Cámara de Comercio de Buenaventura")
                                                .tipoSociedad("SAS")
                                                .nombreRepresentanteLegal("Carlos Mina")
                                                .numeroDocumentoRepresentanteLegal("1045678901")
                                                .cargoPropietario("Director de Operaciones")
                                                .nombrePropietarioPrincipal("Carlos Mina")
                                                .tipoUsuario("Empresa")
                                                .contrasena("pacifico456")
                                                .empresaFueAceptada(true)
                                                .empresaFueAceptadaPorAdmin(true)
                                                .empresaTuvoRespuesta(true)
                                                .build(),
                                Empresa.builder()
                                                .nombre("Llanos Eco Travel")
                                                .razonSocial("Llanos Eco Travel SAS")
                                                .nit("901556677-8")
                                                .sector("Turismo natural")
                                                .fechaFundacion("2016-06-30")
                                                .direccion("Km 5 Vía Restrepo")
                                                .ciudad("Villavicencio")
                                                .telefono("3108899001")
                                                .correo("experiencias@llanoseco.co")
                                                .web("https://llanoseco.co")
                                                .numeroRegistroMercantil("567891234")
                                                .fechaRegistro("2024-01-25")
                                                .entidadRegistro("Cámara de Comercio de Villavicencio")
                                                .tipoSociedad("SAS")
                                                .nombreRepresentanteLegal("José David Barreto")
                                                .numeroDocumentoRepresentanteLegal("1056789012")
                                                .cargoPropietario("Director General")
                                                .nombrePropietarioPrincipal("José David Barreto")
                                                .tipoUsuario("Empresa")
                                                .contrasena("llanos789")
                                                .empresaFueAceptada(true)
                                                .empresaFueAceptadaPorAdmin(true)
                                                .empresaTuvoRespuesta(true)
                                                .build());
        }

        private List<Cliente> clientesSeed() {
                return List.of(
                                Cliente.builder()
                                                .tipoUsuario("Cliente")
                                                .nombreCompleto("Laura González")
                                                .tipoDocumento("CC")
                                                .numeroDocumento("1020304051")
                                                .numeroTelefono("3001112233")
                                                .email("laura.gonzalez@gmail.com")
                                                .nombreUsuario("lauragonzalez")
                                                .contrasena("cliente123")
                                                .presupuesto(cop(2500000))
                                                .build(),
                                Cliente.builder()
                                                .tipoUsuario("Cliente")
                                                .nombreCompleto("Sebastián López")
                                                .tipoDocumento("CC")
                                                .numeroDocumento("1020304052")
                                                .numeroTelefono("3002223344")
                                                .email("sebastian.lopez@gmail.com")
                                                .nombreUsuario("sebastianlo")
                                                .contrasena("cliente123")
                                                .presupuesto(cop(1800000))
                                                .build(),
                                Cliente.builder()
                                                .tipoUsuario("Cliente")
                                                .nombreCompleto("Valeria Castillo")
                                                .tipoDocumento("CC")
                                                .numeroDocumento("1020304053")
                                                .numeroTelefono("3003334455")
                                                .email("valeria.castillo@gmail.com")
                                                .nombreUsuario("valeriac")
                                                .contrasena("cliente123")
                                                .presupuesto(cop(3200000))
                                                .build(),
                                Cliente.builder()
                                                .tipoUsuario("Cliente")
                                                .nombreCompleto("Andrés Martínez")
                                                .tipoDocumento("CC")
                                                .numeroDocumento("1020304054")
                                                .numeroTelefono("3004445566")
                                                .email("andres.martinez@gmail.com")
                                                .nombreUsuario("andresm")
                                                .contrasena("cliente123")
                                                .presupuesto(cop(2100000))
                                                .build(),
                                Cliente.builder()
                                                .tipoUsuario("Cliente")
                                                .nombreCompleto("Camila Ortiz")
                                                .tipoDocumento("CC")
                                                .numeroDocumento("1020304055")
                                                .numeroTelefono("3005556677")
                                                .email("camila.ortiz@gmail.com")
                                                .nombreUsuario("camilaortiz")
                                                .contrasena("cliente123")
                                                .presupuesto(cop(2700000))
                                                .build());
        }

        private Cliente adminSeed() {
                return Cliente.builder()
                                .tipoUsuario("Administrador")
                                .nombreCompleto("Administrador General")
                                .tipoDocumento("CC")
                                .numeroDocumento("9000000000")
                                .numeroTelefono("3009998888")
                                .email("admin@midestino.com")
                                .nombreUsuario("admin_midestino")
                                .contrasena("admin123")
                                .presupuesto(BigDecimal.ZERO)
                                .build();
        }

        private List<PlanSeed> planSeeds() {
                return List.of(
                                // Caribe Aventuras SAS - Cartagena
                                new PlanSeed("Caribe Aventuras SAS", "Exploración Murallas de Cartagena", "Cultural",
                                                "Centro Histórico, Baluarte San Ignacio", "08:00-12:00",
                                                "contacto@caribeaventuras.co",
                                                "Cartagena", "3101122233", cop(320000), 25, 20, 4.7,
                                                "Recorrido guiado por las murallas y fortalezas coloniales con degustación de dulces típicos.",
                                                "https://images.unsplash.com/photo-1548783307-f63adc82c6c8?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo, Transferencia", "2025-01-10"),
                                new PlanSeed("Caribe Aventuras SAS", "Chiva Rumbera Sunset", "Aventura",
                                                "Salida desde la Torre del Reloj", "17:00-21:00",
                                                "contacto@caribeaventuras.co",
                                                "Cartagena", "3101122244", cop(280000), 40, 35, 4.5,
                                                "Tour en chiva rumbera con música en vivo, bebidas y parada en miradores al atardecer.",
                                                "https://images.unsplash.com/photo-1518459031867-a89b944bffe4?auto=format&fit=crop&w=800&q=80",
                                                "Efectivo, Tarjeta", "2025-01-15"),
                                new PlanSeed("Caribe Aventuras SAS", "Snorkel en Islas del Rosario", "Aventura",
                                                "Muelle La Bodeguita", "07:00-16:00", "reservas@caribeaventuras.co",
                                                "Cartagena", "3101122255", cop(450000), 30, 20, 4.8,
                                                "Excursión en lancha rápida con equipo de snorkel, guía bilingüe y almuerzo típico.",
                                                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-01-20"),
                                new PlanSeed("Caribe Aventuras SAS", "Tour Gastronómico Caribe Fusión", "Gastronómico",
                                                "Barrio Getsemaní, Calle de la Sierpe", "18:00-22:00",
                                                "sabores@caribeaventuras.co",
                                                "Cartagena", "3101122266", cop(360000), 20, 16, 4.6,
                                                "Degustación de platos caribeños en restaurantes boutique con chef invitado.",
                                                "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-01-25"),
                                new PlanSeed("Caribe Aventuras SAS", "Kayak Nocturno en la Ciénaga", "Aventura",
                                                "Reserva Natural Ciénaga de la Virgen", "19:00-22:00",
                                                "aventura@caribeaventuras.co",
                                                "Cartagena", "3101122277", cop(310000), 18, 12, 4.4,
                                                "Remada nocturna para observar plancton bioluminiscente con guía experto.",
                                                "https://images.unsplash.com/photo-1476610182048-b716b8518aae?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-01-30"),
                                new PlanSeed("Caribe Aventuras SAS", "Isla Barú Day Pass Deluxe", "Relax",
                                                "Playa Blanca, Isla Barú", "08:00-18:00", "daypass@caribeaventuras.co",
                                                "Cartagena", "3101122288", cop(520000), 50, 40, 4.9,
                                                "Acceso a club de playa con menú premium, camas balinesas y transporte exclusivo.",
                                                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-02-02"),

                                // Andes Trekking Co - Medellín
                                new PlanSeed("Andes Trekking Co", "Caminata Piedra del Peñol 360°", "Aventura",
                                                "Guatapé, vereda El Peñol", "06:00-17:00", "hola@andestrekking.co",
                                                "Medellín", "3202211334", cop(340000), 25, 20, 4.7,
                                                "Ascenso guiado a la Piedra del Peñol con navegación en lago y almuerzo típico.",
                                                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-02-05"),
                                new PlanSeed("Andes Trekking Co", "Ruta Café y Aventura", "Cultural",
                                                "Finca El Aroma, Fredonia", "07:00-18:00", "contacto@andestrekking.co",
                                                "Medellín", "3202211445", cop(390000), 20, 16, 4.5,
                                                "Experiencia cafetera con tostión artesanal, canopy y degustación gourmet.",
                                                "https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-02-08"),
                                new PlanSeed("Andes Trekking Co", "Parapente San Félix Pro", "Aventura",
                                                "Mirador San Félix", "09:00-15:00", "vuelos@andestrekking.co",
                                                "Medellín", "3202211556", cop(280000), 18, 18, 4.6,
                                                "Vuelo en tándem con instructores certificados y cobertura fotográfica.",
                                                "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-02-12"),
                                new PlanSeed("Andes Trekking Co", "Cañoning Cascada Cristal", "Aventura",
                                                "San Rafael, Antioquia", "07:00-17:00", "aventura@andestrekking.co",
                                                "Medellín", "3202211667", cop(410000), 15, 12, 4.8,
                                                "Descenso por cascadas con cuerdas, equipo profesional y refrigerios.",
                                                "https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-02-15"),
                                new PlanSeed("Andes Trekking Co", "Tour Pueblos Patrimonio", "Cultural",
                                                "Santa Fe de Antioquia y Jardín", "06:00-20:00",
                                                "patrimonio@andestrekking.co",
                                                "Medellín", "3202211778", cop(360000), 30, 25, 4.4,
                                                "Visita guiada a pueblos coloniales con cata de dulces y fotografía profesional.",
                                                "https://images.unsplash.com/photo-1523419409543-0c1df022bdd1?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-02-20"),
                                new PlanSeed("Andes Trekking Co", "Senderismo Nocturno Arví", "Aventura",
                                                "Parque Arví, entrada Palos Verdes", "18:00-23:00",
                                                "nocturno@andestrekking.co",
                                                "Medellín", "3202211889", cop(260000), 20, 18, 4.3,
                                                "Recorrido interpretativo nocturno con observación de luciérnagas y fogata.",
                                                "https://images.unsplash.com/photo-1476041800956-2c5c97736a02?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-02-24"),

                                // Sabores Urbanos LTDA - Bogotá
                                new PlanSeed("Sabores Urbanos LTDA", "Street Food La Candelaria", "Gastronómico",
                                                "Plaza del Chorro de Quevedo", "16:00-20:00", "info@saboresurbanos.co",
                                                "Bogotá", "3136677881", cop(220000), 20, 18, 4.5,
                                                "Ruta por puestos tradicionales con historias locales y bebidas artesanales.",
                                                "https://images.unsplash.com/photo-1514996937319-344454492b37?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-03-01"),
                                new PlanSeed("Sabores Urbanos LTDA", "Experiencia Cervecera Artesanal", "Gastronómico",
                                                "Zona Chapinero, Calle 64", "18:00-22:00", "cerveza@saboresurbanos.co",
                                                "Bogotá", "3136677992", cop(240000), 16, 14, 4.4,
                                                "Degustación guiada en microcervecerías con maridaje de tapas bogotanas.",
                                                "https://images.unsplash.com/photo-1523475472560-d2df97ec485c?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-04"),
                                new PlanSeed("Sabores Urbanos LTDA", "Arte Urbano Chapinero", "Cultural",
                                                "Carrera 13 con Calle 53", "10:00-14:00", "arte@saboresurbanos.co",
                                                "Bogotá", "3136677003", cop(180000), 18, 15, 4.6,
                                                "Tour guiado por murales y colectivos artísticos con taller de stencil.",
                                                "https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-03-07"),
                                new PlanSeed("Sabores Urbanos LTDA", "Picnic Gourmet Simón Bolívar", "Relax",
                                                "Parque Simón Bolívar, zona lago", "11:00-15:00",
                                                "picnic@saboresurbanos.co",
                                                "Bogotá", "3136677114", cop(260000), 22, 20, 4.3,
                                                "Montaje de picnic con chef personal, actividades recreativas y música en vivo.",
                                                "https://images.unsplash.com/photo-1499636136210-6f4ee915583e?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-10"),
                                new PlanSeed("Sabores Urbanos LTDA", "Taller Ceviche Nikkei", "Gastronómico",
                                                "Estudio Gastronómico Zona G", "15:00-18:00",
                                                "talleres@saboresurbanos.co",
                                                "Bogotá", "3136677225", cop(280000), 14, 12, 4.7,
                                                "Clase práctica con chef invitado y recetario digital incluido.",
                                                "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-14"),
                                new PlanSeed("Sabores Urbanos LTDA", "Brunch Fusión Dominguero", "Gastronómico",
                                                "Restaurante Rooftop Centro", "10:00-13:00",
                                                "reservas@saboresurbanos.co",
                                                "Bogotá", "3136677336", cop(240000), 24, 20, 4.2,
                                                "Brunch temático con estación de mimosas, música en vivo y mercado artesanal.",
                                                "https://images.unsplash.com/photo-1540181940-32422a8dba0d?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-03-17"),

                                // Pacífico Dive Tours - Buenaventura
                                new PlanSeed("Pacífico Dive Tours", "Buceo Avistamiento de Ballenas", "Aventura",
                                                "Bahía de Málaga, plataforma principal", "07:00-15:00",
                                                "reservas@pacificodive.co",
                                                "Buenaventura", "3128899002", cop(620000), 18, 12, 4.9,
                                                "Salida en bote con instructores PADI, equipos incluidos y registro fotográfico.",
                                                "https://images.unsplash.com/photo-1518834107812-67b0b7c58434?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-20"),
                                new PlanSeed("Pacífico Dive Tours", "Snorkel Manglares La Barra", "Aventura",
                                                "Comunidad La Barra, muelle principal", "08:00-16:00",
                                                "info@pacificodive.co",
                                                "Buenaventura", "3128899113", cop(380000), 20, 16, 4.6,
                                                "Exploración de manglares con guía comunitario y almuerzo afrodisíaco.",
                                                "https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-03-23"),
                                new PlanSeed("Pacífico Dive Tours", "Kayak Bahía de Málaga", "Aventura",
                                                "Centro de visitantes Bahía de Málaga", "09:00-13:00",
                                                "aventura@pacificodive.co",
                                                "Buenaventura", "3128899224", cop(290000), 16, 12, 4.4,
                                                "Recorrido en kayak por esteros y cuevas con interpretación ambiental.",
                                                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-26"),
                                new PlanSeed("Pacífico Dive Tours", "Expedición Cascadas Agua Clara", "Aventura",
                                                "Reserva San Cipriano", "07:00-18:00", "expediciones@pacificodive.co",
                                                "Buenaventura", "3128899335", cop(410000), 22, 18, 4.5,
                                                "Travesía en brujitas, caminata ecológica y descenso en cascadas cristalinas.",
                                                "https://images.unsplash.com/photo-1470777630575-5fd2743d11d7?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-03-29"),
                                new PlanSeed("Pacífico Dive Tours", "Pesca Artesanal Comunitaria", "Cultural",
                                                "Isla Juanchaco", "05:00-12:00", "comunidad@pacificodive.co",
                                                "Buenaventura", "3128899446", cop(260000), 15, 12, 4.3,
                                                "Jornada de pesca tradicional con cocción del pescado y relatos locales.",
                                                "https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-04-01"),
                                new PlanSeed("Pacífico Dive Tours", "Tour Gastronómico Pacífico", "Gastronómico",
                                                "Malecón de Buenaventura", "12:00-16:00", "sabores@pacificodive.co",
                                                "Buenaventura", "3128899557", cop(240000), 24, 20, 4.2,
                                                "Recorrido por cocinas tradicionales con música del Pacífico en vivo.",
                                                "https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-04-04"),

                                // Llanos Eco Travel - Villavicencio
                                new PlanSeed("Llanos Eco Travel", "Safari Llanero Sunrise", "Aventura",
                                                "Hato La Aurora", "05:00-13:00", "experiencias@llanoseco.co",
                                                "Villavicencio", "3109900112", cop(450000), 20, 16, 4.8,
                                                "Recorrido en camioneta 4x4 con avistamiento de fauna y desayuno típico.",
                                                "https://images.unsplash.com/photo-1437957146754-f6377debe171?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-04-07"),
                                new PlanSeed("Llanos Eco Travel", "Cabalgata Río Meta", "Aventura",
                                                "Vereda Puerto López", "08:00-17:00", "cabalgatas@llanoseco.co",
                                                "Villavicencio", "3109900223", cop(320000), 18, 15, 4.5,
                                                "Cabalgata guiada con almuerzo criollo y baño en el río Meta.",
                                                "https://images.unsplash.com/photo-1512302940121-5dda7724fd87?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-04-10"),
                                new PlanSeed("Llanos Eco Travel", "Ruta del Cacao Experiencial", "Cultural",
                                                "Finca Altamira, Restrepo", "09:00-16:00", "cacao@llanoseco.co",
                                                "Villavicencio", "3109900334", cop(280000), 16, 14, 4.4,
                                                "Visita a cultivo de cacao fino, cata sensorial y elaboración de bombones.",
                                                "https://images.unsplash.com/photo-1542528180-0d5174dac283?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-04-13"),
                                new PlanSeed("Llanos Eco Travel", "Glamping Estrellas del Llano", "Relax",
                                                "Mirador Piedra del Amor", "15:00-11:00", "glamping@llanoseco.co",
                                                "Villavicencio", "3109900445", cop(580000), 12, 10, 4.9,
                                                "Alojamiento en domos geodésicos con cena gourmet y observación astronómica.",
                                                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-04-17"),
                                new PlanSeed("Llanos Eco Travel", "Avistamiento Aves Humedales", "Aventura",
                                                "Reserva Humedal Coroncoro", "06:00-12:00", "aves@llanoseco.co",
                                                "Villavicencio", "3109900556", cop(260000), 20, 16, 4.3,
                                                "Caminata interpretativa con biólogo y préstamo de binoculares profesionales.",
                                                "https://images.unsplash.com/photo-1526662092594-e98c1e356d6b?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Transferencia", "2025-04-20"),
                                new PlanSeed("Llanos Eco Travel", "Parrando Llanero Tradicional", "Cultural",
                                                "Hacienda El Ceibo", "17:00-23:00", "parrando@llanoseco.co",
                                                "Villavicencio", "3109900667", cop(300000), 35, 30, 4.6,
                                                "Jornada festiva con coleo demostrativo, baile joropo y buffet llanero.",
                                                "https://images.unsplash.com/photo-1471295253337-3ceaaedca402?auto=format&fit=crop&w=800&q=80",
                                                "Tarjeta, Efectivo", "2025-04-24"));
        }

        private static BigDecimal cop(long value) {
                return BigDecimal.valueOf(value);
        }

        private record PlanSeed(
                        String empresaNombre,
                        String nombre,
                        String tipoSitio,
                        String direccion,
                        String horario,
                        String email,
                        String ciudad,
                        String telefono,
                        BigDecimal precio,
                        int cantidadDisponible,
                        int personasDisponibles,
                        double valoracionPromedio,
                        String informacionGeneral,
                        String imagenUrl,
                        String metodosPago,
                        String fechaRegistro) {

                public BigDecimal precio() {
                        return Objects.requireNonNullElse(precio, BigDecimal.ZERO);
                }

                public double valoracionPromedio() {
                        return valoracionPromedio;
                }
        }
}
