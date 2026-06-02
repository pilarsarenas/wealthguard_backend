-- Make usuario_id nullable in categoria to support global categories
ALTER TABLE `categoria` DROP FOREIGN KEY `FK_categoria_usuario_id`;
ALTER TABLE `categoria` MODIFY `usuario_id` int NULL;
ALTER TABLE `categoria` ADD CONSTRAINT `FK_categoria_usuario_id` FOREIGN KEY (`usuario_id`) REFERENCES `usuario`(`id`);
