# 📧 SISTEMA DE REGISTRO Y VERIFICACIÓN POR EMAIL - MODELO SAAS

## 🎯 Funcionamiento Actual

El sistema de registro de alumnos **ya está adaptado al modelo SaaS** y funciona correctamente con las siguientes características:

### ✅ Flujo de Registro Completo

```
1. Alumno accede a /registro
   ↓
2. Ve formulario con:
   - Datos personales (nombre, apellidos)
   - Credenciales (username, email, password)
   - **Selector de ACADEMIAS** ← ✅ Adaptado a SaaS
   ↓
3. Alumno selecciona la academia a la que quiere inscribirse
   ↓
4. El sistema:
   - Crea el usuario con rol ALUMNO
   - Lo asocia a la academia seleccionada
   - Crea el registro de alumno
   - Genera token de verificación
   - **Envía email de verificación** ← ✅ Implementado
   ↓
5. Alumno recibe email
   ↓
6. Click en enlace de verificación
   ↓
7. Cuenta activada → Puede iniciar sesión
```

---

## 📊 Selector de Academias en Registro

### Implementación Actual ✅

El formulario de registro (`registro.html`) incluye:

```html
<select class="form-control" id="academiaId" required>
    <option value="">Selecciona una academia</option>
    <option th:each="academia : ${academias}"
            th:value="${academia.id}"
            th:text="${academia.nombre + (academia.direccion != null ? ' - ' + academia.direccion : '')}">
    </option>
</select>
```

**Características:**
- ✅ Lista **TODAS las academias activas** del sistema
- ✅ Muestra nombre y dirección de cada academia
- ✅ Campo **obligatorio** - el alumno debe elegir una
- ✅ Solo academias activas son visibles
- ✅ Información adicional sobre el proceso de verificación

### ¿Por qué todas las academias?

En el modelo SaaS, el registro es **público** para que cualquier persona pueda inscribirse en cualquier academia disponible:

```
ACADEMIA 1 (Propietario A)
  ← Alumnos pueden registrarse aquí
  
ACADEMIA 2 (Propietario A)
  ← Alumnos pueden registrarse aquí
  
ACADEMIA 3 (Propietario B)
  ← Alumnos pueden registrarse aquí
```

Esto es correcto porque:
- 📚 Cada academia es un **cliente independiente** que quiere captar alumnos
- 🌍 El registro es **público** para facilitar la inscripción
- 🎓 Los alumnos eligen libremente a qué academia inscribirse

---

## 🔒 Sistema de Verificación por Email

### Funcionamiento

#### 1. **Registro**
```java
// En AuthController.java
Usuario usuario = usuarioService.crearUsuarioAlumno(
    username, password, email, nombre, apellidos, academia
);

// Crear token y enviar email
tokenVerificacionService.crearTokenVerificacion(usuario);
```

#### 2. **Token Generado**
- Se crea un token único aleatorio
- Se asocia al usuario
- Se establece fecha de expiración (24 horas)
- Se envía por email

#### 3. **Email Enviado**
El alumno recibe un email con:
```
Asunto: Verifica tu cuenta en Gestor de Academias

Hola [Nombre],

Haz clic en el siguiente enlace para verificar tu cuenta:
http://localhost:8090/verificar-email?token=[TOKEN_ÚNICO]

Este enlace expira en 24 horas.
```

#### 4. **Verificación**
```
Usuario hace click → /verificar-email?token=abc123
  ↓
Sistema verifica el token
  ↓
Si válido: usuario.setEmailVerificado(true)
  ↓
Usuario puede iniciar sesión
```

---

## 📝 Archivos Involucrados

### Backend

