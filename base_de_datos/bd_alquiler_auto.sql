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

-- =======================
-- Tabla: vehiculos
-- =======================
CREATE TABLE `vehiculos` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `placa` varchar(50) NOT NULL,
  `id_marca` int(11) NOT NULL,
  `id_tipo` int(11) NOT NULL,
  `modelo` varchar(50) NOT NULL,
  `foto` varchar(100) NOT NULL,
  `actividad` ENUM('PRESTADO', 'LIBRE') NOT NULL DEFAULT 'LIBRE',
  `estado` int(11) NOT NULL DEFAULT 1,
  `precio_por_dia` decimal(10,2) DEFAULT 1,
  `fecha` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_marca`) REFERENCES `marcas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_tipo`) REFERENCES `tipos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

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
  `estado`  ENUM('EN PRESTAMO', 'DEVUELTO','CANCELADO') NOT NULL DEFAULT 'EN PRESTAMO',
  `penalidad` decimal(10,2) DEFAULT 0,
  `penalidad_por_dia` decimal(10,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_vehiculo`) REFERENCES `vehiculos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_doc`) REFERENCES `documentos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_moneda`) REFERENCES `moneda` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

COMMIT;
-- =======================
-- Nuevos INSERTS
-- =======================

-- Monedas
INSERT INTO moneda (simbolo, nombre) VALUES
('S/', 'Soles'),
('$', 'Dólares'),
('€', 'Euros'),
('£', 'Libras'),
('¥', 'Yen'),
('₿', 'Bitcoin'),
('R$', 'Real'),
('₽', 'Rublo'),
('₩', 'Won'),
('C$', 'Peso colombiano');

-- Documentos
INSERT INTO documentos (documento) VALUES
('Boleta'),
('Factura'),
('Nota de venta'),
('Ticket'),
('Recibo'),
('Guía'),
('Remito'),
('Factura electrónica'),
('Boleta electrónica'),
('Orden de compra');

-- Marcas
INSERT INTO marcas (marca) VALUES
('Toyota'),
('Hyundai'),
('Chevrolet'),
('Nissan'),
('Ford'),
('Kia'),
('Volkswagen'),
('Mazda'),
('Honda'),
('Suzuki');

-- Tipos
INSERT INTO tipos (tipo) VALUES
('Sedán'),
('SUV'),
('Pickup'),
('Hatchback'),
('Convertible'),
('Coupé'),
('Furgoneta'),
('Camión'),
('Minivan'),
('Eléctrico');

-- Clientes
INSERT INTO clientes (dni, nombre, telefono, direccion) VALUES
('11111111', 'Pedro Gómez', '900000001', 'Av. Los Héroes 101'),
('22222222', 'Lucía Ramírez', '900000002', 'Jr. Ayacucho 234'),
('33333333', 'Carlos Díaz', '900000003', 'Av. Brasil 321'),
('44444444', 'María León', '900000004', 'Jr. Junín 456'),
('55555555', 'Luis Torres', '900000005', 'Calle Lima 567'),
('66666666', 'Rosa López', '900000006', 'Pasaje Piura 678'),
('77777777', 'Miguel Ríos', '900000007', 'Av. San Martín 789'),
('88888888', 'Elena Soto', '900000008', 'Jr. Bolívar 890'),
('99999999', 'Sergio Paredes', '900000009', 'Av. Grau 1011'),
('00000000', 'Natalia Vega', '900000010', 'Calle Cusco 1122');

-- Configuración (una sola entrada como corresponde)
INSERT INTO configuracion (ruc, nombre, telefono, correo, direccion, mensaje, logo, moneda, impuesto, cant_factura, penalidad_por_dia)
VALUES ('12345678901', 'Mi Empresa S.A.C.', '012345678', 'empresa@correo.com', 'Av. Principal 123', '¡Gracias por su preferencia!', 'logo.png', 1, 18, 1000, 10.00);

-- Vehículos
INSERT INTO vehiculos (placa, id_marca, id_tipo, modelo, foto, precio_por_dia) VALUES
('AAA111', 1, 1, 'Corolla', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 100),
('BBB222', 2, 2, 'Tucson', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 120),
('CCC333', 3, 3, 'Silverado', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 150),
('DDD444', 4, 4, 'March', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 90),
('EEE555', 5, 5, 'Mustang', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 200),
('FFF666', 6, 6, 'Rio', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 80),
('GGG777', 7, 7, 'Golf', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 110),
('HHH888', 8, 8, 'CX-5', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 130),
('III999', 9, 9, 'Civic', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 105),
('JJJ000', 10, 10, 'Swift', 'https://res-console.cloudinary.com/drrlway38/thumbnails/v1/image/upload/v1751315161/dmlzdGEtZGVsLWNvY2hlLTNkX2MydHo3aQ==/preview', 95);

-- Alquileres (todos en EN PRESTAMO y estado activo)
INSERT INTO alquiler (id_cliente, id_vehiculo, id_moneda, num_dias, precio_dia, abono, fecha_prestamo, hora, fecha_estimada_devolucion, id_doc, observacion, estado, penalidad_por_dia)
VALUES
(1, 1, 1, 5, 100.00, 200.00, '2025-06-20', '09:00:00', '2025-06-25', 1, 'Sin observación', 'EN PRESTAMO', 10.00),
(2, 2, 2, 3, 120.00, 100.00, '2025-06-21', '10:30:00', '2025-06-24', 2, 'Sin observación', 'EN PRESTAMO', 10.00),
(3, 3, 1, 7, 150.00, 300.00, '2025-06-22', '08:00:00', '2025-06-29', 3, 'Sin observación', 'EN PRESTAMO', 10.00),
(4, 4, 2, 2, 90.00, 50.00, '2025-06-23', '11:15:00', '2025-06-25', 4, 'Sin observación', 'EN PRESTAMO', 10.00),
(5, 5, 1, 4, 200.00, 100.00, '2025-06-24', '12:00:00', '2025-06-28', 5, 'Sin observación', 'EN PRESTAMO', 10.00),
(6, 6, 2, 6, 80.00, 80.00, '2025-06-25', '13:00:00', '2025-07-01', 6, 'Sin observación', 'EN PRESTAMO', 10.00),
(7, 7, 1, 5, 110.00, 110.00, '2025-06-26', '14:00:00', '2025-07-01', 7, 'Sin observación', 'EN PRESTAMO', 10.00),
(8, 8, 2, 3, 130.00, 90.00, '2025-06-27', '15:00:00', '2025-06-30', 8, 'Sin observación', 'EN PRESTAMO', 10.00),
(9, 9, 1, 4, 105.00, 100.00, '2025-06-28', '16:00:00', '2025-07-02', 9, 'Sin observación', 'EN PRESTAMO', 10.00),
(10, 10, 2, 2, 95.00, 50.00, '2025-06-29', '17:00:00', '2025-07-01', 10, 'Sin observación', 'EN PRESTAMO', 10.00);


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

