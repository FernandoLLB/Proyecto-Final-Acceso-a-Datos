# Sistema de Verificación de Email - Guía de Implementación

## 📧 Descripción General

Se ha implementado un sistema completo de verificación de email para el registro de alumnos en el Gestor de Academias. Cuando un alumno se registra, recibe un correo electrónico con un enlace de verificación que debe usar antes de poder iniciar sesión.

## 🔧 Componentes Implementados

### 1. **Configuración del Servidor de Email**

**Archivo**: `src/main/resources/application.properties`

Se agregaron las siguientes propiedades para configurar Gmail SMTP:

```properties
# Configuración de correo electrónico
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=fernandolloretb@gmail.com
spring.mail.password=uggb wvvv fqpo cavd
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000

# URL base de la aplicación para links de verificación
app.base.url=http://localhost:8090
```

### 2. **Entidad TokenVerificacion**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/model/TokenVerificacion.java`

- Almacena tokens únicos asociados a usuarios
- Token válido por 24 horas
- Relación OneToOne con Usuario
- Método `isExpirado()` para validar vigencia

### 3. **Repositorio TokenVerificacionRepository**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/repository/TokenVerificacionRepository.java`

Métodos para:
- Buscar por token
- Buscar por usuario
- Eliminar por usuario

### 4. **Servicio EmailService**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/service/EmailService.java`

Métodos principales:
- `enviarEmailVerificacion()`: Envía email HTML con enlace de verificación
- `enviarEmailBienvenida()`: Envía email de bienvenida tras verificación exitosa

### 5. **Servicio TokenVerificacionService**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/service/TokenVerificacionService.java`

Métodos principales:
- `crearTokenVerificacion(Usuario usuario)`: Genera token UUID y envía email
- `verificarToken(String token)`: Valida token y activa cuenta
- `reenviarEmailVerificacion(String email)`: Reenvía email si no se recibió

### 6. **Modificaciones en Usuario**

El modelo `Usuario` ya tenía los campos necesarios:
- `emailVerificado` (Boolean): Indica si el email fue verificado
- `activo` (Boolean): Indica si la cuenta está activa

### 7. **Modificaciones en AuthController**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/controller/AuthController.java`

Endpoints agregados:
- `GET /verificar-email?token={token}`: Verifica el email con el token
- `GET /reenviar-verificacion`: Muestra formulario para reenviar email
- `POST /reenviar-verificacion`: Procesa el reenvío de email

**Modificación en `/registro`**:
Después de crear el usuario y alumno, se llama a `tokenVerificacionService.crearTokenVerificacion(usuario)` para generar y enviar el email.

### 8. **Modificaciones en CustomUserDetailsService**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/service/CustomUserDetailsService.java`

Se agregó validación en `loadUserByUsername()`:
- Verifica que `emailVerificado` sea `true` antes de permitir login
- Excepción: ADMIN y PROPIETARIO no requieren verificación
- También verifica que la cuenta esté activa

### 9. **Modificaciones en SecurityConfig**

**Archivo**: `src/main/java/es/fempa/acd/demosecurityproductos/config/SecurityConfig.java`

Se permitió acceso público a:
- `/verificar-email`
- `/reenviar-verificacion`

### 10. **Vista reenviar-verificacion.html**

**Archivo**: `src/main/resources/templates/reenviar-verificacion.html`

Formulario simple para reenviar el email de verificación.

### 11. **Modificaciones en login.html**

**Archivo**: `src/main/resources/templates/login.html`

Se agregaron alertas para:
- Email verificado exitosamente
- Registro completado (con mensaje de revisar email)
- Mensajes de error/éxito generales

## 🔄 Flujo de Verificación

### Registro de Nuevo Alumno:

1. Usuario completa formulario en `/registro`
2. Sistema crea Usuario y Alumno con `emailVerificado = false`
3. Se genera token UUID único
4. Se guarda TokenVerificacion en base de datos
5. Se envía email HTML con enlace: `http://localhost:8090/verificar-email?token={UUID}`
6. Usuario es redirigido a `/login` con mensaje de "revisar email"

### Verificación:

1. Usuario hace clic en enlace del email
2. Sistema busca token en base de datos
3. Si token existe y no expiró:
   - Marca `emailVerificado = true`
   - Elimina token usado
   - Envía email de bienvenida
   - Redirige a `/login` con mensaje de éxito
