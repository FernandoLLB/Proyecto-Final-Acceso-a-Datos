-- Script de Corrección: Actualizar contraseñas de propietarios
-- Problema: Las contraseñas no están correctamente encriptadas
-- Solución: Actualizar con hash BCrypt correcto para "admin123"

-- Hash BCrypt para "admin123": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy

-- Actualizar contraseña del propietario1
UPDATE usuario
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'propietario1';

-- Actualizar contraseña del propietario2
UPDATE usuario
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'propietario2';

-- Actualizar contraseña del propietario3
UPDATE usuario
SET password = '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy'
WHERE username = 'propietario3';

-- Verificar que las contraseñas se actualizaron
SELECT
    username,
    rol,
    SUBSTRING(password, 1, 20) as password_hash_inicio,
    activo,
    email_verificado
FROM usuario
WHERE rol = 'PROPIETARIO'
ORDER BY username;

SELECT 'Contraseñas actualizadas. Todos los propietarios pueden usar: admin123' as Resultado;
