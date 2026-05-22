-- ============================================================
-- midestinobd — Script completo con datos limpios
-- Versión 2.0 — 2026-05-21
-- ¡ATENCIÓN! Reemplaza TODOS los datos existentes.
-- Prerequisito: schema ya creado (ejecutar my trip 1.sql primero).
-- ============================================================

SET NAMES utf8mb4;
USE midestinobd;

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- PASO 1: LIMPIAR TABLAS (orden inverso de dependencias FK)
-- ============================================================
TRUNCATE TABLE compra_plan_cantidades_compradas;
TRUNCATE TABLE compra_plan_ciudades;
TRUNCATE TABLE compra_plan_correos;
TRUNCATE TABLE compra_plan_direcciones;
TRUNCATE TABLE compra_plan_empresas;
TRUNCATE TABLE compra_plan_horarios;
TRUNCATE TABLE compra_plan_imagenes;
TRUNCATE TABLE compra_plan_informaciones_generales;
TRUNCATE TABLE compra_plan_nombre_planes;
TRUNCATE TABLE compra_plan_por_empresas;
TRUNCATE TABLE compra_plan_precios;
TRUNCATE TABLE compra_plan_telefonos;
TRUNCATE TABLE compra_plan_tipo_sitios;
TRUNCATE TABLE carritos_compra_planes;
TRUNCATE TABLE compra_planes;
TRUNCATE TABLE valoraciones;
TRUNCATE TABLE planes_empresas;
TRUNCATE TABLE imagenes;
TRUNCATE TABLE clientes;
TRUNCATE TABLE empresas;

-- ============================================================
-- PASO 2: EMPRESAS (5 operativas + 1 de prueba)
-- Orden columnas: id_empresa, rut, cargo_propietario,
--   certificado_existencia, ciudad, confirmación, contrasena,
--   correo, direccion, empresa_fue_aceptada,
--   empresa_fue_aceptada_por_admin, empresa_tuvo_respuesta,
--   entidad_registro, estados_financieros, fecha_firma,
--   fecha_fundacion, fecha_registro, firma_representante_legal,
--   foto_perfil, ganancias, nit, nombre,
--   nombre_propietario_principal, nombre_representante_legal,
--   notaria_registro, numero_documento_representante_legal,
--   numero_registro_mercantil, otros_documentos_legales,
--   razon_social, sector, telefono, tipo_sociedad,
--   tipo_usuario, web
-- ============================================================
INSERT INTO `empresas` VALUES
(1, NULL, NULL, NULL, 'Cartagena',
 b'0', 'caribe123', 'contacto@caribeaventuras.co',
 'Cra 2 #5-45, Centro Histórico',
 b'1', b'1', b'1',
 'Cámara de Comercio de Cartagena', NULL, NULL,
 '2010-05-12', '2024-01-15', NULL, NULL, NULL,
 '901234567-1', 'Caribe Aventuras SAS',
 'Valentina Herrera', 'Valentina Herrera',
 NULL, '1020304050', '123456789', NULL,
 'Caribe Aventuras SAS', 'Turismo',
 '3001234567', 'SAS', 'Empresa', 'https://caribeaventuras.co'),

(2, NULL, NULL, NULL, 'Medellín',
 b'0', 'andes321', 'hola@andestrekking.co',
 'Calle 10 #43B-30',
 b'1', b'1', b'1',
 'Cámara de Comercio de Medellín', NULL, NULL,
 '2013-08-19', '2024-02-10', NULL, NULL, NULL,
 '901987654-2', 'Andes Trekking Co',
 'Juan Esteban Ríos', 'Juan Esteban Ríos',
 NULL, '1030507090', '234567891', NULL,
 'Andes Trekking Co SAS', 'Turismo de aventura',
 '3009876543', 'SAS', 'Empresa', 'https://andestrekking.co'),

(3, NULL, NULL, NULL, 'Bogotá',
 b'0', 'sabores2024', 'info@saboresurbanos.co',
 'Carrera 7 #60-45',
 b'1', b'1', b'1',
 'Cámara de Comercio de Bogotá', NULL, NULL,
 '2015-03-22', '2024-03-05', NULL, NULL, NULL,
 '900654321-7', 'Sabores Urbanos LTDA',
 'Mariana Torres', 'Mariana Torres',
 NULL, '1012345678', '345678912', NULL,
 'Sabores Urbanos LTDA', 'Gastronomía y cultura',
 '3115556677', 'LTDA', 'Empresa', 'https://saboresurbanos.co'),