4. Si token no existe o expiró:
   - Redirige a `/login` con mensaje de error

### Reenvío de Email:

1. Usuario visita `/reenviar-verificacion`
2. Ingresa su email
3. Sistema busca usuario por email
4. Verifica que no esté ya verificado
5. Elimina token anterior (si existe)
6. Genera nuevo token y envía email

### Inicio de Sesión:

1. Usuario ingresa credenciales
2. `CustomUserDetailsService` verifica:
   - Usuario existe
   - Contraseña correcta
   - Email verificado (excepto ADMIN/PROPIETARIO)
   - Cuenta activa
3. Si email no verificado: error "Debes verificar tu email..."
4. Si todo OK: permite acceso

## 📋 Dependencia Maven

**Archivo**: `pom.xml`

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

## ⚙️ Configuración de Gmail

**Importante**: El código de aplicación `uggb wvvv fqpo cavd` se obtiene desde:
1. Google Account > Seguridad
2. Verificación en dos pasos (debe estar activada)
3. Contraseñas de aplicaciones
4. Generar nueva contraseña para "Correo"

## 🗄️ Cambios en Base de Datos

Al iniciar la aplicación, Hibernate creará automáticamente la tabla `token_verificacion` con:
- `id` (BIGINT, PK)
- `token` (VARCHAR, UNIQUE)
- `usuario_id` (BIGINT, FK)
- `fecha_creacion` (DATETIME)
- `fecha_expiracion` (DATETIME)

La tabla `usuario` ya tenía el campo `email_verificado`.

## 🔒 Seguridad

- Tokens son UUID aleatorios (imposibles de adivinar)
- Tokens expiran en 24 horas
- Tokens se eliminan después de uso
- Solo un token activo por usuario
- Verificación obligatoria para ALUMNO, PROFESOR, SECRETARIA
- Exención para ADMIN y PROPIETARIO

## 📧 Diseño de Emails

Los emails enviados son HTML responsivos con:
- Header con logo y título
- Botón de acción (verificar cuenta / iniciar sesión)
- Link alternativo (por si botón no funciona)
- Información de expiración
- Footer con copyright

## 🧪 Pruebas

Para probar el sistema:

1. **Registrar nuevo alumno**:
   - Ir a http://localhost:8090/registro
   - Completar formulario
   - Verificar que aparece mensaje "Te hemos enviado un email..."

2. **Verificar email**:
   - Revisar bandeja de entrada del email proporcionado
   - Hacer clic en "Verificar mi cuenta"
   - Verificar redirección a login con mensaje de éxito

3. **Intentar login sin verificar**:
   - Registrar usuario pero NO verificar email
   - Intentar hacer login
   - Verificar mensaje: "Debes verificar tu email..."

4. **Reenviar verificación**:
   - Ir a http://localhost:8090/reenviar-verificacion
   - Ingresar email
   - Verificar que llega nuevo email

## 🚨 Posibles Problemas y Soluciones

### Email no llega:

1. Verificar spam/correo no deseado
2. Verificar configuración SMTP en `application.properties`
3. Verificar que "Verificación en dos pasos" esté activa en Google
4. Regenerar contraseña de aplicación
5. Verificar logs de la aplicación para errores

### Token expirado:

- Usuario debe solicitar reenvío en `/reenviar-verificacion`

### Usuario bloqueado:

- Si olvidó verificar y han pasado 24h, debe reenviar email

## 📝 Notas Adicionales

- El sistema NO bloquea ADMIN ni PROPIETARIO para evitar problemas de acceso
- Los usuarios creados desde el panel de administración también requerirán verificación (si son ALUMNO/PROFESOR/SECRETARIA)
- La URL base (`app.base.url`) debe actualizarse al desplegar en producción
- Los timeouts de SMTP están configurados en 5 segundos

## 🎯 Próximas Mejoras Sugeridas

1. Agregar expiración de cuentas no verificadas (auto-eliminar después de X días)
2. Limitar número de reenvíos por hora (prevenir spam)
3. Agregar verificación de email al cambiar email de cuenta existente
4. Implementar templates de email más personalizables
5. Agregar logs detallados de envío de emails
6. Implementar cola de emails para mejor rendimiento