#### AuthController.java
```java
@GetMapping("/registro")
public String mostrarFormularioRegistro(Model model) {
    model.addAttribute("registroDTO", new RegistroAlumnoDTO());
    List<Academia> academias = academiaService.listarActivasParaRegistro();
    model.addAttribute("academias", academias);
    return "registro";
}

@PostMapping("/registro")
public String procesarRegistro(@ModelAttribute("registroDTO") RegistroAlumnoDTO dto) {
    // 1. Crear usuario
    // 2. Crear alumno
    // 3. Enviar email de verificación
    tokenVerificacionService.crearTokenVerificacion(usuario);
    return "redirect:/login";
}
```

#### AcademiaService.java
```java
// Método público sin autenticación para registro
public List<Academia> listarActivasParaRegistro() {
    return academiaRepository.findByActivaTrue();
}
```

#### TokenVerificacionService.java
```java
public void crearTokenVerificacion(Usuario usuario) {
    // Genera token único
    // Envía email con enlace
    // Guarda token en BD
}

public boolean verificarToken(String token) {
    // Valida token
    // Verifica expiración
    // Activa usuario
}
```

### Frontend

#### registro.html
- Formulario con selector de academias
- Información sobre verificación de email
- Validaciones de campos

#### login.html
- Mensaje de éxito tras registro
- Enlace para reenviar verificación
- Mensaje de confirmación tras verificar email

---

## 🎯 Mensajes al Usuario

### En Registro (registro.html)
```
┌─────────────────────────────────────────┐
│ ℹ️ Verificación por Email              │
│                                         │
│ Tras el registro, recibirás un email   │
│ de verificación. Debes confirmar tu    │
│ cuenta antes de poder iniciar sesión.  │
└─────────────────────────────────────────┘
```

### Tras Registrarse (login.html)
```
┌─────────────────────────────────────────┐
│ ✉️ ¡Registro Completado!               │
│                                         │
│ Te hemos enviado un email de           │
│ verificación a tu@correo.com           │
│                                         │
│ Por favor, revisa tu bandeja y haz     │
│ clic en el enlace para activar tu      │
│ cuenta.                                 │
│                                         │
│ ¿No recibiste el email?                │
│ Reenviar email de verificación         │
└─────────────────────────────────────────┘
```

### Tras Verificar (login.html)
```
┌─────────────────────────────────────────┐
│ ✅ ¡Email Verificado!                  │
│                                         │
│ Tu cuenta ha sido verificada           │
│ exitosamente. Ya puedes iniciar        │
│ sesión.                                 │
└─────────────────────────────────────────┘
```

---

## 🔐 Seguridad Implementada

### Validaciones en Registro
```java
// 1. Contraseñas coinciden
if (!password.equals(confirmPassword)) {
    throw error;
}

// 2. Longitud mínima
if (password.length() < 3) {
    throw error;
}

// 3. Academia seleccionada
if (academiaId == null) {
    throw error;
}

// 4. Academia activa
Academia academia = obtenerPorIdParaRegistro(academiaId);
if (!academia.getActiva()) {
    throw error;
}
```

### Verificación de Token
```java
// 1. Token existe
TokenVerificacion token = findByToken(tokenStr);

// 2. No expirado
if (token.getFechaExpiracion().isBefore(LocalDateTime.now())) {
    throw error;
}

// 3. No usado previamente
if (token.isUsado()) {
    throw error;
}
```

---

## 🧪 Cómo Probar el Sistema

### Paso 1: Acceder al Registro
```
URL: http://localhost:8090/registro
```

### Paso 2: Completar Formulario
```
Nombre: Juan
Apellidos: Pérez
Email: juan@test.com
Usuario: juanperez
Password: 123456
Confirmar Password: 123456
Academia: [Seleccionar de la lista] ← ✅ Importante
```

### Paso 3: Enviar Registro
- Click en "Registrarse"
- Serás redirigido al login
- Verás mensaje: "Te hemos enviado un email..."

### Paso 4: Verificar Email
```
⚠️ IMPORTANTE: Revisa los logs de la aplicación
```

Si tienes un servidor SMTP configurado:
- Recibirás el email en tu bandeja
- Click en el enlace

