-- ========================================
-- Script de Datos de Prueba - PROFESORES
-- Versión: 1.0
-- Fecha: 06/02/2026
-- ========================================
-- Este script crea profesores de prueba para cada academia
-- Se ejecuta después de V3__datos_prueba.sql

-- ========================================
-- PROFESORES PARA ACADEMIAS DEL PROPIETARIO 1
-- ========================================

-- Obtener ID de academia "Academia Elite Madrid Centro"
SET @academia1 = (SELECT id FROM academia WHERE nombre = 'Academia Elite Madrid Centro' LIMIT 1);

-- Profesor 1: Juan Martínez (Programación)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'juan.martinez@elitemadrid.com', 'Juan', 'Martínez López', 'PROFESOR', 1, 1, @academia1);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia1, 'Programación Web y Aplicaciones',
        'Profesor con 10 años de experiencia en desarrollo web. Especializado en Java, Spring Boot y tecnologías frontend modernas.',
        '2020-01-15');

-- Profesor 2: María García (Diseño)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'maria.garcia@elitemadrid.com', 'María', 'García Ruiz', 'PROFESOR', 1, 1, @academia1);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia1, 'Diseño Gráfico y UX/UI',
        'Diseñadora profesional con experiencia en branding y diseño de interfaces. Apasionada por la experiencia de usuario.',
        '2021-03-20');

-- Obtener ID de academia "Academia Elite Barcelona"
SET @academia2 = (SELECT id FROM academia WHERE nombre = 'Academia Elite Barcelona' LIMIT 1);

-- Profesor 3: Carlos Rodríguez (Bases de Datos)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'carlos.rodriguez@elitebarcelona.com', 'Carlos', 'Rodríguez Pérez', 'PROFESOR', 1, 1, @academia2);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia2, 'Administración de Bases de Datos',
        'Experto en MySQL, PostgreSQL y Oracle. Especializado en optimización y diseño de bases de datos.',
        '2019-09-10');

-- ========================================
-- PROFESORES PARA ACADEMIAS DEL PROPIETARIO 2
-- ========================================

-- Obtener ID de academia "Formación Avanzada Central"
SET @academia3 = (SELECT id FROM academia WHERE nombre = 'Formación Avanzada Central' LIMIT 1);

-- Profesor 4: Laura Sánchez (Marketing Digital)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor4', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'laura.sanchez@formacion.com', 'Laura', 'Sánchez Díaz', 'PROFESOR', 1, 1, @academia3);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia3, 'Marketing Digital y Redes Sociales',
        'Especialista en estrategias de marketing digital, SEO y gestión de redes sociales.',
        '2020-06-01');

-- Profesor 5: Pedro Fernández (Ciberseguridad)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor5', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'pedro.fernandez@formacion.com', 'Pedro', 'Fernández Gil', 'PROFESOR', 1, 1, @academia3);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia3, 'Ciberseguridad y Hacking Ético',
        'Certificado en CISSP y CEH. Experto en seguridad informática y auditorías de seguridad.',
        '2021-11-15');

-- Obtener ID de academia "Formación Avanzada Norte"
SET @academia4 = (SELECT id FROM academia WHERE nombre = 'Formación Avanzada Norte' LIMIT 1);

-- Profesor 6: Ana López (Idiomas)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor6', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'ana.lopez@formacion.com', 'Ana', 'López Martín', 'PROFESOR', 1, 1, @academia4);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia4, 'Inglés y Francés',
        'Profesora nativa con certificados TEFL y DELF. Especializada en preparación de exámenes oficiales.',
        '2018-02-01');

-- ========================================
-- PROFESORES PARA ACADEMIAS DEL PROPIETARIO 3
-- ========================================

-- Obtener ID de academia "CEI Campus Principal"
SET @academia6 = (SELECT id FROM academia WHERE nombre = 'CEI Campus Principal' LIMIT 1);

-- Profesor 7: Miguel Torres (Inteligencia Artificial)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor7', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'miguel.torres@innovacion.com', 'Miguel', 'Torres Ruiz', 'PROFESOR', 1, 1, @academia6);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia6, 'Inteligencia Artificial y Machine Learning',
        'Doctorado en IA. Experiencia en proyectos de deep learning y procesamiento de lenguaje natural.',
        '2022-01-10');

-- Profesor 8: Elena Jiménez (Data Science)
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor8', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'elena.jimenez@innovacion.com', 'Elena', 'Jiménez Vega', 'PROFESOR', 1, 1, @academia6);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia6, 'Ciencia de Datos y Analytics',
        'Especialista en análisis de datos, Python, R y visualización con Tableau. Experiencia en Big Data.',
        '2022-03-15');

-- ========================================
-- PROFESOR DESACTIVADO PARA PRUEBAS
-- ========================================

-- Profesor 9: Desactivado para probar la funcionalidad
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, activo, email_verificado, academia_id)
VALUES ('profesor9', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
        'inactivo@elitemadrid.com', 'Profesor', 'Inactivo Test', 'PROFESOR', 0, 1, @academia1);

INSERT INTO profesor (usuario_id, academia_id, especialidad, biografia, fecha_contratacion)
VALUES (LAST_INSERT_ID(), @academia1, 'Testing',
        'Profesor desactivado para pruebas del sistema.',
        '2023-01-01');

-- ========================================
-- VERIFICACIÓN
-- ========================================

SELECT 'RESUMEN DE PROFESORES CREADOS' AS Resultado;

SELECT
    'Profesores creados' AS Info,
    COUNT(*) AS Total,
    SUM(CASE WHEN u.activo = 1 THEN 1 ELSE 0 END) AS Activos,
    SUM(CASE WHEN u.activo = 0 THEN 1 ELSE 0 END) AS Inactivos
FROM profesor p
JOIN usuario u ON p.usuario_id = u.id;

SELECT
    'Profesores por academia' AS Info,
    a.nombre AS Academia,
    COUNT(p.id) AS Total_Profesores
FROM academia a
LEFT JOIN profesor p ON p.academia_id = a.id
GROUP BY a.id, a.nombre
ORDER BY a.nombre;

-- ========================================
-- CREDENCIALES DE ACCESO PROFESORES
-- ========================================

SELECT '=== CREDENCIALES DE PROFESORES ===' AS Info;
SELECT '
PROFESORES:
  Todos tienen la contraseña: admin123

  Academia Elite Madrid Centro:
    - profesor1 / admin123 (Juan Martínez - Programación)
    - profesor2 / admin123 (María García - Diseño)
    - profesor9 / admin123 (Inactivo - Testing)

  Academia Elite Barcelona:
    - profesor3 / admin123 (Carlos Rodríguez - BBDD)

  Formación Avanzada Central:
    - profesor4 / admin123 (Laura Sánchez - Marketing)
    - profesor5 / admin123 (Pedro Fernández - Ciberseguridad)

  Formación Avanzada Norte:
    - profesor6 / admin123 (Ana López - Idiomas)

  CEI Campus Principal:
    - profesor7 / admin123 (Miguel Torres - IA)
    - profesor8 / admin123 (Elena Jiménez - Data Science)
' AS Credenciales;

-- ========================================
-- NOTAS
-- ========================================

-- NOTA 1: Todos los profesores tienen contraseña 'admin123' (hash BCrypt)
-- NOTA 2: Todos los emails están verificados
-- NOTA 3: Un profesor está inactivo para pruebas (profesor9)
-- NOTA 4: Cada profesor tiene especialidad y biografía
-- NOTA 5: Las fechas de contratación son variadas para realismo
