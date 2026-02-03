# Guía de Seguridad y Configuración

## 🔐 Configuración de Secretos y Variables de Entorno

### Problema Identificado

La aplicación tenía las siguientes vulnerabilidades de seguridad en `application.properties`:

1. **Credenciales de base de datos expuestas**: Usuario `root` y contraseña en texto plano
2. **Contraseña de email expuesta**: App password de Gmail en texto plano  
3. **Configuración de desarrollo en producción**: `ddl-auto=update`, `show-sql=true`
4. **Logging excesivo**: Security DEBUG habilitado

### Solución Implementada

Se ha implementado un sistema de perfiles de Spring con variables de entorno:

#### 1. Archivos de Configuración

- **`application.properties`**: Configuración base con variables de entorno
- **`application-dev.properties`**: Configuración para desarrollo (NO subir a Git)
- **`application-prod.properties`**: Configuración para producción
- **`.env.example`**: Plantilla de variables de entorno

#### 2. Variables de Entorno

Todas las credenciales y configuraciones sensibles ahora usan variables de entorno:

| Variable | Descripción | Ejemplo Dev | Ejemplo Prod |
|----------|-------------|-------------|--------------|
| `DB_URL` | URL de la base de datos | `jdbc:mysql://localhost:3306/...` | URL de DB en servidor |
| `DB_USERNAME` | Usuario de base de datos | `root` | Usuario específico con permisos limitados |
| `DB_PASSWORD` | Contraseña de base de datos | ⚠️ Valor sensible | ⚠️ Valor sensible |
| `MAIL_USERNAME` | Email para envío | `tu_email@gmail.com` | Email corporativo |
| `MAIL_PASSWORD` | Contraseña de aplicación | ⚠️ Valor sensible | ⚠️ Valor sensible |
| `JPA_DDL_AUTO` | Modo de gestión del esquema | `update` | `validate` (NUNCA update) |
| `JPA_SHOW_SQL` | Mostrar SQL en logs | `true` | `false` |
| `LOG_LEVEL_SECURITY` | Nivel de log de seguridad | `DEBUG` | `WARN` |
| `APP_BASE_URL` | URL base de la aplicación | `http://localhost:8090` | `https://tudominio.com` |

## 📝 Configuración para Desarrollo

### Opción 1: Usar el Perfil de Desarrollo (Recomendado para desarrollo local)

El archivo `application-dev.properties` ya contiene tus credenciales de desarrollo.

**Ejecutar con perfil dev:**

```bash
# En IntelliJ IDEA
# Run > Edit Configurations > Active profiles: dev

# Desde línea de comandos
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# O establecer variable de entorno
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Opción 2: Variables de Entorno (Recomendado para CI/CD)

1. **Crea tu archivo `.env` local** (copiando desde `.env.example`):
   ```bash
   copy .env.example .env
   ```

2. **Edita `.env` con tus credenciales reales**:
   ```properties
   DB_PASSWORD=tu_contraseña_mysql
   MAIL_USERNAME=tu_email@gmail.com
   MAIL_PASSWORD=tu_app_password_gmail
   SPRING_PROFILES_ACTIVE=dev
   ```

3. **Carga las variables antes de ejecutar**:
   ```powershell
   # PowerShell (Windows)
   Get-Content .env | ForEach-Object {
       if ($_ -match '^([^=]+)=(.*)$') {
           [Environment]::SetEnvironmentVariable($matches[1], $matches[2], 'Process')
       }
   }
   mvn spring-boot:run
   ```

### Opción 3: Configurar en IntelliJ IDEA

1. **Run > Edit Configurations**
2. **Environment variables**: 
   ```
   DB_PASSWORD=tu_contraseña;MAIL_PASSWORD=tu_app_password;SPRING_PROFILES_ACTIVE=dev
   ```
3. **Active profiles**: `dev`

## 🚀 Configuración para Producción

### ⚠️ IMPORTANTE: Nunca uses credenciales reales en archivos

### 1. Variables de Entorno del Sistema

En el servidor de producción, configura las variables de entorno:

**Linux/Unix:**
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_URL=jdbc:mysql://db-server:3306/acd_gestion_academias
export DB_USERNAME=app_user
export DB_PASSWORD=contraseña_segura_aquí
export MAIL_HOST=smtp.tuempresa.com
export MAIL_USERNAME=noreply@tuempresa.com
export MAIL_PASSWORD=contraseña_email_aquí
export APP_BASE_URL=https://academias.tuempresa.com
```

