-- Script de Verificación y Prueba para Dashboard de Alumno
-- Fecha: 29 de enero de 2026

-- ============================================
-- PASO 1: VERIFICACIÓN DE DATOS EXISTENTES
-- ============================================

-- 1.1 Listar todos los alumnos con sus usuarios
SELECT
    a.id as alumno_id,
    u.id as usuario_id,
    u.username,
    u.nombre,
    u.apellidos,
    u.email,
    a.estado_matricula,
    a.academia_id
FROM alumno a
INNER JOIN usuario u ON a.usuario_id = u.id
ORDER BY a.id;

-- 1.2 Verificar matrículas de un alumno específico
-- Reemplazar [ID_ALUMNO] con el ID real
SELECT
    m.id as matricula_id,
    m.alumno_id,
    m.curso_id,
    m.estado,
    m.fecha_matriculacion,
    c.nombre as curso_nombre,
    c.descripcion as curso_descripcion,
    c.duracion_horas,
    c.fecha_inicio,
    c.fecha_fin,
    p.id as profesor_id,
    pu.nombre as profesor_nombre,
    pu.apellidos as profesor_apellidos
FROM matricula m
LEFT JOIN curso c ON m.curso_id = c.id
LEFT JOIN profesor p ON c.profesor_id = p.id
LEFT JOIN usuario pu ON p.usuario_id = pu.id
WHERE m.alumno_id = [ID_ALUMNO];

-- 1.3 Detectar problemas en las relaciones
-- Matrículas sin curso asociado (¡PROBLEMA!)
SELECT m.id, m.alumno_id, m.curso_id
FROM matricula m
LEFT JOIN curso c ON m.curso_id = c.id
WHERE c.id IS NULL;

-- Cursos sin profesor asociado (¡PROBLEMA!)
SELECT c.id, c.nombre, c.profesor_id
FROM curso c
LEFT JOIN profesor p ON c.profesor_id = p.id
WHERE p.id IS NULL;

-- Profesores sin usuario asociado (¡PROBLEMA!)
SELECT p.id, p.usuario_id
FROM profesor p
LEFT JOIN usuario u ON p.usuario_id = u.id
WHERE u.id IS NULL;

-- ============================================
-- PASO 2: CREAR DATOS DE PRUEBA (SI ES NECESARIO)
-- ============================================

-- 2.1 Obtener IDs necesarios para crear datos de prueba
SELECT
    'Academia ID' as tipo,
    id as valor
FROM academia
LIMIT 1;

SELECT
    'Usuario Admin/Secretaria ID' as tipo,
    id as valor
FROM usuario
WHERE rol IN ('ADMIN', 'SECRETARIA')
LIMIT 1;

SELECT
    'Profesor ID' as tipo,
    p.id as valor,
    u.nombre as nombre
FROM profesor p
INNER JOIN usuario u ON p.usuario_id = u.id
LIMIT 1;

-- 2.2 Crear un alumno de prueba (si no existe)
-- NOTA: Ajustar los valores según tu base de datos
/*
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, academia_id, activo)
VALUES (
    'alumno.prueba',
    '{bcrypt}$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', -- password: password
    'alumno.prueba@test.com',
    'Juan',
    'Pérez García',
    'ALUMNO',
    1, -- academia_id
    true
);

INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula, observaciones)
VALUES (
    LAST_INSERT_ID(),
    1, -- academia_id
    NOW(),
    'ACTIVO',
    'Alumno de prueba para testing'
);
*/

-- 2.3 Crear un curso de prueba (si no existe)
-- NOTA: Ajustar profesor_id y academia_id
/*
INSERT INTO curso (
    academia_id,
    nombre,
    descripcion,
    duracion_horas,
    precio,
    fecha_inicio,
    fecha_fin,
    categoria,
    profesor_id,
    plazas_disponibles,
    activo
)
VALUES (
    1, -- academia_id
    'Programación en Java Avanzado',
    'Curso completo de programación en Java con Spring Boot, JPA, y desarrollo web. Aprende a crear aplicaciones empresariales robustas y escalables.',
    120,
    599.99,
    '2026-02-01',
    '2026-06-30',
    'Programación',
    1, -- profesor_id (ajustar según tu BD)
    25,
    true
);
*/

