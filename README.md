# 🚗 Alquiler de Vehículos

##📘 Descripción del Proyecto

Este proyecto consiste en un sistema de alquiler de autos desarrollado con Spring Boot. Su objetivo principal es permitir a trabajadores y administradores gestionar de manera eficiente:

El registro de clientes.

El registro y administración de vehículos disponibles para alquilar.

La creación y seguimiento de alquileres, incluyendo fechas, pagos, penalidades y observaciones.

El sistema utiliza:

Spring Boot con Spring Security, JPA, y Lombok.

Cloudinary para el almacenamiento de imágenes de los vehículos.

Bootstrap para el diseño visual.

MySQL como base de datos relacional.

Cada vez que un cliente acude a la tienda, los trabajadores registran la operación de alquiler con todos los detalles requeridos (fechas, duración, documento, abono, penalidad si corresponde, etc.). Solo los usuarios con rol ADMIN o WORKER pueden acceder al sistema.

# ⚙️ Archivo: application-dev.properties

Este archivo contiene la configuración del entorno de desarrollo para una aplicación Spring Boot. Debe ubicarse en el directorio `src/main/resources/` dentro del proyecto.

---

## 🧱 Secciones del archivo

### 🔗 Conexión a la Base de Datos

Configura el acceso a la base de datos MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/alquiler_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=TU_USUARIO
spring.datasource.password=TU_CONTRASEÑA
```

* `url`: Dirección de tu base de datos (cambia `localhost`, puerto o nombre de base de datos si es necesario).
* `username`: Usuario de acceso a MySQL.
* `password`: Contraseña del usuario.

---

### ☁️ Configuración de Cloudinary

Cloudinary se utiliza para almacenar y gestionar imágenes (como las fotos de los vehículos).

```properties
cloudinary.cloud_name=TU_CLOUD_NAME
cloudinary.api_key=TU_API_KEY
cloudinary.api_secret=TU_API_SECRET
```

* `cloud_name`: Nombre de tu cuenta Cloudinary.
* `api_key`: Clave pública para autenticación.
* `api_secret`: Clave secreta para operaciones seguras.

---

### ⚠️ Importante

No compartas este archivo en repositorios públicos. Usa `.gitignore` para protegerlo:

```text
# Ignorar archivos de configuración sensibles\src/main/resources/application-*.properties
```

---

## 📌 Ubicación recomendada

```text
src/
 └── main/
     └── resources/
         └── application-dev.properties
```

---

## 📖 Referencia

* [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
* [Cloudinary Documentation](https://cloudinary.com/documentation)


# 📦 Base de Datos: Sistema de Alquiler de Vehículos

Este repositorio contiene el esquema SQL de la base de datos `alquiler_db` para un sistema de gestión de alquiler de vehículos. A continuación se detallan las tablas y relaciones utilizadas en el diseño del sistema.

## 🧱 Estructura de la Base de Datos

### 🔧 Configuración Inicial

```sql
CREATE DATABASE IF NOT EXISTS alquiler_db;
USE alquiler_db;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
/*!40101 SET NAMES utf8mb4 */;
```

---

### 🪙 Tabla: `moneda`

Contiene las monedas aceptadas para operaciones del sistema.

```sql
CREATE TABLE `moneda` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `simbolo` VARCHAR(10) NOT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` INT(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 📄 Tabla: `documentos`

Tipos de documentos válidos para emitir comprobantes.

```sql
CREATE TABLE `documentos` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `documento` VARCHAR(20) NOT NULL,
  `estado` INT(11) NOT NULL DEFAULT 1,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 🚘 Tabla: `marcas`

Marcas disponibles para los vehículos en alquiler.

```sql
CREATE TABLE `marcas` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `marca` VARCHAR(50) NOT NULL,
  `estado` INT(11) NOT NULL DEFAULT 1,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 🏷️ Tabla: `tipos`

Tipos o categorías de vehículos.

```sql
CREATE TABLE `tipos` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `tipo` VARCHAR(50) NOT NULL,
  `estado` INT(11) NOT NULL DEFAULT 1,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 👤 Tabla: `clientes`

Información de los clientes que alquilan vehículos.