Si NO tienes SMTP configurado:
- El token aparece en los logs de la consola
- Busca: "Token de verificación: [TOKEN]"
- Accede manualmente: `/verificar-email?token=[TOKEN]`

### Paso 5: Iniciar Sesión
```
Usuario: juanperez
Password: 123456
```

✅ Si el email está verificado → acceso permitido
❌ Si NO está verificado → "Email no verificado"

---

## ⚙️ Configuración SMTP (Opcional)

Para enviar emails reales, configura en `application.properties`:

```properties
# Email Configuration
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu_email@gmail.com
spring.mail.password=tu_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# URL base para enlaces de verificación
app.base.url=http://localhost:8090
```

---

## 🎯 Mejoras Implementadas

### 1. Selector de Academia Mejorado ✅
```html
<!-- ANTES -->
<option th:text="${academia.nombre}">Academia</option>

<!-- AHORA -->
<option th:text="${academia.nombre + ' - ' + academia.direccion}">
    Academia XYZ - Calle Principal 123
</option>
```

### 2. Información sobre Verificación ✅
```html
<div class="alert alert-info">
    <i class="bi bi-envelope-check"></i>
    <strong>Verificación por Email</strong>
    <p>Tras el registro, recibirás un email de verificación...</p>
</div>
```

### 3. Mensajes Detallados ✅
- Email enviado a qué dirección
- Enlace para reenviar si no llegó
- Confirmación visual tras verificar

---

## 📊 Modelo de Datos

### Tablas Involucradas

```sql
-- Usuario (con emailVerificado)
CREATE TABLE usuario (
    id BIGINT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    email VARCHAR(100) UNIQUE,
    email_verificado BOOLEAN DEFAULT FALSE, ← ✅ Control de verificación
    academia_id BIGINT REFERENCES academia(id),
    rol VARCHAR(20),
    activo BOOLEAN DEFAULT TRUE
);

-- Token de Verificación
CREATE TABLE token_verificacion (
    id BIGINT PRIMARY KEY,
    token VARCHAR(255) UNIQUE,
    usuario_id BIGINT REFERENCES usuario(id),
    fecha_creacion TIMESTAMP,
    fecha_expiracion TIMESTAMP,
    usado BOOLEAN DEFAULT FALSE
);

-- Alumno (asociado a academia)
CREATE TABLE alumno (
    id BIGINT PRIMARY KEY,
    usuario_id BIGINT REFERENCES usuario(id),
    academia_id BIGINT REFERENCES academia(id), ← ✅ Academia seleccionada
    estado_matricula VARCHAR(20)
);
```

---

## ✅ Checklist de Funcionamiento

- [x] Formulario de registro con selector de academias
- [x] Lista de academias activas cargada correctamente
- [x] Validación de academia obligatoria
- [x] Creación de usuario con rol ALUMNO
- [x] Asociación a academia seleccionada
- [x] Generación de token de verificación
- [x] Envío de email (o log si no hay SMTP)
- [x] Enlace de verificación funcional
- [x] Usuario puede iniciar sesión tras verificar
- [x] Mensaje claro tras registro exitoso
- [x] Opción de reenviar email de verificación
- [x] Validación de token y expiración

---

## 🎉 Conclusión

El sistema de registro con verificación por email **está completamente implementado y adaptado al modelo SaaS**:

### ✅ Funcionamiento SaaS
- Los alumnos **eligen la academia** a la que quieren inscribirse
- Se listan **todas las academias activas** del sistema
- Cada academia puede captar sus propios alumnos

### ✅ Seguridad
- Email verificado obligatorio
- Tokens con expiración
- Validaciones robustas

### ✅ Experiencia de Usuario
- Proceso claro y guiado
- Mensajes informativos
- Opción de reenviar email

---

**Fecha:** 06/02/2026  
**Versión:** 2.1  
**Estado:** ✅ **FUNCIONANDO CORRECTAMENTE**  
**Adaptado a:** Modelo SaaS Multi-Tenant

**¡El sistema de registro está listo para producción!** 🚀
