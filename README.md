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

### 📄 Archivo: `env.example`

Ubicación:

```
.env.example
```

Contiene la estructura necesaria para conectar con la base de datos y Cloudinary.

### 🚀 Pasos para Configurar

1. Copia el archivo `.env.example` y renómbralo como:
   ```
   .env
   ```

2. Asegúrate de completar con tus datos reales:

   ```properties
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/alquiler_db?useSSL=false&serverTimezone=UTC
   SPRING_DATASOURCE_USERNAME=tu_usuario
   SPRING_DATASOURCE_PASSWORD=tu_clave

   CLOUDINARY_CLOUD_NAME=tu_nombre
   CLOUDINARY_API_KEY=tu_api_key
   CLOUDINARY_API_SECRET=tu_api_secret
   ```

3. Este archivo está incluido en `.gitignore`. **No lo subas al repositorio**.

---

## 📁 Descripción de Propiedades

| Propiedad                    | Descripción                              |
|-----------------------------|------------------------------------------|
| `SPRING_DATASOURCE_URL`     | Conexión JDBC a la base de datos MySQL   |
| `SPRING_DATASOURCE_USERNAME`| Usuario de base de datos                 |
| `SPRING_DATASOURCE_PASSWORD`| Contraseña del usuario                   |
| `CLOUDINARY_CLOUD_NAME`     | Nombre de cuenta en Cloudinary           |
| `CLOUDINARY_API_KEY`        | API Key para acceso                      |
| `CLOUDINARY_API_SECRET`     | API Secret (**no lo compartas**)         |

---

## 🧱 Estructura de la Base de Datos

### 📌 Configuración Inicial

🗂️ La creación de la base de datos se encuentra en un archivo bd_alquiler_auto.sql ubicado en una carpeta llamada base_de_datos ubicada dentro de la raíz del proyecto. Asegúrate de ejecutar este archivo en tu gestor de base de datos (por ejemplo, MySQL que es el preferente ya que el proyecto se uso aquel gestor) antes de iniciar la aplicación.

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