(4, NULL, NULL, NULL, 'Buenaventura',
 b'0', 'pacifico456', 'reservas@pacificodive.co',
 'Malecón Bahía de Málaga',
 b'1', b'1', b'1',
 'Cámara de Comercio de Buenaventura', NULL, NULL,
 '2012-11-02', '2024-02-28', NULL, NULL, NULL,
 '900112233-4', 'Pacífico Dive Tours',
 'Carlos Mina', 'Carlos Mina',
 NULL, '1045678901', '456789123', NULL,
 'Pacífico Dive Tours SAS', 'Turismo ecológico',
 '3127788990', 'SAS', 'Empresa', 'https://pacificodive.co'),

(5, NULL, NULL, NULL, 'Villavicencio',
 b'0', 'llanos789', 'experiencias@llanoseco.co',
 'Km 5 Vía Restrepo',
 b'1', b'1', b'1',
 'Cámara de Comercio de Villavicencio', NULL, NULL,
 '2016-06-30', '2024-01-25', NULL, NULL, NULL,
 '901556677-8', 'Llanos Eco Travel',
 'José David Barreto', 'José David Barreto',
 NULL, '1056789012', '567891234', NULL,
 'Llanos Eco Travel SAS', 'Turismo natural',
 '3108899001', 'SAS', 'Empresa', 'https://llanoseco.co'),

(6, NULL, '', '', 'Cartagena de indias',
 b'1', '1234567890', 'contacto@ejemplo.com',
 'Torices',
 b'1', b'1', b'1',
 'cdsfdsfdsfds', '', '2025-11-04', '2025-10-26', '2025-10-26',
 'ddasdffffg', NULL, 0.00,
 '4535353', 'sdffdsfdsfdsdsf', '', '', '', '',
 '3455435', '',
 'sdfdsfdfs', 'fsdfdsfdsfds', '1234567890', 'dfsfdsfdsfds',
 'Empresa', 'fsdsfdsfd');

-- ============================================================
-- PASO 3: CLIENTES (5 clientes + 1 administrador)
-- Orden columnas: id_cliente, contrasena, email, foto_perfil,
--   nombre_completo, nombre_usuario, numero_documento,
--   numero_telefono, presupuesto, tipo_documento, tipo_usuario
-- ============================================================
INSERT INTO `clientes` VALUES
(1, 'cliente123', 'laura.gonzalez@gmail.com', NULL,
 'Laura González', 'lauragonzalez',
 '1020304051', '3001112233', 2500000.00, 'CC', 'Cliente'),

(2, 'cliente123', 'sebastian.lopez@gmail.com', NULL,
 'Sebastián López', 'sebastianlo',
 '1020304052', '3002223344', 1800000.00, 'CC', 'Cliente'),

(3, 'cliente123', 'valeria.castillo@gmail.com', NULL,
 'Valeria Castillo', 'valeriac',
 '1020304053', '3003334455', 3200000.00, 'CC', 'Cliente'),

(4, 'cliente123', 'andres.martinez@gmail.com', NULL,
 'Andrés Martínez', 'andresm',
 '1020304054', '3004445566', 2100000.00, 'CC', 'Cliente'),

(5, 'cliente123', 'camila.ortiz@gmail.com', NULL,
 'Camila Ortiz', 'camilaortiz',
 '1020304055', '3005556677', 2700000.00, 'CC', 'Cliente'),

(6, 'admin123', 'admin@midestino.com', NULL,
 'Administrador General', 'admin_midestino',
 '9000000000', '3009998888', 0.00, 'CC', 'Administrador');

-- ============================================================
-- PASO 4: PLANES DE EMPRESAS (30 reales + 1 de prueba)
-- Orden columnas: id, cantidad_disponible, ciudad, direccion,
--   disponible, email, fecha_registro, horario, imagen_url,
--   informacion_general, metodo_pago, nombre,
--   personas_disponibles, precio, telefono, tipo_sitio,
--   valoracion_promedio, id_empresa
-- valoracion_promedio = NULL (se calcula al final)
-- ============================================================
INSERT INTO `planes_empresas` VALUES

