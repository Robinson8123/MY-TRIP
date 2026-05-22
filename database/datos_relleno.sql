-- ============================================================
-- DATOS DE RELLENO — midestinobd
-- Ejecutar en MySQL después de importar my trip 1.sql
-- ============================================================

USE midestinobd;

-- ============================================================
-- 1. VALORACIONES (tabla crítica para el sistema de recomendación)
--    Se necesitan al menos 25-30 registros distribuidos entre
--    múltiples usuarios y planes para que el filtrado colaborativo
--    funcione correctamente.
-- ============================================================

INSERT INTO `valoraciones` (`comentario`, `fecha`, `puntuacion`, `cliente_id`, `plan_empresa_id`) VALUES

-- Cliente 1 (Laura González) — le gustan cultural, gastronómico, relax
('El tour gastronómico caribeño fue una experiencia de sabores increíble', '2025-11-05 14:30:00', 5, 1, 4),
('Isla Barú Day Pass es el paraíso en Colombia, servicio premium impecable', '2025-11-10 10:15:00', 5, 1, 6),
('El street food en La Candelaria tiene mucha historia y sabor auténtico', '2025-11-15 18:45:00', 4, 1, 13),
('El picnic gourmet en el parque fue romántico y delicioso', '2025-11-20 12:00:00', 5, 1, 16),
('El tour de arte urbano en Chapinero es diferente y muy cultural', '2025-11-22 13:00:00', 4, 1, 15),

-- Cliente 2 (Sebastián López) — le gustan aventura y naturaleza
('La chiva rumbera es diversión pura, música y rumba asegurada', '2025-11-06 20:00:00', 4, 2, 2),
('El snorkel en las Islas del Rosario fue alucinante, aguas cristalinas', '2025-11-11 16:30:00', 5, 2, 3),
('La Piedra del Peñol es impresionante, guías muy bien preparados', '2025-11-16 17:00:00', 4, 2, 7),
('El parapente en San Félix fue la experiencia más emocionante de mi vida', '2025-11-21 11:00:00', 5, 2, 9),
('El senderismo nocturno en Arví y las luciérnagas fueron mágicos', '2025-11-25 22:00:00', 4, 2, 12),
('El cañoning en Cascada Cristal fue intenso pero absolutamente increíble', '2025-11-28 09:00:00', 5, 2, 10),

-- Cliente 3 (Valeria Castillo) — le gustan aventura, relax, gastronomía premium
('El snorkel en los manglares es una experiencia diferente a cualquier otra', '2025-11-07 09:00:00', 4, 3, 3),
('La ruta del café y aventura en Fredonia es un must-do en Antioquia', '2025-11-12 17:30:00', 5, 3, 8),
('Ver ballenas jorobadas fue un momento que nunca olvidaré en la vida', '2025-11-22 08:30:00', 5, 3, 19),
('El safari llanero al amanecer es absolutamente espectacular', '2025-11-26 06:30:00', 5, 3, 25),
('La ruta del cacao y los bombones artesanales superaron todas mis expectativas', '2025-11-28 14:00:00', 4, 3, 27),
('El glamping bajo las estrellas del llano es magia pura', '2025-11-30 08:00:00', 5, 3, 28),

-- Cliente 4 (Andrés Martínez) — le gustan aventura, cultural, ecológico
('El kayak nocturno en la Ciénaga para ver el plancton fue único', '2025-11-08 20:30:00', 4, 4, 5),
('Los pueblos patrimonio de Antioquia son joyas coloniales increíbles', '2025-11-13 18:00:00', 5, 4, 11),
('El snorkel en los manglares de La Barra es muy auténtico y ecológico', '2025-11-23 10:00:00', 4, 4, 20),
('Las cascadas de Agua Clara son impresionantes, tour muy bien organizado', '2025-11-27 09:00:00', 5, 4, 22),
('La cabalgata por el río Meta fue relajante y emocionante a la vez', '2025-11-29 09:30:00', 4, 4, 26),
('El avistamiento de aves en los humedales con el biólogo fue fascinante', '2025-12-02 07:00:00', 5, 4, 29),

