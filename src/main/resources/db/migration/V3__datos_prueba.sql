-- Script de Datos de Prueba para el Sistema SaaS de Gestión de Academias
-- Ejecutar DESPUÉS del script de migración V2__add_propietario_entity.sql
--
-- Este script crea:
-- 1. Usuario ADMIN (si no existe)
-- 2. Tres propietarios de prueba con sus usuarios
-- 3. Academias asociadas a cada propietario

-- ========================================
-- PASO 1: Crear usuario ADMIN (si no existe)
-- ========================================

INSERT IGNORE INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'admin@sistemaacademias.com', 'Administrador', 'Sistema', 'ADMIN', 1, 1, NULL);
-- Contraseña: admin123

-- ========================================
-- PASO 2: Crear propietarios de prueba
-- ========================================

-- Propietario 1: Academia Elite
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('propietario1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'propietario1@elite.com', 'Juan', 'García López', 'PROPIETARIO', 1, 1, NULL);

INSERT INTO propietario (usuario_id, razon_social, nif_cif, telefono, direccion, fecha_alta, activo)
VALUES (LAST_INSERT_ID(), 'Academia Elite S.L.', 'B12345678', '+34 912 345 678',
        'Calle Principal 123, 28001 Madrid', NOW(), 1);

SET @propietario1_id = LAST_INSERT_ID();

-- Propietario 2: Formación Avanzada
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('propietario2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'propietario2@formacion.com', 'María', 'Rodríguez Sánchez', 'PROPIETARIO', 1, 1, NULL);

INSERT INTO propietario (usuario_id, razon_social, nif_cif, telefono, direccion, fecha_alta, activo)
VALUES (LAST_INSERT_ID(), 'Formación Avanzada SL', 'B87654321', '+34 913 456 789',
        'Avenida de Europa 45, 28100 Madrid', NOW(), 1);

SET @propietario2_id = LAST_INSERT_ID();

-- Propietario 3: Centro Educativo Innovación
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('propietario3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'propietario3@innovacion.com', 'Carlos', 'Martínez Pérez', 'PROPIETARIO', 1, 1, NULL);

INSERT INTO propietario (usuario_id, razon_social, nif_cif, telefono, direccion, fecha_alta, activo)
VALUES (LAST_INSERT_ID(), 'Centro Educativo Innovación', 'B11223344', '+34 914 567 890',
        'Plaza Mayor 10, 28013 Madrid', NOW(), 1);

SET @propietario3_id = LAST_INSERT_ID();

-- ========================================
-- PASO 3: Crear academias para cada propietario
-- ========================================

-- Academias del Propietario 1 (Academia Elite)
INSERT INTO academia (nombre, activa, fecha_alta, nif_cif, email_contacto, telefono, direccion, propietario_id)
VALUES
('Academia Elite Madrid Centro', 1, NOW(), 'B12345678', 'info@elitemadrid.com',
 '+34 912 345 678', 'Calle Gran Vía 25, 28013 Madrid', @propietario1_id),

('Academia Elite Barcelona', 1, NOW(), 'B12345679', 'info@elitebarcelona.com',
 '+34 933 456 789', 'Passeig de Gràcia 100, 08008 Barcelona', @propietario1_id);

-- Academias del Propietario 2 (Formación Avanzada)
INSERT INTO academia (nombre, activa, fecha_alta, nif_cif, email_contacto, telefono, direccion, propietario_id)
VALUES
('Formación Avanzada Central', 1, NOW(), 'B87654321', 'central@formacion.com',
 '+34 913 456 789', 'Calle Serrano 50, 28006 Madrid', @propietario2_id),

('Formación Avanzada Norte', 1, NOW(), 'B87654322', 'norte@formacion.com',
 '+34 913 456 790', 'Avenida de Burgos 12, 28036 Madrid', @propietario2_id),

('Formación Avanzada Sur', 0, NOW(), 'B87654323', 'sur@formacion.com',
 '+34 913 456 791', 'Calle Alcalá 200, 28028 Madrid', @propietario2_id);

-- Academias del Propietario 3 (Centro Educativo Innovación)
INSERT INTO academia (nombre, activa, fecha_alta, nif_cif, email_contacto, telefono, direccion, propietario_id)
VALUES
('CEI Campus Principal', 1, NOW(), 'B11223344', 'principal@innovacion.com',
 '+34 914 567 890', 'Ciudad Universitaria s/n, 28040 Madrid', @propietario3_id);

-- ========================================
-- PASO 4: Verificación
-- ========================================

-- Mostrar resumen de la carga de datos
SELECT 'RESUMEN DE DATOS DE PRUEBA' AS Resultado;

SELECT
    '1. Propietarios creados' AS Info,
    COUNT(*) AS Total
FROM propietario;

SELECT
    '2. Academias creadas por propietario' AS Info,
    p.razon_social AS Propietario,
    COUNT(a.id) AS Total_Academias,
    SUM(CASE WHEN a.activa = 1 THEN 1 ELSE 0 END) AS Academias_Activas
FROM propietario p
LEFT JOIN academia a ON a.propietario_id = p.id
GROUP BY p.id, p.razon_social;

SELECT
    '3. Total de academias en el sistema' AS Info,
    COUNT(*) AS Total,
    SUM(CASE WHEN activa = 1 THEN 1 ELSE 0 END) AS Activas,
    SUM(CASE WHEN activa = 0 THEN 1 ELSE 0 END) AS Inactivas
FROM academia;

-- ========================================
-- INFORMACIÓN DE ACCESO
-- ========================================

SELECT '=== CREDENCIALES DE ACCESO ===' AS Info;
SELECT '
ADMIN:
  Usuario: admin
  Contraseña: admin123

PROPIETARIOS:
  Usuario: propietario1
  Contraseña: admin123
  Empresa: Academia Elite S.L.
  Academias: 2

  Usuario: propietario2
  Contraseña: admin123
  Empresa: Formación Avanzada SL
  Academias: 3 (1 inactiva)

  Usuario: propietario3
  Contraseña: admin123
  Empresa: Centro Educativo Innovación
  Academias: 1
' AS Credenciales;

-- ========================================
-- NOTAS
-- ========================================

-- NOTA 1: Todos los usuarios tienen la contraseña 'admin123' (hash BCrypt)
-- NOTA 2: Todos los emails están verificados
-- NOTA 3: Una academia está inactiva para pruebas (Formación Avanzada Sur)
-- NOTA 4: Este script es IDEMPOTENTE para el ADMIN (usa INSERT IGNORE)
-- NOTA 5: Para propietarios, ejecuta solo UNA VEZ o elimina datos previos