**Windows (PowerShell):**
```powershell
$env:SPRING_PROFILES_ACTIVE="prod"
$env:DB_URL="jdbc:mysql://db-server:3306/acd_gestion_academias"
$env:DB_USERNAME="app_user"
$env:DB_PASSWORD="contraseña_segura_aquí"
# ... etc
```

### 2. Configuración en Servidor de Aplicaciones

**Tomcat** - Editar `setenv.sh` o `setenv.bat`:
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_PASSWORD="contraseña_aquí"
```

**Docker** - Archivo `docker-compose.yml`:
```yaml
services:
  app:
    image: gestion-academias:latest
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_URL=jdbc:mysql://db:3306/acd_gestion_academias
      - DB_USERNAME=app_user
      - DB_PASSWORD=${DB_PASSWORD}
    env_file:
      - .env.prod  # Archivo con secretos (NO subir a Git)
```

### 3. Gestores de Secretos (Recomendado para Producción)

Para entornos empresariales, considera usar:

- **AWS Secrets Manager**
- **Azure Key Vault**
- **HashiCorp Vault**
- **Spring Cloud Config Server**

Ejemplo con AWS Secrets Manager:
```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-aws-secrets-manager-config</artifactId>
</dependency>
```

## 🔒 Mejores Prácticas de Seguridad

### ✅ Hacer:

1. **Usar perfiles de Spring**: `dev`, `prod`, `test`
2. **Variables de entorno** para todos los secretos
3. **`ddl-auto=validate`** en producción (NUNCA `update` o `create-drop`)
4. **Logs mínimos** en producción (`WARN` o `ERROR`)
5. **Usuario de BD con permisos limitados** en producción (no root)
6. **HTTPS** en producción con certificados válidos
7. **Cambiar todas las contraseñas** antes de desplegar
8. **Rotar credenciales** periódicamente

### ❌ NO Hacer:

1. ❌ Subir archivos con contraseñas a Git
2. ❌ Usar contraseñas de desarrollo en producción
3. ❌ Usar `root` o usuarios con privilegios excesivos
4. ❌ Exponer stack traces en producción
5. ❌ Dejar `show-sql=true` en producción
6. ❌ Usar `ddl-auto=update` en producción
7. ❌ Mantener logs DEBUG/TRACE en producción

## 📋 Checklist de Despliegue

Antes de desplegar a producción:

- [ ] Variables de entorno configuradas en el servidor
- [ ] Perfil `prod` activado (`SPRING_PROFILES_ACTIVE=prod`)
- [ ] Credenciales de desarrollo removidas
- [ ] Usuario de BD con permisos mínimos necesarios
- [ ] `ddl-auto=validate` (verificar esquema sin modificarlo)
- [ ] `show-sql=false`
- [ ] Logs en nivel `WARN` o `INFO`
- [ ] HTTPS configurado con certificados válidos
- [ ] URL base apunta al dominio de producción
- [ ] Backup de la base de datos
- [ ] Plan de rollback preparado

## 🔄 Migración de Base de Datos

**IMPORTANTE**: En producción, NO uses `ddl-auto=update`

### Usar Flyway o Liquibase para migraciones controladas:

**Flyway** (recomendado):

1. Agregar dependencia:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

2. Crear scripts de migración en `src/main/resources/db/migration/`:
   - `V1__initial_schema.sql`
   - `V2__add_email_verification.sql`
   - etc.

3. Configurar:
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true
spring.flyway.baseline-on-migrate=true
```

## 🆘 Solución de Problemas

### Error: "Property 'DB_PASSWORD' could not be resolved"

**Solución**: Define la variable de entorno o usa el perfil `dev`:
```bash
set SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Error: "Access denied for user 'root'@'localhost'"

**Solución**: 
1. Verifica que MySQL esté corriendo
2. Verifica la contraseña en `application-dev.properties` o variable de entorno
3. Verifica permisos del usuario

### La aplicación usa configuración incorrecta

**Solución**: Verifica el perfil activo:
```bash
# Ver qué perfil está activo en los logs:
# The following profiles are active: dev
```

## 📚 Referencias

- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Spring Profiles](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles)
- [Flyway Migrations](https://flywaydb.org/documentation/)
- [OWASP Top 10 - Sensitive Data Exposure](https://owasp.org/www-project-top-ten/)

## 📞 Contacto

Para preguntas sobre la configuración de seguridad, contacta al equipo de DevOps o al administrador del sistema.

---

**Última actualización**: Febrero 2026