-- Cliente 5 (Camila Ortiz) — le gustan relax, gastronomía, cultural
('Isla Barú es el paraíso, volveré sin ninguna duda', '2025-11-09 17:00:00', 5, 5, 6),
('El picnic gourmet en el Simón Bolívar fue una tarde perfecta', '2025-11-14 12:30:00', 4, 5, 16),
('El taller de ceviche nikkei fue muy didáctico y el resultado delicioso', '2025-11-19 16:00:00', 5, 5, 17),
('El glamping bajo las estrellas del llano es irrepetible', '2025-11-24 08:00:00', 5, 5, 28),
('El parrando llanero es cultura pura, una noche que no se olvida', '2025-11-30 23:00:00', 4, 5, 30),
('La ruta del cacao es perfecta para los amantes del chocolate fino', '2025-12-01 15:00:00', 5, 5, 27),
('El brunch fusión dominguero fue un desayuno espectacular', '2025-12-03 11:00:00', 4, 5, 18);

-- ============================================================
-- 2. ACTUALIZAR VALORACION PROMEDIO EN PLANES
--    Se recalcula automáticamente a partir de todas las valoraciones
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

-- ============================================================
-- 3. COMPRAS REALIZADAS (historial de compras)
-- ============================================================

INSERT INTO `compra_planes` (`estado`, `fecha_compra`, `nombre_plan`, `personas_disponibles`, `precio_total_compra`, `id_cliente`) VALUES
('Comprado', '2025-11-10', 'Exploración Murallas de Cartagena',   2, 640000.00,  1),
('Comprado', '2025-11-11', 'Isla Barú Day Pass Deluxe',            1, 520000.00,  1),
('Comprado', '2025-11-16', 'Caminata Piedra del Peñol 360°',      1, 340000.00,  2),
('Comprado', '2025-11-22', 'Buceo Avistamiento de Ballenas',       1, 620000.00,  3),
('Comprado', '2025-11-26', 'Safari Llanero Sunrise',               2, 900000.00,  3),
('Comprado', '2025-11-23', 'Expedición Cascadas Agua Clara',       1, 410000.00,  4),
('Comprado', '2025-11-24', 'Glamping Estrellas del Llano',         2, 1160000.00, 5),
('Comprado', '2025-12-01', 'Isla Barú Day Pass Deluxe',            1, 520000.00,  5);

-- ============================================================
-- 4. CARRITOS APROBADOS (vinculados a las compras anteriores)
--    Los IDs de compra_planes inician desde 1 (tabla estaba vacía)
-- ============================================================

INSERT INTO `carritos_compra_planes` (`cantidad`, `eliminado`, `fue_aprobado`, `precio_total`, `cliente_id`, `compra_plan_id`, `plan_empresa_id`) VALUES
(2, b'0', b'1', 640000.00,  1, 1, 1),
(1, b'0', b'1', 520000.00,  1, 2, 6),
(1, b'0', b'1', 340000.00,  2, 3, 7),
(1, b'0', b'1', 620000.00,  3, 4, 19),
(2, b'0', b'1', 900000.00,  3, 5, 25),
(1, b'0', b'1', 410000.00,  4, 6, 22),
(2, b'0', b'1', 1160000.00, 5, 7, 28),
(1, b'0', b'1', 520000.00,  5, 8, 6);

-- ============================================================
-- 5. VERIFICACIÓN — cuenta registros insertados
-- ============================================================

SELECT 'valoraciones'   AS tabla, COUNT(*) AS total FROM valoraciones
UNION ALL
SELECT 'compra_planes',           COUNT(*)          FROM compra_planes
UNION ALL
SELECT 'carritos_compra_planes',  COUNT(*)          FROM carritos_compra_planes
UNION ALL
SELECT 'planes con promedio',     COUNT(*)          FROM planes_empresas WHERE valoracion_promedio IS NOT NULL;