-- === Caribe Aventuras SAS (empresa 1) — Cartagena ===
(1, 25, 'Cartagena', 'Centro Histórico, Baluarte San Ignacio',
 b'1', 'contacto@caribeaventuras.co', '2025-01-10', '08:00-12:00',
 'https://images.unsplash.com/photo-1548783307-f63adc82c6c8?auto=format&fit=crop&w=800&q=80',
 'Recorrido guiado por las murallas y fortalezas coloniales con degustación de dulces típicos.',
 'Tarjeta, Efectivo, Transferencia',
 'Exploración Murallas de Cartagena',
 20, 320000.00, '3101122233', 'Cultural', NULL, 1),

(2, 40, 'Cartagena', 'Salida desde la Torre del Reloj',
 b'1', 'contacto@caribeaventuras.co', '2025-01-15', '17:00-21:00',
 'https://images.unsplash.com/photo-1518459031867-a89b944bffe4?auto=format&fit=crop&w=800&q=80',
 'Tour en chiva rumbera con música en vivo, bebidas y parada en miradores al atardecer.',
 'Efectivo, Tarjeta',
 'Chiva Rumbera Sunset',
 35, 280000.00, '3101122244', 'Aventura', NULL, 1),

(3, 30, 'Cartagena', 'Muelle La Bodeguita',
 b'1', 'reservas@caribeaventuras.co', '2025-01-20', '07:00-16:00',
 'https://images.unsplash.com/photo-1507525428034-b723cf961d3e?auto=format&fit=crop&w=800&q=80',
 'Excursión en lancha rápida con equipo de snorkel, guía bilingüe y almuerzo típico.',
 'Tarjeta, Transferencia',
 'Snorkel en Islas del Rosario',
 20, 450000.00, '3101122255', 'Aventura', NULL, 1),

(4, 20, 'Cartagena', 'Barrio Getsemaní, Calle de la Sierpe',
 b'1', 'sabores@caribeaventuras.co', '2025-01-25', '18:00-22:00',
 'https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?auto=format&fit=crop&w=800&q=80',
 'Degustación de platos caribeños en restaurantes boutique con chef invitado.',
 'Tarjeta, Efectivo',
 'Tour Gastronómico Caribe Fusión',
 16, 360000.00, '3101122266', 'Gastronómico', NULL, 1),

(5, 18, 'Cartagena', 'Reserva Natural Ciénaga de la Virgen',
 b'1', 'aventura@caribeaventuras.co', '2025-01-30', '19:00-22:00',
 'https://images.unsplash.com/photo-1476610182048-b716b8518aae?auto=format&fit=crop&w=800&q=80',
 'Remada nocturna para observar plancton bioluminiscente con guía experto.',
 'Tarjeta, Transferencia',
 'Kayak Nocturno en la Ciénaga',
 12, 310000.00, '3101122277', 'Aventura', NULL, 1),

(6, 50, 'Cartagena', 'Playa Blanca, Isla Barú',
 b'1', 'daypass@caribeaventuras.co', '2025-02-02', '08:00-18:00',
 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80',
 'Acceso a club de playa con menú premium, camas balinesas y transporte exclusivo.',
 'Tarjeta, Transferencia',
 'Isla Barú Day Pass Deluxe',
 40, 520000.00, '3101122288', 'Relax', NULL, 1),

-- === Andes Trekking Co (empresa 2) — Medellín ===
(7, 25, 'Medellín', 'Guatapé, vereda El Peñol',
 b'1', 'hola@andestrekking.co', '2025-02-05', '06:00-17:00',
 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80',
 'Ascenso guiado a la Piedra del Peñol con navegación en lago y almuerzo típico.',
 'Tarjeta, Transferencia',
 'Caminata Piedra del Peñol 360°',
 20, 340000.00, '3202211334', 'Aventura', NULL, 2),

