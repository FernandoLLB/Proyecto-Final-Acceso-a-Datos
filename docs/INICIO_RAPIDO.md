# Inicio Rápido - Configuración de Desarrollo

## 🚀 Configuración Rápida (2 minutos)

Sigue estos pasos para ejecutar la aplicación en modo desarrollo:

### Opción 1: Usar Perfil de Desarrollo (MÁS FÁCIL)

El perfil de desarrollo ya tiene tus credenciales configuradas.

**En IntelliJ IDEA:**
1. Abre **Run > Edit Configurations**
2. En **Active profiles**, escribe: `dev`
3. Click en **OK**
4. Ejecuta la aplicación normalmente

**Desde línea de comandos:**
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

✅ ¡Listo! La aplicación usará `application-dev.properties`

---

### Opción 2: Usar Variables de Entorno (Más seguro)

Si prefieres no tener credenciales en archivos:

1. **Copia el archivo de ejemplo:**
   ```powershell
   copy .env.example .env
   ```

2. **Edita `.env` con tus credenciales:**
   ```properties
   DB_PASSWORD=sqlRedec1990.
   MAIL_USERNAME=fernandolloretb@gmail.com
   MAIL_PASSWORD=uggb wvvv fqpo cavd
   SPRING_PROFILES_ACTIVE=dev
   ```

3. **Carga las variables y ejecuta:**
   ```powershell
   .\load-env.ps1
   mvn spring-boot:run
   ```

---

## ⚙️ Configurar en IntelliJ IDEA (Método completo)

1. **Run > Edit Configurations**
2. Selecciona tu configuración de Spring Boot
3. En **Environment variables**, añade:
   ```
   SPRING_PROFILES_ACTIVE=dev
   ```
   O si usas variables:
   ```
   DB_PASSWORD=tu_password;MAIL_PASSWORD=tu_mail_password;SPRING_PROFILES_ACTIVE=dev
   ```
4. En **Active profiles** (pestaña Spring Boot), escribe: `dev`
5. Click **OK** y ejecuta

---

## 📋 Verificación

Cuando la aplicación arranque, deberías ver en los logs:

```
The following profiles are active: dev
```

Y la aplicación estará disponible en: http://localhost:8090

---

## ⚠️ Importante

- ✅ El archivo `application-dev.properties` está en `.gitignore` (no se subirá a Git)
- ✅ El archivo `.env` está en `.gitignore` (no se subirá a Git)
- ❌ NUNCA subas credenciales reales a Git
- ❌ NUNCA uses estas credenciales en producción

---

## 🆘 Problemas Comunes

### Error: "Property 'DB_PASSWORD' could not be resolved"

**Solución**: Estás usando el perfil incorrecto. Activa el perfil `dev`:
```powershell
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Error: "Access denied for user 'root'@'localhost'"

**Solución**: Verifica que MySQL esté corriendo y la contraseña en `application-dev.properties` sea correcta.

### La aplicación no arranca

**Solución**: 
1. Verifica que MySQL esté corriendo en el puerto 3306
2. Verifica que la base de datos `acd_gestion_academias` exista
3. Verifica las credenciales en `application-dev.properties`

---

## 📚 Más Información

Para configuración de producción y detalles de seguridad, lee:
- [GUIA_SEGURIDAD_CONFIGURACION.md](GUIA_SEGURIDAD_CONFIGURACION.md)
