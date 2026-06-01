-- ==========================================
-- 1. USUARIOS
-- ==========================================
INSERT INTO `usuario` (
  `activo`, `contador_intentos`, `cuenta_bloqueada`, `email`, `es_admin`, `fecha_registro`, 
  `fecha_ultimo_cambio_password`, `foto_perfil`, `nick_usuario`, `nombre`, 
  `password`, `pregunta_seguridad`, `primer_apellido`, `respuesta_seguridad`, `segundo_apellido`
) VALUES 
(0, 0, 0, 'juan@email.com', 0, NOW(6), NULL, NULL, 'juan99', 'Juan', 'hash123', '¿Mascota?', 'Pérez', 'Toby', 'García'),
(0, 0, 0, 'ana.admin@email.com', 1, NOW(6), NOW(6), 'ana_avatar.png', 'anaroot', 'Ana', 'hash456', '¿Ciudad?', 'Rodríguez', 'Madrid', NULL),
(0, 2, 0, 'marta@email.com', 0, NOW(6), NULL, NULL, 'marta_dev', 'Marta', 'hash789', '¿Color?', 'Sanz', 'Verde', 'López');

-- ==========================================
-- 2. CATEGORÍAS
-- ==========================================
INSERT INTO `categoria` (`nombre`,`usuario_id`) VALUES 
('Vivienda', 1),
('Ocio', 2),
('Salario', 3);

-- ==========================================
-- 3. TIPOS DE RECOMENDACIÓN
-- ==========================================
INSERT INTO `tipo_recomendacion` (`mensaje`, `nombre`) VALUES 
('Has superado el 80% de tu presupuesto en Ocio. ¡Controla tus gastos!', 'Alerta de Gasto'),
('¡Estás a punto de conseguir tu meta de ahorro! Sigue así.', 'Meta Cercana'),
('Tienes un buen balance positivo este mes. Considera invertir.', 'Consejo Inversión');

-- ==========================================
-- 4. TRANSACCIONES
-- ==========================================
INSERT INTO `transaccion` (`cantidad`, `descripcion`, `fecha`, `tipo_transaccion`, `categoria_id`, `usuario_id`) VALUES 
(1000.00, 'Pago de alquiler', '2026-05-01 10:00:00', 0, 1, 1),
(45.50, 'Entradas de cine', '2026-05-15 18:30:00', 0, 2, 2),
(2500.00, 'Nómina mensual', '2026-05-28 09:00:00', 1, 3, 3);

-- ==========================================
-- 5. SCORES FINANCIEROS
-- ==========================================
INSERT INTO `score_financiero` (`fecha_calculo`, `nivel`, `valor_maximo`, `usuario_id`) VALUES 
(NOW(6), 750, 1000, 1),
(NOW(6), 920, 1000, 2),
(NOW(6), 640, 1000, 3);

-- ==========================================
-- 6. RECOMENDACIONES
-- ==========================================
INSERT INTO `recomendacion` (`fecha_recomendacion`, `tipo_recomendacion_id`, `usuario_id`) VALUES 
(NOW(6), 1, 1),
(NOW(6), 2, 2),
(NOW(6), 3, 3);

-- ==========================================
-- 7. PRESUPUESTOS
-- ==========================================
INSERT INTO `presupuesto` (`fecha_fin`, `fecha_inicio`, `limite`, `categoria_id`, `usuario_id`) VALUES 
('2026-05-31 23:59:59', '2026-05-01 00:00:00', 1200.00, 1, 1),
('2026-05-31 23:59:59', '2026-05-01 00:00:00', 150.00, 2, 2),
('2026-05-31 23:59:59', '2026-05-01 00:00:00', 1000.00, 1, 3);

-- ==========================================
-- 8. OBJETIVOS
-- ==========================================
INSERT INTO `objetivo` (`cantidad_objetivo`, `completado`, `fecha_fin`, `fecha_inicio`, `categoria_id`, `usuario_id`) VALUES 
(15000.00, 0, '2026-12-31 23:59:59', '2026-01-01 00:00:00', 1, 1),
(500.00, 1, '2026-06-30 23:59:59', '2026-01-01 00:00:00', 2, 2),
(3000.00, 0, '2026-12-31 23:59:59', '2026-01-01 00:00:00', 1, 3);