# Resumen de Cambios de Seguridad

## 📋 Cambios Realizados

Fecha: 3 de Febrero de 2026

### Problema Identificado

Se encontraron **4 vulnerabilidades de seguridad críticas** en `application.properties`:

1. **Línea 2**: URL de base de datos expuesta
2. **Línea 4**: Contraseña de MySQL en texto plano (`sqlRedec1990.`)
3. **Línea 13**: Logging de seguridad en modo DEBUG (expone información sensible)
4. **Línea 25**: Contraseña de Gmail en texto plano (`uggb wvvv fqpo cavd`)

Además:
- `ddl-auto=update` (peligroso en producción)
- `show-sql=true` (expone queries SQL)
- Usuario `root` de MySQL (privilegios excesivos)

### Solución Implementada

Se implementó un sistema de **configuración por perfiles y variables de entorno**.

---

## 📁 Archivos Creados/Modificados

### ✅ Archivos Modificados

1. **`src/main/resources/application.properties`**
   - ✅ Todas las credenciales reemplazadas por variables de entorno
   - ✅ Valores por defecto seguros
   - ✅ Sintaxis: `${VARIABLE:valor_por_defecto}`

2. **`.gitignore`**
   - ✅ Agregado `.env` y archivos sensibles
   - ✅ Agregado `application-dev.properties` (opcional)
   - ✅ Agregado archivos con credenciales

3. **`README.md`**
   - ✅ Sección de seguridad actualizada
   - ✅ Referencias a nueva documentación

### ✅ Archivos Creados

1. **`src/main/resources/application-dev.properties`**
   - Perfil de desarrollo con credenciales actuales
   - NO se sube a Git (está en .gitignore)
   - Uso: `SPRING_PROFILES_ACTIVE=dev`

2. **`src/main/resources/application-prod.properties`**
   - Perfil de producción sin credenciales hardcodeadas
   - Requiere variables de entorno obligatorias
   - Configuración segura (validate, no show-sql, logs WARN)

3. **`.env.example`**
   - Plantilla de variables de entorno
   - Se copia a `.env` y se personaliza
   - `.env` NO se sube a Git

4. **`load-env.ps1`**
   - Script PowerShell para cargar `.env`
   - Facilita desarrollo en Windows
   - Uso: `.\load-env.ps1` antes de ejecutar

5. **`docs/GUIA_SEGURIDAD_CONFIGURACION.md`**
   - Documentación completa de seguridad
   - Guía para desarrollo y producción
   - Mejores prácticas y checklist

6. **`docs/INICIO_RAPIDO.md`**
   - Guía rápida de configuración (2 minutos)
   - Instrucciones para IntelliJ IDEA
   - Solución de problemas comunes

---

## 🔑 Variables de Entorno Definidas

| Variable | Descripción | Requerida | Valor Dev | Valor Prod |
|----------|-------------|-----------|-----------|------------|
| `DB_URL` | URL de MySQL | Sí | localhost:3306 | Variable |
| `DB_USERNAME` | Usuario BD | Sí | root | app_user |
| `DB_PASSWORD` | Contraseña BD | **SÍ** | ⚠️ Sensible | ⚠️ Sensible |
| `MAIL_USERNAME` | Email SMTP | **SÍ** | Gmail | Variable |
| `MAIL_PASSWORD` | Password email | **SÍ** | ⚠️ Sensible | ⚠️ Sensible |
| `JPA_DDL_AUTO` | Modo DDL | No | update | validate |
| `JPA_SHOW_SQL` | Mostrar SQL | No | true | false |
| `LOG_LEVEL_SECURITY` | Log nivel | No | DEBUG | WARN |
| `APP_BASE_URL` | URL app | Sí | localhost:8090 | Variable |
| `SPRING_PROFILES_ACTIVE` | Perfil activo | Sí | dev | prod |

---

## 🚀 Cómo Usar

### Para Desarrollo (3 opciones)