(8, 20, 'Medellín', 'Finca El Aroma, Fredonia',
 b'1', 'contacto@andestrekking.co', '2025-02-08', '07:00-18:00',
 'https://images.unsplash.com/photo-1470770841072-f978cf4d019e?auto=format&fit=crop&w=800&q=80',
 'Experiencia cafetera con tostión artesanal, canopy y degustación gourmet.',
 'Tarjeta, Efectivo',
 'Ruta Café y Aventura',
 16, 390000.00, '3202211445', 'Cultural', NULL, 2),

(9, 18, 'Medellín', 'Mirador San Félix',
 b'1', 'vuelos@andestrekking.co', '2025-02-12', '09:00-15:00',
 'https://images.unsplash.com/photo-1441974231531-c6227db76b6e?auto=format&fit=crop&w=800&q=80',
 'Vuelo en tándem con instructores certificados y cobertura fotográfica.',
 'Tarjeta, Efectivo',
 'Parapente San Félix Pro',
 18, 280000.00, '3202211556', 'Aventura', NULL, 2),

(10, 15, 'Medellín', 'San Rafael, Antioquia',
 b'1', 'aventura@andestrekking.co', '2025-02-15', '07:00-17:00',
 'https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80',
 'Descenso por cascadas con cuerdas, equipo profesional y refrigerios.',
 'Tarjeta, Transferencia',
 'Cañoning Cascada Cristal',
 12, 410000.00, '3202211667', 'Aventura', NULL, 2),

(11, 30, 'Medellín', 'Santa Fe de Antioquia y Jardín',
 b'1', 'patrimonio@andestrekking.co', '2025-02-20', '06:00-20:00',
 'https://images.unsplash.com/photo-1523419409543-0c1df022bdd1?auto=format&fit=crop&w=800&q=80',
 'Visita guiada a pueblos coloniales con cata de dulces y fotografía profesional.',
 'Tarjeta, Efectivo',
 'Tour Pueblos Patrimonio',
 25, 360000.00, '3202211778', 'Cultural', NULL, 2),

(12, 20, 'Medellín', 'Parque Arví, entrada Palos Verdes',
 b'1', 'nocturno@andestrekking.co', '2025-02-24', '18:00-23:00',
 'https://images.unsplash.com/photo-1476041800956-2c5c97736a02?auto=format&fit=crop&w=800&q=80',
 'Recorrido interpretativo nocturno con observación de luciérnagas y fogata.',
 'Tarjeta, Transferencia',
 'Senderismo Nocturno Arví',
 18, 260000.00, '3202211889', 'Aventura', NULL, 2),

-- === Sabores Urbanos LTDA (empresa 3) — Bogotá ===
(13, 20, 'Bogotá', 'Plaza del Chorro de Quevedo',
 b'1', 'info@saboresurbanos.co', '2025-03-01', '16:00-20:00',
 'https://images.unsplash.com/photo-1514996937319-344454492b37?auto=format&fit=crop&w=800&q=80',
 'Ruta por puestos tradicionales con historias locales y bebidas artesanales.',
 'Tarjeta, Efectivo',
 'Street Food La Candelaria',
 18, 220000.00, '3136677881', 'Gastronómico', NULL, 3),

(14, 16, 'Bogotá', 'Zona Chapinero, Calle 64',
 b'1', 'cerveza@saboresurbanos.co', '2025-03-04', '18:00-22:00',
 'https://images.unsplash.com/photo-1523475472560-d2df97ec485c?auto=format&fit=crop&w=800&q=80',
 'Degustación guiada en microcervecerías con maridaje de tapas bogotanas.',
 'Tarjeta, Transferencia',
 'Experiencia Cervecera Artesanal',
 14, 240000.00, '3136677992', 'Gastronómico', NULL, 3),

(15, 18, 'Bogotá', 'Carrera 13 con Calle 53',
 b'1', 'arte@saboresurbanos.co', '2025-03-07', '10:00-14:00',
 'https://images.unsplash.com/photo-1487412720507-e7ab37603c6f?auto=format&fit=crop&w=800&q=80',
 'Tour guiado por murales y colectivos artísticos con taller de stencil.',
 'Tarjeta, Efectivo',
 'Arte Urbano Chapinero',
 15, 180000.00, '3136677003', 'Cultural', NULL, 3),