```sql
CREATE TABLE `clientes` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `dni` VARCHAR(10) DEFAULT NULL,
  `nombre` VARCHAR(100) NOT NULL,
  `telefono` VARCHAR(15) NOT NULL,
  `direccion` TEXT NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` INT(11) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 🔐 Tabla: `usuarios`

Usuarios con acceso al sistema (trabajadores y administradores).

```sql
CREATE TABLE `usuarios` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `usuario` VARCHAR(20) NOT NULL UNIQUE,
  `nombre` VARCHAR(100) NOT NULL,
  `apellido` VARCHAR(100) DEFAULT NULL,
  `correo` VARCHAR(80) NOT NULL,
  `telefono` VARCHAR(20) DEFAULT NULL,
  `direccion` VARCHAR(100) DEFAULT NULL,
  `perfil` VARCHAR(50) NOT NULL DEFAULT 'avatar.svg',
  `clave` VARCHAR(100) NOT NULL,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `estado` INT(11) NOT NULL DEFAULT 1,
  `rol` ENUM('ADMIN', 'WORKER') NOT NULL DEFAULT 'WORKER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### ⚙️ Tabla: `configuracion`

Configuración general de la empresa y parámetros del sistema.

```sql
CREATE TABLE `configuracion` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `ruc` VARCHAR(20) NOT NULL,
  `nombre` VARCHAR(200) NOT NULL,
  `telefono` VARCHAR(15) NOT NULL,
  `correo` VARCHAR(100) NOT NULL,
  `direccion` VARCHAR(200) NOT NULL,
  `mensaje` TEXT NOT NULL,
  `logo` VARCHAR(10) NOT NULL,
  `moneda` INT(11) NOT NULL,
  `impuesto` INT(11) NOT NULL,
  `cant_factura` INT(11) NOT NULL,
  `penalidad_por_dia` DECIMAL(10,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`moneda`) REFERENCES `moneda` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 🚙 Tabla: `vehiculos`

Vehículos disponibles para alquiler.

```sql
CREATE TABLE `vehiculos` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `placa` VARCHAR(50) NOT NULL,
  `id_marca` INT(11) NOT NULL,
  `id_tipo` INT(11) NOT NULL,
  `modelo` VARCHAR(50) NOT NULL,
  `foto` VARCHAR(50) NOT NULL,
  `actividad` ENUM('PRESTADO', 'LIBRE') NOT NULL DEFAULT 'LIBRE',
  `estado` INT(11) NOT NULL DEFAULT 1,
  `precio_por_dia` DECIMAL(10,2) DEFAULT 1,
  `fecha` TIMESTAMP NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_marca`) REFERENCES `marcas` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_tipo`) REFERENCES `tipos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

### 📋 Tabla: `alquiler`

Registra cada operación de alquiler realizada por los clientes.

```sql
CREATE TABLE `alquiler` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `id_cliente` INT(11) NOT NULL,
  `id_vehiculo` INT(11) NOT NULL,
  `id_moneda` INT(11) NOT NULL,
  `num_dias` INT(11) DEFAULT 0,
  `precio_dia` DECIMAL(10,2) NOT NULL,
  `abono` DECIMAL(10,2) DEFAULT 0,
  `fecha_prestamo` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `fecha_estimada_devolucion` DATE NOT NULL,
  `fecha_real_devolucion` DATE DEFAULT NULL,
  `id_doc` INT(11) NOT NULL,
  `observacion` TEXT DEFAULT NULL,
  `estado` INT(11) NOT NULL DEFAULT 1,
  `penalidad` DECIMAL(10,2) DEFAULT 0,
  `penalidad_por_dia` DECIMAL(10,2) DEFAULT 0,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`id_cliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_vehiculo`) REFERENCES `vehiculos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_doc`) REFERENCES `documentos` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  FOREIGN KEY (`id_moneda`) REFERENCES `moneda` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## 📝 Notas

- Ejecutar los scripts en el orden indicado para respetar dependencias.
- Incluye campos de auditoría (`fecha`, `estado`) en varias tablas.
- Uso de `ON DELETE CASCADE` para mantener integridad referencial.
---

### 🧪 Usuario de Prueba

Para ingresar al sistema, se ha creado un usuario de prueba con las siguientes credenciales:

- **Usuario:** `admin`
- **Contraseña:** `contra123` (encriptada con bcrypt)

```sql
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
```
