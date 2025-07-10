# 🚗 Alquiler de Vehículos

![image](https://github.com/user-attachments/assets/ae24bc37-1c52-4561-a751-f036832107e8)

---

## 📘 Descripción del Proyecto

Este proyecto consiste en un sistema de alquiler de autos desarrollado con **Spring Boot**. Su objetivo principal es permitir a trabajadores y administradores gestionar de manera eficiente:

- El registro de clientes.  
- El registro y administración de vehículos disponibles para alquilar.  
- La creación y seguimiento de alquileres, incluyendo fechas, pagos, penalidades y observaciones.

### 🧰 Tecnologías Utilizadas

- Spring Boot + Spring Security + JPA + Lombok  
- MySQL (base de datos relacional)  
- Cloudinary (almacenamiento de imágenes de vehículos)  
- Bootstrap (interfaz gráfica)

---

## ⚙️ Configuración del Entorno de Desarrollo

Este repositorio incluye un archivo de ejemplo de configuración para la aplicación Spring Boot.

### 📄 Archivo: `application-dev.properties.example`

Ubicación:

```
src/main/resources/application-dev.properties.example
```

Contiene la estructura necesaria para conectar con la base de datos y Cloudinary.

### 🚀 Pasos para Configurar

1. Copia el archivo `.example` y renómbralo como:

   ```
   application-dev.properties
   ```

2. Asegúrate de completar con tus datos reales:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/alquiler_db?useSSL=false&serverTimezone=UTC
   spring.datasource.username=tu_usuario
   spring.datasource.password=tu_clave

   cloudinary.cloud_name=tu_nombre
   cloudinary.api_key=tu_api_key
   cloudinary.api_secret=tu_api_secret
   ```

3. Este archivo está incluido en `.gitignore`. **No lo subas al repositorio**.

---

## 📁 Descripción de Propiedades

| Propiedad                    | Descripción                              |
|-----------------------------|------------------------------------------|
| `spring.datasource.url`     | Conexión JDBC a la base de datos MySQL   |
| `spring.datasource.username`| Usuario de base de datos                 |
| `spring.datasource.password`| Contraseña del usuario                   |
| `cloudinary.cloud_name`     | Nombre de cuenta en Cloudinary           |
| `cloudinary.api_key`        | API Key para acceso                      |
| `cloudinary.api_secret`     | API Secret (**no lo compartas**)         |

---

## 🧱 Estructura de la Base de Datos

### 📌 Configuración Inicial

🗂️ La creación de la base de datos se encuentra en un archivo .sql ubicado en una carpeta dentro de la raíz del proyecto. Asegúrate de ejecutar este archivo en tu gestor de base de datos (por ejemplo, MySQL Workbench o phpMyAdmin) antes de iniciar la aplicación.

```sql
CREATE DATABASE IF NOT EXISTS alquiler_db;
USE alquiler_db;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";
/*!40101 SET NAMES utf8mb4 */;
```

---

### 🗂️ Tablas Principales

#### 🪙 `moneda`  
Define las monedas utilizadas para los alquileres.

#### 📄 `documentos`  
Tipos de comprobantes disponibles (boleta, factura, etc.).

#### 🚘 `marcas`  
Marcas de los vehículos en el sistema.

#### 🏷️ `tipos`  
Tipos de vehículos (sedán, SUV, pickup, etc.).

#### 👤 `clientes`  
Información detallada de los clientes que alquilan.

#### 🔐 `usuarios`  
Usuarios con acceso al sistema. Tienen roles `ADMIN` o `WORKER`.

#### ⚙️ `configuracion`  
Datos generales de la empresa: RUC, nombre, penalidad, etc.

#### 🚙 `vehiculos`  
Lista de autos disponibles con sus datos y estado.

#### 📋 `alquiler`  
Registros de cada operación de alquiler realizada.

---

## 🧪 Usuario de Prueba

El sistema incluye un usuario administrador de ejemplo:

- 👤 **Usuario**: `admin`  
- 🔑 **Contraseña**: `contra123` (encriptada con Bcrypt)

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

---

## 📝 Notas Finales

- Asegúrate de ejecutar los scripts en orden correcto por las claves foráneas.
- Se utiliza `ON DELETE CASCADE` para mantener integridad referencial.
- Varias tablas incluyen campos de auditoría como `fecha` y `estado`.
- El sistema es ideal para integrarse con backend Spring Boot o REST API.

---