(16, 22, 'Bogotá', 'Parque Simón Bolívar, zona lago',
 b'1', 'picnic@saboresurbanos.co', '2025-03-10', '11:00-15:00',
 'https://images.unsplash.com/photo-1499636136210-6f4ee915583e?auto=format&fit=crop&w=800&q=80',
 'Montaje de picnic con chef personal, actividades recreativas y música en vivo.',
 'Tarjeta, Transferencia',
 'Picnic Gourmet Simón Bolívar',
 20, 260000.00, '3136677114', 'Relax', NULL, 3),

(17, 14, 'Bogotá', 'Estudio Gastronómico Zona G',
 b'1', 'talleres@saboresurbanos.co', '2025-03-14', '15:00-18:00',
 'https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=800&q=80',
 'Clase práctica con chef invitado y recetario digital incluido.',
 'Tarjeta, Transferencia',
 'Taller Ceviche Nikkei',
 12, 280000.00, '3136677225', 'Gastronómico', NULL, 3),

(18, 24, 'Bogotá', 'Restaurante Rooftop Centro',
 b'1', 'reservas@saboresurbanos.co', '2025-03-17', '10:00-13:00',
 'https://images.unsplash.com/photo-1540181940-32422a8dba0d?auto=format&fit=crop&w=800&q=80',
 'Brunch temático con estación de mimosas, música en vivo y mercado artesanal.',
 'Tarjeta, Efectivo',
 'Brunch Fusión Dominguero',
 20, 240000.00, '3136677336', 'Gastronómico', NULL, 3),

-- === Pacífico Dive Tours (empresa 4) — Buenaventura / Neiva / Cali ===
(19, 18, 'Buenaventura', 'Bahía de Málaga, plataforma principal',
 b'1', 'reservas@pacificodive.co', '2025-03-20', '07:00-15:00',
 'https://images.unsplash.com/photo-1518834107812-67b0b7c58434?auto=format&fit=crop&w=800&q=80',
 'Salida en bote con instructores PADI, equipos incluidos y registro fotográfico.',
 'Tarjeta, Transferencia',
 'Buceo Avistamiento de Ballenas',
 12, 620000.00, '3128899002', 'Aventura', NULL, 4),

(20, 20, 'Buenaventura', 'Comunidad La Barra, muelle principal',
 b'1', 'info@pacificodive.co', '2025-03-23', '08:00-16:00',
 'https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80',
 'Exploración de manglares con guía comunitario y almuerzo afrodisíaco.',
 'Tarjeta, Efectivo',
 'Snorkel Manglares La Barra',
 16, 380000.00, '3128899113', 'Aventura', NULL, 4),

(21, 16, 'Buenaventura', 'Centro de visitantes Bahía de Málaga',
 b'1', 'aventura@pacificodive.co', '2025-03-26', '09:00-13:00',
 'https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80',
 'Recorrido en kayak por esteros y cuevas con interpretación ambiental.',
 'Tarjeta, Transferencia',
 'Kayak Bahía de Málaga',
 12, 290000.00, '3128899224', 'Aventura', NULL, 4),

(22, 22, 'Neiva', 'Reserva San Cipriano',
 b'1', 'expediciones@pacificodive.co', '2025-03-29', '07:00-18:00',
 'https://images.unsplash.com/photo-1470777630575-5fd2743d11d7?auto=format&fit=crop&w=800&q=80',
 'Travesía en brujitas, caminata ecológica y descenso en cascadas cristalinas.',
 'Tarjeta, Transferencia',
 'Expedición Cascadas Agua Clara',
 18, 410000.00, '3128899335', 'Aventura', NULL, 4),

(23, 15, 'Neiva', 'Isla Juanchaco',
 b'1', 'comunidad@pacificodive.co', '2025-04-01', '05:00-12:00',
 'https://images.unsplash.com/photo-1526481280695-3c4691bc66f0?auto=format&fit=crop&w=800&q=80',
 'Jornada de pesca tradicional con cocción del pescado y relatos locales.',
 'Tarjeta, Efectivo',
 'Pesca Artesanal Comunitaria',
 12, 260000.00, '3128899446', 'Cultural', NULL, 4),

