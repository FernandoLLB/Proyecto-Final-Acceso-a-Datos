# ⚠️ SOLUCIÓN RÁPIDA - Error "Unable to find Propietario with id 0"

## 🐛 Problema

Si ves este error al acceder a `/admin/dashboard`:

```
org.springframework.orm.jpa.JpaObjectRetrievalFailureException: Unable to find es.fempa.acd.demosecurityproductos.model.Propietario with id 0
```

## ✅ Causa

Hay academias en la base de datos que tienen `propietario_id = 0` o `NULL`, y ese propietario no existe.

## 🔧 Solución (Elige una)

### Opción 1: Ejecutar script de corrección automático

```powershell
# Desde PowerShell en la carpeta del proyecto
Get-Content src/main/resources/db/migration/V4__fix_academias_huerfanas.sql | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p acd_gestion_academias
```

### Opción 2: Desde MySQL Workbench

1. Abre MySQL Workbench
2. Conecta a tu servidor
3. Abre el archivo: `src/main/resources/db/migration/V4__fix_academias_huerfanas.sql`
4. Click en el rayo ⚡ para ejecutar
5. Verás el resumen de academias corregidas

### Opción 3: Comando SQL directo

```sql
-- Conectar a MySQL
USE acd_gestion_academias;

-- Asignar todas las academias huérfanas al primer propietario
UPDATE academia 
SET propietario_id = (SELECT id FROM propietario ORDER BY id LIMIT 1)
WHERE propietario_id = 0 OR propietario_id IS NULL;

-- Verificar que ya no hay academias sin propietario
SELECT COUNT(*) FROM academia WHERE propietario_id = 0 OR propietario_id IS NULL;
```

### Opción 4: Desde PowerShell (comando único)

```powershell
echo "UPDATE academia SET propietario_id = (SELECT id FROM propietario ORDER BY id LIMIT 1) WHERE propietario_id = 0 OR propietario_id IS NULL;" | & "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p acd_gestion_academias
```

## ✅ Verificación

Después de ejecutar cualquiera de las soluciones, verifica:

```sql
-- Debe devolver 0
SELECT COUNT(*) as Academias_Problema 
FROM academia 
WHERE propietario_id = 0 OR propietario_id IS NULL;

-- Ver distribución de academias
SELECT 
    p.razon_social as Propietario,
    COUNT(a.id) as Total_Academias
FROM propietario p
LEFT JOIN academia a ON a.propietario_id = p.id
GROUP BY p.id;
```

## 🚀 Reiniciar la Aplicación

Después de corregir la BD:

1. **Detener la aplicación** (Ctrl+C si está en PowerShell, o Stop en el IDE)
2. **Reiniciar:**
   - Desde IDE: Run → `GestionAcademiasApplication.java`
   - Desde PowerShell: `mvn spring-boot:run`
3. **Acceder a:** http://localhost:8090
4. **Login:** `admin / admin123`

## ℹ️ ¿Por qué pasó esto?

Este problema ocurre cuando:
- Ya existía una academia en la BD antes de la migración
- La academia no fue correctamente asignada a un propietario
- Se insertó una academia manualmente con `propietario_id = 0`

## 🎯 Prevención

El script `V2__add_propietario_entity.sql` debería haber asignado automáticamente todas las academias al "propietario de migración", pero si ya tenías datos en la BD o ejecutaste scripts en otro orden, algunas academias pueden haber quedado sin propietario.

El script `V4__fix_academias_huerfanas.sql` corrige este problema de forma permanente.

## 📞 Más Ayuda

Si el problema persiste:

1. Verifica que el script se ejecutó correctamente
2. Verifica que existen propietarios en la BD: `SELECT * FROM propietario;`
3. Si no hay propietarios, ejecuta `V3__datos_prueba.sql` primero
4. Luego ejecuta `V4__fix_academias_huerfanas.sql`

---

**Fecha:** 06/02/2026  
**Script de corrección:** `V4__fix_academias_huerfanas.sql`  
**Estado:** ✅ Solución verificada y probada
