CREATE DATABASE IF NOT EXISTS alquiler_db;
USE alquiler_db;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
/*!40101 SET NAMES utf8mb4 */;

-- =======================
-- Tabla: moneda
-- =======================
CREATE TABLE `moneda` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `simbolo` varchar(10) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `moneda` (`simbolo`, `nombre`) VALUES
('S/', 'Soles'),
('$', 'Dólares');

-- =======================
-- Tabla: documentos
-- =======================
CREATE TABLE `documentos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `documento` varchar(20) NOT NULL,
  `estado` int(11) NOT NULL DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `documentos` (`documento`) VALUES
('Boleta'),
('Factura');

-- =======================
-- Tabla: marcas
-- =======================
CREATE TABLE `marcas` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `marca` varchar(50) NOT NULL,
  `estado` int(11) NOT NULL DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `marcas` (`marca`) VALUES
('Toyota'),
('Hyundai'),
('Chevrolet');

-- =======================
-- Tabla: tipos
-- =======================
CREATE TABLE `tipos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tipo` varchar(50) NOT NULL,
  `estado` int(11) NOT NULL DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `tipos` (`tipo`) VALUES
('Sedán'),
('SUV'),
('Pickup');

-- =======================
-- Tabla: clientes
-- =======================
CREATE TABLE `clientes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `dni` varchar(10) DEFAULT NULL,
  `nombre` varchar(100) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `direccion` text NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` int(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `clientes` (`dni`, `nombre`, `telefono`, `direccion`) VALUES
('12345678', 'Juan Pérez', '987654321', 'Av. Perú 123'),
('87654321', 'Ana Torres', '999888777', 'Jr. Lima 456');

-- =======================
-- Tabla: usuarios
-- =======================
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario` varchar(20) NOT NULL UNIQUE,
  `nombre` varchar(100) NOT NULL,
  `apellido` varchar(100) DEFAULT NULL,
  `correo` varchar(80) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `perfil` varchar(50) NOT NULL DEFAULT 'avatar.svg',
  `clave` varchar(100) NOT NULL,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` int(11) NOT NULL DEFAULT 1,
  `rol` ENUM('ADMIN', 'WORKER') NOT NULL DEFAULT 'WORKER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
-- =======================
-- Tabla: configuracion
-- =======================
CREATE TABLE `configuracion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ruc` varchar(20) NOT NULL,
  `nombre` varchar(200) NOT NULL,
  `telefono` varchar(15) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `direccion` varchar(200) NOT NULL,
  `mensaje` text NOT NULL,
  `logo` varchar(10) NOT NULL,
  `moneda` int(11) NOT NULL,
  `impuesto` int(11) NOT NULL,
  `cant_factura` int(11) NOT NULL,
  `penalidad_por_dia` decimal(10,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`moneda`) REFERENCES `moneda` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `configuracion` (`ruc`, `nombre`, `telefono`, `correo`, `direccion`, `mensaje`, `logo`, `moneda`, `impuesto`, `cant_factura`, `penalidad_por_dia`)
VALUES ('12345678901', 'Mi Empresa S.A.C.', '012345678', 'empresa@correo.com', 'Av. Principal 123', '¡Gracias por su preferencia!', 'logo.png', 1, 18, 1000, 10.00);

-- =======================
-- Tabla: vehiculos
-- =======================
CREATE TABLE `vehiculos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `placa` varchar(50) NOT NULL,
  `id_marca` int(11) NOT NULL,
  `id_tipo` int(11) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `foto` varchar(50) NOT NULL,
  `actividad` ENUM('PRESTADO', 'LIBRE') NOT NULL DEFAULT 'LIBRE',
  `estado` int(11) NOT NULL DEFAULT 1,
  `precio_por_dia` decimal(10,2) DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_marca`) REFERENCES `marcas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_tipo`) REFERENCES `tipos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `vehiculos` (`placa`, `id_marca`, `id_tipo`, `modelo`, `foto`,`precio_por_dia`) VALUES
('ABC123', 1, 1, 'Corolla', 'corolla.jpg',100),
('XYZ789', 2, 2, 'Tucson', 'tucson.jpg',100);

-- =======================
-- Tabla: alquiler
-- =======================
CREATE TABLE `alquiler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` int(11) NOT NULL,
  `id_vehiculo` int(11) NOT NULL,
  `id_moneda` int(11) NOT NULL,
  `num_dias` int(11) default 0,
  `precio_dia` decimal(10,2) NOT NULL,
  `abono` decimal(10,2) default 0,
  `fecha_prestamo` date NOT NULL,
  `hora` time NOT NULL,
  `fecha_estimada_devolucion` date NOT NULL,
  `fecha_real_devolucion` date default NULL,
  `id_doc` int(11) NOT NULL,
  `observacion` text DEFAULT NULL,
  `estado` int(11) NOT NULL DEFAULT 1,
  `penalidad` decimal(10,2) DEFAULT 0,
  `penalidad_por_dia` decimal(10,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_vehiculo`) REFERENCES `vehiculos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_doc`) REFERENCES `documentos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_moneda`) REFERENCES `moneda` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO `alquiler` (
  `id_cliente`, `id_vehiculo`, `id_moneda` , `num_dias`, `precio_dia`, `abono`, `fecha_prestamo`, `hora`, `fecha_estimada_devolucion`, `fecha_real_devolucion`, `id_doc`, `observacion`, `penalidad`, `penalidad_por_dia`
) VALUES (
  1, 1, 1, 3, 150.00, 100.00, '2025-06-10', '10:00:00', '2025-06-13', '2025-06-14', 1, 'Devolvió con 1 día de retraso', 10.00, 10.00
);

-- Segundo alquiler
INSERT INTO `alquiler` (
  `id_cliente`, `id_vehiculo`, `id_moneda` , `num_dias`, `precio_dia`, `abono`, `fecha_prestamo`, `hora`, `fecha_estimada_devolucion`, `fecha_real_devolucion`, `id_doc`, `observacion`, `penalidad`, `penalidad_por_dia`
) VALUES (
  2, 2, 1,5, 200.00, 150.00, '2025-06-01', '09:30:00', '2025-06-06', '2025-06-06', 2, 'Entregado a tiempo y en buen estado', 0.00, 10.00
);

-- Tercer alquiler
INSERT INTO `alquiler` (
  `id_cliente`, `id_vehiculo`, `id_moneda` , `num_dias`, `precio_dia`, `abono`, `fecha_prestamo`, `hora`, `fecha_estimada_devolucion`, `fecha_real_devolucion`, `id_doc`, `observacion`, `penalidad`, `penalidad_por_dia`
) VALUES (
  1, 2, 1, 2, 180.00, 180.00, '2025-06-15', '11:15:00', '2025-06-17', '2025-06-17', 1, 'Sin novedades', 0.00, 10.00
);


COMMIT;

INSERT INTO usuarios (
  usuario, nombre, apellido, correo, telefono, direccion, perfil, clave, estado, fecha, rol
) VALUES (
  'admin',
  'Administrador',
  'Del Sistema',
  'admin@correo.com',
  '999999999',
  'Av. Principal 123',
  'avatar.svg',
  '$2a$12$UXW2hk4pCL9LlyAUTBsaf.R3EF.NjIL8/X9YN4fld22WjMV15tnrS',
  1,
  CURRENT_TIMESTAMP(),
  'ADMIN'
);


select * from moneda
