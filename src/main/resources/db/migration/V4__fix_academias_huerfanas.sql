-- Script de Corrección: Asignar propietario a academias huérfanas
-- Ejecutar si hay error "Unable to find Propietario with id 0"

-- Verificar academias con problemas
SELECT
    'Academias con propietario_id = 0 o NULL' as Problema,
    COUNT(*) as Total
FROM academia
WHERE propietario_id = 0 OR propietario_id IS NULL;

-- Verificar academias con propietario inexistente
SELECT
    'Academias con propietario inexistente' as Problema,
    COUNT(*) as Total
FROM academia a
LEFT JOIN propietario p ON a.propietario_id = p.id
WHERE p.id IS NULL;

-- Obtener el ID del primer propietario
SET @primer_propietario_id = (SELECT id FROM propietario ORDER BY id LIMIT 1);

-- Mostrar a qué propietario se asignarán
SELECT
    @primer_propietario_id as ID_Propietario,
    razon_social as Razon_Social,
    u.username as Usuario
FROM propietario p
INNER JOIN usuario u ON u.id = p.usuario_id
WHERE p.id = @primer_propietario_id;

-- Corregir academias con propietario_id = 0
UPDATE academia
SET propietario_id = @primer_propietario_id
WHERE propietario_id = 0;

-- Corregir academias con propietario_id NULL
UPDATE academia
SET propietario_id = @primer_propietario_id
WHERE propietario_id IS NULL;

-- Corregir academias con propietario inexistente
UPDATE academia a
LEFT JOIN propietario p ON a.propietario_id = p.id
SET a.propietario_id = @primer_propietario_id
WHERE p.id IS NULL AND a.propietario_id IS NOT NULL;

-- Verificación final
SELECT
    'ESTADO FINAL' as Resultado,
    COUNT(*) as Total_Academias,
    SUM(CASE WHEN propietario_id IS NOT NULL THEN 1 ELSE 0 END) as Con_Propietario,
    SUM(CASE WHEN propietario_id IS NULL OR propietario_id = 0 THEN 1 ELSE 0 END) as Sin_Propietario
FROM academia;

-- Mostrar distribución de academias por propietario
SELECT
    p.razon_social as Propietario,
    u.username as Usuario,
    COUNT(a.id) as Total_Academias
FROM propietario p
INNER JOIN usuario u ON u.id = p.usuario_id
LEFT JOIN academia a ON a.propietario_id = p.id
GROUP BY p.id
ORDER BY p.id;
