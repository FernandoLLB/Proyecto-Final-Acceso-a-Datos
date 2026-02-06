# 📁 Scripts de Migración de Base de Datos

## 🎯 Orden de Ejecución (Flyway)

Flyway ejecuta automáticamente estos scripts en orden numérico:

### V2__add_propietario_entity.sql
**Propósito:** Agregar entidad Propietario al modelo  
**Fecha:** Inicial  
**Cambios:**
- Crea tabla `propietario`
- Agrega columna `propietario_id` a tabla `academia`
- Migra datos existentes
- Crea constraints y foreign keys

### V3__datos_prueba.sql
**Propósito:** Cargar datos de prueba iniciales  
**Fecha:** Inicial  
**Crea:**
- 1 usuario ADMIN
- 3 propietarios con usuarios
- 6 academias distribuidas entre propietarios
- Academias: 2 para propietario1, 3 para propietario2, 1 para propietario3

**Credenciales:**
```
admin / admin123
propietario1 / admin123 (2 academias)
propietario2 / admin123 (3 academias)
propietario3 / admin123 (1 academia)
```

### V4__fix_academias_huerfanas.sql
**Propósito:** Corregir academias sin propietario  
**Fecha:** Fase de corrección  
**Cambios:**
- Asigna propietario por defecto a academias huérfanas
- Garantiza integridad referencial

### V5__fix_passwords_propietarios.sql
**Propósito:** Corregir contraseñas de propietarios  
**Fecha:** Fase de corrección  
**Cambios:**
- Actualiza passwords a hash BCrypt correcto
- Password: admin123

### V6__datos_profesores.sql ✨ NUEVO
**Propósito:** Cargar profesores de prueba  
**Fecha:** 06/02/2026  
**Crea:**
- 9 profesores distribuidos en 6 academias
- 8 profesores activos + 1 inactivo (para pruebas)
- Especialidades variadas
- Biografías completas

**Credenciales:**
```
profesor1 / admin123 (Juan Martínez - Programación)
profesor2 / admin123 (María García - Diseño)
profesor3 / admin123 (Carlos Rodríguez - BBDD)
profesor4 / admin123 (Laura Sánchez - Marketing)
profesor5 / admin123 (Pedro Fernández - Ciberseguridad)
profesor6 / admin123 (Ana López - Idiomas)
profesor7 / admin123 (Miguel Torres - IA)
profesor8 / admin123 (Elena Jiménez - Data Science)
profesor9 / admin123 (Inactivo - Testing)
```

---

## 🔄 Cómo Aplicar Migraciones

### Automático (Recomendado)
```powershell
# Flyway ejecuta automáticamente al iniciar la aplicación
mvn spring-boot:run
```

### Manual (Si es necesario)
```powershell
# Ejecutar Flyway manualmente
mvn flyway:migrate
```

### Reiniciar Base de Datos
```sql
-- En MySQL
DROP DATABASE IF EXISTS gestor_academias;
CREATE DATABASE gestor_academias CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Luego reiniciar aplicación
mvn spring-boot:run
```

---

## 📊 Estado de Migraciones

### Ver estado actual:
```sql
SELECT * FROM flyway_schema_history ORDER BY installed_rank;
```

### Resultado esperado:
```
| installed_rank | version | description              | success |
|----------------|---------|--------------------------|---------|
| 1              | 2       | add propietario entity   | 1       |
| 2              | 3       | datos prueba             | 1       |
| 3              | 4       | fix academias huerfanas  | 1       |
| 4              | 5       | fix passwords            | 1       |
| 5              | 6       | datos profesores         | 1       |
```

---

## 🚨 Solución de Problemas

### Error: "Migration checksum mismatch"
**Causa:** El archivo SQL fue modificado después de ejecutarse  
**Solución:**
```sql
-- Opción 1: Limpiar y reiniciar
DROP DATABASE gestor_academias;
CREATE DATABASE gestor_academias CHARACTER SET utf8mb4;

-- Opción 2: Reparar (solo si sabes lo que haces)
mvn flyway:repair
```

### Error: "Script V6 no se ejecuta"
**Causa:** La migración anterior falló  
**Solución:**
```sql
-- Ver qué falló
SELECT * FROM flyway_schema_history WHERE success = 0;

-- Corregir manualmente y luego
mvn flyway:repair
mvn spring-boot:run
```

### Error: "Cannot create PoolableConnectionFactory"
**Causa:** Base de datos no existe o credenciales incorrectas  
**Solución:**
```properties
# Verificar application.properties:
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_academias
spring.datasource.username=root
spring.datasource.password=tu_password
```

---

## 📝 Convenciones

### Nombres de Archivos
```
V{número}__{descripción}.sql

Ejemplos:
- V2__add_propietario_entity.sql
- V3__datos_prueba.sql
- V6__datos_profesores.sql
```

### Estructura de Scripts
```sql
-- ========================================
-- Título y descripción
-- ========================================

-- Cambios SQL

-- ========================================
-- Verificación
-- ========================================

-- Consultas de verificación
```

---

## 🎯 Datos de Prueba Actuales

### Usuarios Admin
- **admin** / admin123

### Propietarios
- **propietario1** / admin123 (2 academias)
- **propietario2** / admin123 (3 academias)
- **propietario3** / admin123 (1 academia)

### Profesores
- **profesor1-8** / admin123 (8 activos)
- **profesor9** / admin123 (1 inactivo)

### Academias
- 6 academias activas
- 0 academias inactivas (después de V4)

---

## 📖 Documentación Adicional

- **V6 Detallada:** `docs/Implementacion SaaS/SOLUCION_ERROR_PROFESOR.md`
- **Estado del Sistema:** `docs/Implementacion SaaS/IMPLEMENTACION_FINAL_COMPLETADA.md`

---

**Última actualización:** 06/02/2026  
**Versión actual:** V6