(24, 24, 'Cali', 'Malecón de Buenaventura',
 b'1', 'sabores@pacificodive.co', '2025-04-04', '12:00-16:00',
 'https://images.unsplash.com/photo-1466978913421-dad2ebd01d17?auto=format&fit=crop&w=800&q=80',
 'Recorrido por cocinas tradicionales con música del Pacífico en vivo.',
 'Tarjeta, Efectivo',
 'Tour Gastronómico Pacífico',
 20, 240000.00, '3128899557', 'Gastronómico', NULL, 4),

-- === Llanos Eco Travel (empresa 5) — Cali / Santa Marta / Barranquilla ===
(25, 20, 'Cali', 'Hato La Aurora',
 b'1', 'experiencias@llanoseco.co', '2025-04-07', '05:00-13:00',
 'https://images.unsplash.com/photo-1437957146754-f6377debe171?auto=format&fit=crop&w=800&q=80',
 'Recorrido en camioneta 4x4 con avistamiento de fauna y desayuno típico.',
 'Tarjeta, Transferencia',
 'Safari Llanero Sunrise',
 16, 450000.00, '3109900112', 'Aventura', NULL, 5),

(26, 18, 'Cali', 'Vereda Puerto López',
 b'1', 'cabalgatas@llanoseco.co', '2025-04-10', '08:00-17:00',
 'https://images.unsplash.com/photo-1512302940121-5dda7724fd87?auto=format&fit=crop&w=800&q=80',
 'Cabalgata guiada con almuerzo criollo y baño en el río Meta.',
 'Tarjeta, Efectivo',
 'Cabalgata Río Meta',
 15, 320000.00, '3109900223', 'Aventura', NULL, 5),

(27, 16, 'Santa marta', 'Finca Altamira, Restrepo',
 b'1', 'cacao@llanoseco.co', '2025-04-13', '09:00-16:00',
 'https://images.unsplash.com/photo-1542528180-0d5174dac283?auto=format&fit=crop&w=800&q=80',
 'Visita a cultivo de cacao fino, cata sensorial y elaboración de bombones.',
 'Tarjeta, Transferencia',
 'Ruta del Cacao Experiencial',
 14, 280000.00, '3109900334', 'Cultural', NULL, 5),

(28, 12, 'Santa marta', 'Mirador Piedra del Amor',
 b'1', 'glamping@llanoseco.co', '2025-04-17', '15:00-11:00',
 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=800&q=80',
 'Alojamiento en domos geodésicos con cena gourmet y observación astronómica.',
 'Tarjeta, Transferencia',
 'Glamping Estrellas del Llano',
 10, 580000.00, '3109900445', 'Relax', NULL, 5),

(29, 20, 'Barranquilla', 'Reserva Humedal Coroncoro',
 b'1', 'aves@llanoseco.co', '2025-04-20', '06:00-12:00',
 'https://images.unsplash.com/photo-1526662092594-e98c1e356d6b?auto=format&fit=crop&w=800&q=80',
 'Caminata interpretativa con biólogo y préstamo de binoculares profesionales.',
 'Tarjeta, Transferencia',
 'Avistamiento Aves Humedales',
 16, 260000.00, '3109900556', 'Aventura', NULL, 5),

(30, 35, 'Barranquilla', 'Hacienda El Ceibo',
 b'1', 'parrando@llanoseco.co', '2025-04-24', '17:00-23:00',
 'https://images.unsplash.com/photo-1471295253337-3ceaaedca402?auto=format&fit=crop&w=800&q=80',
 'Jornada festiva con coleo demostrativo, baile joropo y buffet llanero.',
 'Tarjeta, Efectivo',
 'Parrando Llanero Tradicional',
 30, 300000.00, '3109900667', 'Cultural', NULL, 5),

-- === Plan de prueba (empresa 6) ===
(31, 24, 'Tunja', 'Casa',
 b'0', 'isaac@falso.com', '2025-11-1', '9:00 AM - 10:00 PM',
 'http://localhost:9999/api/planes/1/imagen',
 'ffsdfds', 'cualquier forma', 'aaaaaaaaa',
 44, 566675.00, '1234567890', 'Fiesta', NULL, 6);

