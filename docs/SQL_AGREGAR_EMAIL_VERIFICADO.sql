-- Script para agregar el campo email_verificado a la tabla usuario
-- Ejecutar este script si ya tienes una base de datos existente

-- Agregar columna email_verificado con valor por defecto FALSE
ALTER TABLE usuario
ADD COLUMN IF NOT EXISTS email_verificado BOOLEAN NOT NULL DEFAULT FALSE;

-- Actualizar usuarios existentes para que tengan email_verificado = FALSE
UPDATE usuario
SET email_verificado = FALSE
WHERE email_verificado IS NULL;

-- Verificar que todos los usuarios tengan el campo configurado
SELECT id, username, email, email_verificado
FROM usuario;
