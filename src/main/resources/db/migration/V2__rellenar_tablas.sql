INSERT INTO `usuario` (
  `activo`, `email`, `fecha_registro`, `fecha_ultimo_cambio_password`, 
  `foto_perfil`, `nick_usuario`, `nombre`, `password`, 
  `pregunta_seguridad`, `primer_apellido`, `respuesta_seguridad`, `segundo_apellido`
) VALUES 
(0, 'juan@email.com', NOW(6), NULL, NULL, 'juan99', 'Juan', 'hash123', '¿Mascota?', 'Pérez', 'Toby', 'García');

INSERT INTO `categoria` (`nombre`, `icono`) VALUES 
('Vivienda', 'home'),
('Ocio', 'sports_esports'),
('Salario', 'payments'),
('Alimentación', 'restaurant'),
('Transporte', 'directions_car'),
('Salud', 'medical_services'),
('Educación', 'school'),
('Suscripciones', 'subscriptions'),
('Viajes', 'flight'),
('Regalos', 'redeem'),
('Otros', 'category');

INSERT INTO `tipo_recomendacion` (`mensaje`, `nombre`) VALUES 
('Has superado el 80% de tu presupuesto en Ocio. ¡Controla tus gastos!', 'Alerta de Gasto'),
('¡Estás a punto de conseguir tu meta de ahorro! Sigue así.', 'Meta Cercana'),
('Tienes un buen balance positivo este mes. Considera invertir.', 'Consejo Inversión');

INSERT INTO `transaccion` (`cantidad`, `descripcion`, `fecha`, `tipo_transaccion`, `categoria_id`, `usuario_id`) VALUES 
(1000.00, 'Pago de alquiler', '2026-06-01 10:00:00', 0, 1, 1);

INSERT INTO `score_financiero` (`fecha_calculo`, `nivel`, `valor_maximo`, `usuario_id`) VALUES 
(NOW(6), 750, 1000, 1);

INSERT INTO `recomendacion` (`fecha_recomendacion`, `tipo_recomendacion_id`, `usuario_id`) VALUES 
(NOW(6), 1, 1);

INSERT INTO `presupuesto` (`fecha_inicio`, `fecha_fin`, `limite`, `categoria_id`, `usuario_id`) VALUES 
('2026-06-01 00:00:00', '2026-06-30 23:59:59', 1200.00, 1, 1);

INSERT INTO `objetivo` (`cantidad_objetivo`, `fecha_inicio`, `fecha_fin`, `usuario_id`) VALUES 
(15000.00, '2026-01-01 00:00:00', '2026-12-31 23:59:59', 1);