-- ============================================================
-- PASO 5: VALORACIONES (30 registros para filtrado colaborativo)
-- Orden columnas: id, comentario, fecha, puntuacion,
--   cliente_id, plan_empresa_id
-- ============================================================
INSERT INTO `valoraciones` (`comentario`, `fecha`, `puntuacion`, `cliente_id`, `plan_empresa_id`) VALUES

-- Cliente 1 (Laura González) — cultural, gastronómico, relax
('El tour gastronómico caribeño fue una experiencia de sabores increíble',      '2025-11-05 14:30:00', 5, 1, 4),
('Isla Barú Day Pass es el paraíso en Colombia, servicio premium impecable',    '2025-11-10 10:15:00', 5, 1, 6),
('El street food en La Candelaria tiene mucha historia y sabor auténtico',      '2025-11-15 18:45:00', 4, 1, 13),
('El picnic gourmet en el parque fue romántico y delicioso',                    '2025-11-20 12:00:00', 5, 1, 16),
('El tour de arte urbano en Chapinero es diferente y muy cultural',             '2025-11-22 13:00:00', 4, 1, 15),

-- Cliente 2 (Sebastián López) — aventura y naturaleza
('La chiva rumbera es diversión pura, música y rumba asegurada',                '2025-11-06 20:00:00', 4, 2, 2),
('El snorkel en las Islas del Rosario fue alucinante, aguas cristalinas',       '2025-11-11 16:30:00', 5, 2, 3),
('La Piedra del Peñol es impresionante, guías muy bien preparados',             '2025-11-16 17:00:00', 4, 2, 7),
('El parapente en San Félix fue la experiencia más emocionante de mi vida',     '2025-11-21 11:00:00', 5, 2, 9),
('El senderismo nocturno en Arví y las luciérnagas fueron mágicos',             '2025-11-25 22:00:00', 4, 2, 12),
('El cañoning en Cascada Cristal fue intenso pero absolutamente increíble',     '2025-11-28 09:00:00', 5, 2, 10),

-- Cliente 3 (Valeria Castillo) — aventura, relax, gastronomía premium
('El snorkel en los manglares es una experiencia diferente a cualquier otra',   '2025-11-07 09:00:00', 4, 3, 3),
('La ruta del café y aventura en Fredonia es un must-do en Antioquia',          '2025-11-12 17:30:00', 5, 3, 8),
('Ver ballenas jorobadas fue un momento que nunca olvidaré en la vida',         '2025-11-22 08:30:00', 5, 3, 19),
('El safari llanero al amanecer es absolutamente espectacular',                 '2025-11-26 06:30:00', 5, 3, 25),
('La ruta del cacao y los bombones artesanales superaron todas mis expectativas','2025-11-28 14:00:00', 4, 3, 27),
('El glamping bajo las estrellas del llano es magia pura',                      '2025-11-30 08:00:00', 5, 3, 28),

-- Cliente 4 (Andrés Martínez) — aventura, cultural, ecológico
('El kayak nocturno en la Ciénaga para ver el plancton fue único',              '2025-11-08 20:30:00', 4, 4, 5),
('Los pueblos patrimonio de Antioquia son joyas coloniales increíbles',         '2025-11-13 18:00:00', 5, 4, 11),
('El snorkel en los manglares de La Barra es muy auténtico y ecológico',        '2025-11-23 10:00:00', 4, 4, 20),
('Las cascadas de Agua Clara son impresionantes, tour muy bien organizado',     '2025-11-27 09:00:00', 5, 4, 22),
('La cabalgata por el río Meta fue relajante y emocionante a la vez',           '2025-11-29 09:30:00', 4, 4, 26),
('El avistamiento de aves en los humedales con el biólogo fue fascinante',      '2025-12-02 07:00:00', 5, 4, 29),