#### Opción 1: Perfil Dev (Recomendado - Más Fácil)
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Opción 2: Variables de Entorno
```powershell
copy .env.example .env
# Editar .env con tus credenciales
.\load-env.ps1
mvn spring-boot:run
```

#### Opción 3: IntelliJ IDEA
1. Run > Edit Configurations
2. Active profiles: `dev`
3. OK y ejecutar

### Para Producción

**NUNCA uses las credenciales de desarrollo**

```bash
# Establecer variables de entorno en el servidor
export SPRING_PROFILES_ACTIVE=prod
export DB_URL="jdbc:mysql://prod-server:3306/academias"
export DB_USERNAME="app_user"
export DB_PASSWORD="contraseña_segura_de_producción"
export MAIL_USERNAME="noreply@tuempresa.com"
export MAIL_PASSWORD="password_prod"
export APP_BASE_URL="https://academias.tuempresa.com"

# Ejecutar
java -jar gestion-academias.jar
```

---

## ✅ Verificación de Seguridad

### Antes (❌ Inseguro)
```properties
spring.datasource.password=sqlRedec1990.
spring.mail.password=uggb wvvv fqpo cavd
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
logging.level.org.springframework.security=DEBUG
```

### Después (✅ Seguro)
```properties
spring.datasource.password=${DB_PASSWORD}
spring.mail.password=${MAIL_PASSWORD}
spring.jpa.hibernate.ddl-auto=${JPA_DDL_AUTO:validate}
spring.jpa.show-sql=${JPA_SHOW_SQL:false}
logging.level.org.springframework.security=${LOG_LEVEL_SECURITY:INFO}
```

---

## 🔒 Mejoras de Seguridad

### ✅ Implementado

1. ✅ Variables de entorno para todas las credenciales
2. ✅ Perfiles de Spring separados (dev, prod)
3. ✅ `.gitignore` actualizado para excluir secretos
4. ✅ Configuración por defecto segura
5. ✅ Documentación completa
6. ✅ Script de carga de variables (PowerShell)
7. ✅ Plantilla `.env.example`

### 🎯 Mejoras Futuras Recomendadas

1. **Flyway/Liquibase**: Migraciones controladas (eliminar `ddl-auto=update`)
2. **Secrets Manager**: AWS Secrets Manager, Azure Key Vault, HashiCorp Vault
3. **Usuario BD específico**: Crear usuario con permisos limitados (no root)
4. **Rotación de credenciales**: Política de cambio periódico
5. **Auditoría**: Logging de accesos y cambios sensibles
6. **HTTPS obligatorio**: SSL/TLS en producción

---

## 📝 Checklist de Despliegue

Antes de desplegar a producción, verificar:

- [ ] Perfil `prod` activado
- [ ] Variables de entorno configuradas en el servidor
- [ ] Credenciales de desarrollo removidas
- [ ] `ddl-auto=validate` (NO update)
- [ ] `show-sql=false`
- [ ] Logs en WARN o INFO
- [ ] Usuario de BD con permisos limitados
- [ ] HTTPS configurado
- [ ] Backup de base de datos
- [ ] Plan de rollback preparado

---

## 📞 Soporte

Para dudas sobre la configuración:
- Ver [INICIO_RAPIDO.md](INICIO_RAPIDO.md)
- Ver [GUIA_SEGURIDAD_CONFIGURACION.md](GUIA_SEGURIDAD_CONFIGURACION.md)
- Contactar al equipo de desarrollo

---

## 🔄 Próximos Pasos

1. **Inmediato**: Probar la aplicación con el perfil `dev`
2. **Corto plazo**: 
   - Implementar Flyway para migraciones
   - Crear usuario de BD con permisos limitados
3. **Largo plazo**:
   - Integrar con gestor de secretos
   - Configurar CI/CD con variables seguras

---

**Última actualización**: 3 de Febrero de 2026
**Estado**: ✅ Implementado y documentado
