# 🚀 Tarjeta de Referencia Rápida - Configuración de Seguridad

## Comandos Rápidos

### Desarrollo Local

```powershell
# Opción 1: Perfil dev (MÁS FÁCIL)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción 2: Con variables de entorno
copy .env.example .env
# Editar .env
.\load-env.ps1
mvn spring-boot:run

# Opción 3: Establecer perfil como variable
$env:SPRING_PROFILES_ACTIVE="dev"
mvn spring-boot:run
```

### IntelliJ IDEA

```
Run > Edit Configurations
Active profiles: dev
```

---

## Archivos Importantes

| Archivo | Descripción | ¿Se sube a Git? |
|---------|-------------|-----------------|
| `application.properties` | Config base | ✅ SÍ |
| `application-dev.properties` | Config desarrollo | ❌ NO |
| `application-prod.properties` | Config producción | ✅ SÍ (sin credenciales) |
| `.env` | Tus credenciales locales | ❌ NO |
| `.env.example` | Plantilla | ✅ SÍ |

---

## Variables de Entorno Esenciales

```properties
# Requeridas para desarrollo
DB_PASSWORD=tu_password_mysql
MAIL_USERNAME=tu_email@gmail.com
MAIL_PASSWORD=tu_app_password_gmail
SPRING_PROFILES_ACTIVE=dev
```

---

## Solución Rápida de Problemas

### ❌ Error: "Property 'DB_PASSWORD' could not be resolved"
```powershell
# Solución: Activar perfil dev
$env:SPRING_PROFILES_ACTIVE="dev"
mvn spring-boot:run
```

### ❌ Error: "Access denied for user 'root'"
```
# Verificar:
1. MySQL corriendo → services.msc
2. Password en application-dev.properties
3. Base de datos existe → acd_gestion_academias
```

### ❌ La aplicación no arranca
```powershell
# 1. Verificar MySQL
Get-Service -Name "MySQL*"

# 2. Verificar perfil activo (debe aparecer en logs)
# The following profiles are active: dev

# 3. Verificar puerto libre
netstat -ano | findstr :8090
```

---

## Perfiles de Spring

| Perfil | Cuándo usar | `ddl-auto` | `show-sql` | Logs |
|--------|-------------|------------|------------|------|
| **dev** | Desarrollo local | update | true | DEBUG |
| **prod** | Producción | validate | false | WARN |
| **test** | Tests automáticos | create-drop | false | WARN |

---

## ⚠️ NUNCA Hacer

- ❌ Subir `.env` a Git
- ❌ Subir `application-dev.properties` a Git (ya está en .gitignore)
- ❌ Usar credenciales de dev en producción
- ❌ Usar `ddl-auto=update` en producción
- ❌ Dejar logs DEBUG en producción

---

## ✅ Siempre Hacer

- ✅ Usar perfil `dev` para desarrollo
- ✅ Usar variables de entorno en producción
- ✅ Cambiar TODAS las contraseñas antes de producción
- ✅ Usar `ddl-auto=validate` en producción
- ✅ Crear usuario de BD con permisos limitados

---

## Documentación Completa

- 📘 [INICIO_RAPIDO.md](INICIO_RAPIDO.md) - Configuración paso a paso
- 🔐 [GUIA_SEGURIDAD_CONFIGURACION.md](GUIA_SEGURIDAD_CONFIGURACION.md) - Guía completa
- 📋 [RESUMEN_CAMBIOS_SEGURIDAD.md](RESUMEN_CAMBIOS_SEGURIDAD.md) - Cambios realizados

---

**Versión**: 1.0  
**Fecha**: Febrero 2026