-- Cliente 5 (Camila Ortiz) — relax, gastronomía, cultural
('Isla Barú es el paraíso, volveré sin ninguna duda',                           '2025-11-09 17:00:00', 5, 5, 6),
('El picnic gourmet en el Simón Bolívar fue una tarde perfecta',                '2025-11-14 12:30:00', 4, 5, 16),
('El taller de ceviche nikkei fue muy didáctico y el resultado delicioso',      '2025-11-19 16:00:00', 5, 5, 17),
('El glamping bajo las estrellas del llano es irrepetible',                     '2025-11-24 08:00:00', 5, 5, 28),
('El parrando llanero es cultura pura, una noche que no se olvida',             '2025-11-30 23:00:00', 4, 5, 30),
('La ruta del cacao es perfecta para los amantes del chocolate fino',           '2025-12-01 15:00:00', 5, 5, 27),
('El brunch fusión dominguero fue un desayuno espectacular',                    '2025-12-03 11:00:00', 4, 5, 18);

-- ============================================================
-- PASO 6: COMPRAS REALIZADAS (historial de compras)
-- Orden columnas: id_plan_guardado, estado, fecha_compra,
--   nombre_plan, personas_disponibles, precio_total_compra,
--   id_cliente
-- ============================================================
INSERT INTO `compra_planes` VALUES
(1, 'Comprado', '2025-11-10', 'Exploración Murallas de Cartagena',  2,  640000.00, 1),
(2, 'Comprado', '2025-11-11', 'Isla Barú Day Pass Deluxe',           1,  520000.00, 1),
(3, 'Comprado', '2025-11-16', 'Caminata Piedra del Peñol 360°',     1,  340000.00, 2),
(4, 'Comprado', '2025-11-22', 'Buceo Avistamiento de Ballenas',      1,  620000.00, 3),
(5, 'Comprado', '2025-11-26', 'Safari Llanero Sunrise',              2,  900000.00, 3),
(6, 'Comprado', '2025-11-23', 'Expedición Cascadas Agua Clara',      1,  410000.00, 4),
(7, 'Comprado', '2025-11-24', 'Glamping Estrellas del Llano',        2, 1160000.00, 5),
(8, 'Comprado', '2025-12-01', 'Isla Barú Day Pass Deluxe',           1,  520000.00, 5);

-- ============================================================
-- PASO 7: CARRITOS APROBADOS (vinculados a las compras)
-- Orden columnas: id, cantidad, eliminado, fue_aprobado,
--   precio_total, cliente_id, compra_plan_id, plan_empresa_id
-- ============================================================
INSERT INTO `carritos_compra_planes` VALUES
(1, 2, b'0', b'1',  640000.00, 1, 1,  1),
(2, 1, b'0', b'1',  520000.00, 1, 2,  6),
(3, 1, b'0', b'1',  340000.00, 2, 3,  7),
(4, 1, b'0', b'1',  620000.00, 3, 4, 19),
(5, 2, b'0', b'1',  900000.00, 3, 5, 25),
(6, 1, b'0', b'1',  410000.00, 4, 6, 22),
(7, 2, b'0', b'1', 1160000.00, 5, 7, 28),
(8, 1, b'0', b'1',  520000.00, 5, 8,  6);

-- ============================================================
-- PASO 8: CALCULAR VALORACION PROMEDIO
-- ============================================================
UPDATE `planes_empresas` pe
SET `valoracion_promedio` = (
    SELECT ROUND(AVG(v.puntuacion), 1)
    FROM `valoraciones` v
    WHERE v.plan_empresa_id = pe.id
)
WHERE EXISTS (
    SELECT 1 FROM `valoraciones` v WHERE v.plan_empresa_id = pe.id
);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- VERIFICACIÓN FINAL
-- ============================================================
SELECT 'empresas'                AS tabla, COUNT(*) AS total FROM empresas
UNION ALL
SELECT 'clientes',                          COUNT(*)          FROM clientes
UNION ALL
SELECT 'planes_empresas',                   COUNT(*)          FROM planes_empresas
UNION ALL
SELECT 'valoraciones',                      COUNT(*)          FROM valoraciones
UNION ALL
SELECT 'compra_planes',                     COUNT(*)          FROM compra_planes
UNION ALL
SELECT 'carritos_compra_planes',            COUNT(*)          FROM carritos_compra_planes
UNION ALL
SELECT 'planes con valoracion_promedio',    COUNT(*)          FROM planes_empresas WHERE valoracion_promedio IS NOT NULL;
