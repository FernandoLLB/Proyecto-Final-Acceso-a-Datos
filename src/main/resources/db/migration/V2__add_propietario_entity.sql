-- Migración para añadir la entidad Propietario al modelo SaaS
-- Versión 2.0 - Sistema de Gestión de Academias
-- Fecha: 06/02/2026
--
-- Este script realiza las siguientes operaciones:
-- 1. Crea la tabla propietario
-- 2. Migra los datos existentes (crea un propietario inicial)
-- 3. Añade la columna propietario_id a la tabla academia
-- 4. Establece relaciones y constraints

-- ========================================
-- PASO 1: Crear la tabla propietario
-- ========================================

CREATE TABLE propietario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nif_cif VARCHAR(20),
    razon_social VARCHAR(300),
    fecha_alta DATETIME NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    telefono VARCHAR(20),
    direccion VARCHAR(300),
    CONSTRAINT fk_propietario_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_propietario_usuario ON propietario(usuario_id);
CREATE INDEX idx_propietario_activo ON propietario(activo);
CREATE INDEX idx_propietario_nifcif ON propietario(nif_cif);

-- ========================================
-- PASO 2: Migrar datos existentes
-- ========================================

-- Buscar el primer usuario con rol ADMIN para asociarlo como propietario inicial
-- Este será el propietario de migración que tendrá todas las academias existentes

SET @admin_usuario_id = (SELECT id FROM usuario WHERE rol = 'ADMIN' LIMIT 1);

-- Si existe un usuario ADMIN, crear el propietario de migración
INSERT INTO propietario (usuario_id, razon_social, fecha_alta, activo, nif_cif)
SELECT
    @admin_usuario_id,
    'Migración Inicial - Sistema Legacy',
    NOW(),
    TRUE,
    'MIGRATION-2026'
WHERE @admin_usuario_id IS NOT NULL;

-- Guardar el ID del propietario de migración para usarlo después
SET @propietario_migracion_id = LAST_INSERT_ID();

-- ========================================
-- PASO 3: Añadir columna propietario_id a academia
-- ========================================

-- Añadir la columna (inicialmente nullable para la migración)
ALTER TABLE academia
ADD COLUMN propietario_id BIGINT NULL AFTER direccion;

-- Crear índice temporal
CREATE INDEX idx_academia_propietario ON academia(propietario_id);

-- ========================================
-- PASO 4: Asignar propietario a academias existentes
-- ========================================

-- Asignar todas las academias existentes al propietario de migración
UPDATE academia
SET propietario_id = @propietario_migracion_id
WHERE propietario_id IS NULL
AND @propietario_migracion_id IS NOT NULL;

-- ========================================
-- PASO 5: Establecer constraints finales
-- ========================================

-- Hacer la columna propietario_id NOT NULL (ahora todas las academias tienen propietario)
ALTER TABLE academia
MODIFY COLUMN propietario_id BIGINT NOT NULL;

-- Añadir la foreign key
ALTER TABLE academia
ADD CONSTRAINT fk_academia_propietario
FOREIGN KEY (propietario_id) REFERENCES propietario(id) ON DELETE RESTRICT;

-- ========================================
-- PASO 6: Verificación
-- ========================================

-- Verificar que todas las academias tienen propietario
SELECT
    'Verificación de migración' AS Proceso,
    COUNT(*) AS Total_Academias,
    SUM(CASE WHEN propietario_id IS NOT NULL THEN 1 ELSE 0 END) AS Academias_Con_Propietario,
    (SELECT COUNT(*) FROM propietario) AS Total_Propietarios
FROM academia;

-- ========================================
-- NOTAS IMPORTANTES
-- ========================================

-- 1. Este script crea un propietario inicial asociado al primer usuario ADMIN
-- 2. Todas las academias existentes se asignan a este propietario de migración
-- 3. La columna propietario_id es obligatoria para todas las academias
-- 4. Si no existe ningún usuario ADMIN, el script no creará el propietario de migración
--    y las academias quedarán sin propietario asignado (deberás asignarlo manualmente)

-- ========================================
-- ROLLBACK (en caso de necesitar deshacer los cambios)
-- ========================================

-- Para ejecutar el rollback, ejecuta estos comandos:
--
-- ALTER TABLE academia DROP FOREIGN KEY fk_academia_propietario;
-- ALTER TABLE academia DROP COLUMN propietario_id;
-- DROP TABLE propietario;
