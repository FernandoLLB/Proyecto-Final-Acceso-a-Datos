-- ============================================================
-- Script de inicialización de Base de Datos
-- Proyecto: Gestor de Academias AD
-- Base de datos: MySQL en puerto 3306
-- ============================================================

-- 1. Crear la base de datos si no existe
CREATE DATABASE IF NOT EXISTS acd_proyecto_2025
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 2. Crear el usuario 'acd' si no existe y asignar contraseña
-- Nota: Si el usuario ya existe, primero eliminarlo o modificar la contraseña
CREATE USER IF NOT EXISTS 'acd'@'localhost' IDENTIFIED BY 'acd';
CREATE USER IF NOT EXISTS 'acd'@'%' IDENTIFIED BY 'acd';

-- 3. Otorgar todos los privilegios sobre la base de datos al usuario
GRANT ALL PRIVILEGES ON acd_proyecto_2025.* TO 'acd'@'localhost';
GRANT ALL PRIVILEGES ON acd_proyecto_2025.* TO 'acd'@'%';

-- 4. Aplicar los cambios de privilegios
FLUSH PRIVILEGES;

-- 5. Verificación (opcional - muestra los privilegios del usuario)
-- SHOW GRANTS FOR 'acd'@'localhost';

-- ============================================================
-- INSTRUCCIONES DE USO:
-- ============================================================
-- 1. Abrir MySQL como administrador (root):
--    mysql -u root -p
--
-- 2. Ejecutar este script:
--    SOURCE C:/ruta/al/proyecto/scripts/init-database.sql;
--
--    O también:
--    mysql -u root -p < scripts/init-database.sql
--
-- 3. Verificar la creación:
--    SHOW DATABASES;
--    SELECT User, Host FROM mysql.user WHERE User = 'acd';
-- ============================================================