-- 2.4 Matricular al alumno de prueba en el curso
-- NOTA: Ajustar IDs según los datos creados
/*
INSERT INTO matricula (
    academia_id,
    alumno_id,
    curso_id,
    fecha_matriculacion,
    estado,
    observaciones,
    matriculado_por
)
VALUES (
    1, -- academia_id
    [ID_ALUMNO], -- del paso 2.2
    [ID_CURSO], -- del paso 2.3
    NOW(),
    'ACTIVA',
    'Matrícula de prueba',
    1 -- ID del usuario admin/secretaria
);
*/

-- ============================================
-- PASO 3: VERIFICACIÓN POST-CREACIÓN
-- ============================================

-- 3.1 Verificar que todo esté correcto para el alumno de prueba
SELECT
    'Verificación Completa' as paso,
    m.id as matricula_id,
    a.id as alumno_id,
    au.username as alumno_username,
    c.nombre as curso,
    c.duracion_horas,
    c.fecha_inicio,
    c.fecha_fin,
    p.id as profesor_id,
    pu.nombre as profesor_nombre,
    m.estado,
    m.fecha_matriculacion
FROM matricula m
INNER JOIN alumno a ON m.alumno_id = a.id
INNER JOIN usuario au ON a.usuario_id = au.id
INNER JOIN curso c ON m.curso_id = c.id
INNER JOIN profesor p ON c.profesor_id = p.id
INNER JOIN usuario pu ON p.usuario_id = pu.id
WHERE au.username = 'alumno.prueba'; -- Cambiar por el username del alumno

-- 3.2 Contar matrículas por estado para el alumno
SELECT
    a.id as alumno_id,
    u.username,
    COUNT(*) as total_matriculas,
    SUM(CASE WHEN m.estado = 'ACTIVA' THEN 1 ELSE 0 END) as activas,
    SUM(CASE WHEN m.estado = 'COMPLETADA' THEN 1 ELSE 0 END) as completadas,
    SUM(CASE WHEN m.estado = 'CANCELADA' THEN 1 ELSE 0 END) as canceladas,
    SUM(CASE WHEN m.estado = 'SUSPENDIDA' THEN 1 ELSE 0 END) as suspendidas
FROM alumno a
INNER JOIN usuario u ON a.usuario_id = u.id
LEFT JOIN matricula m ON m.alumno_id = a.id
WHERE u.username = 'alumno.prueba' -- Cambiar por el username del alumno
GROUP BY a.id, u.username;

-- ============================================
-- PASO 4: LIMPIEZA (OPCIONAL)
-- ============================================

-- Para eliminar los datos de prueba creados
/*
-- 4.1 Eliminar matrículas de prueba
DELETE FROM matricula
WHERE alumno_id = [ID_ALUMNO_PRUEBA];

-- 4.2 Eliminar curso de prueba (opcional)
DELETE FROM curso
WHERE id = [ID_CURSO_PRUEBA];

-- 4.3 Eliminar alumno de prueba
DELETE FROM alumno
WHERE id = [ID_ALUMNO_PRUEBA];

-- 4.4 Eliminar usuario de prueba
DELETE FROM usuario
WHERE username = 'alumno.prueba';
*/

-- ============================================
-- CONSULTAS ÚTILES PARA DEBUGGING
-- ============================================

-- Listar todos los alumnos con cantidad de matrículas
SELECT
    a.id,
    u.username,
    u.nombre,
    u.apellidos,
    COUNT(m.id) as total_matriculas
FROM alumno a
INNER JOIN usuario u ON a.usuario_id = u.id
LEFT JOIN matricula m ON m.alumno_id = a.id
GROUP BY a.id, u.username, u.nombre, u.apellidos
ORDER BY total_matriculas DESC;

-- Listar cursos disponibles para matricular
SELECT
    c.id,
    c.nombre,
    c.descripcion,
    c.duracion_horas,
    c.fecha_inicio,
    c.fecha_fin,
    pu.nombre as profesor,
    c.plazas_disponibles,
    COUNT(m.id) as matriculas_actuales
FROM curso c
INNER JOIN profesor p ON c.profesor_id = p.id
INNER JOIN usuario pu ON p.usuario_id = pu.id
LEFT JOIN matricula m ON m.curso_id = c.id AND m.estado = 'ACTIVA'
WHERE c.activo = true
GROUP BY c.id, c.nombre, c.descripcion, c.duracion_horas,
         c.fecha_inicio, c.fecha_fin, pu.nombre, c.plazas_disponibles
ORDER BY c.fecha_inicio;
