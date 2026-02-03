# Documentación de API - Gestor de Academias

## Descripción General

API web del sistema de Gestión de Academias, construida con Spring Boot 3.4.1 y Spring Security. Proporciona endpoints para la gestión completa de academias, usuarios, cursos, alumnos, profesores y reservas de aulas.

---

## Información Técnica

- **Framework:** Spring Boot 3.4.1
- **Java Version:** 17
- **Base de Datos:** MySQL
- **Autenticación:** Spring Security con sesiones
- **Template Engine:** Thymeleaf
- **Validación:** Jakarta Bean Validation

---

## Configuración de Seguridad

### Autenticación
El sistema utiliza autenticación basada en formularios con Spring Security. Los usuarios deben autenticarse para acceder a los recursos protegidos.

### Roles y Permisos

| Rol | Descripción | Acceso |
|-----|-------------|--------|
| **ADMIN** | Administrador del sistema | Acceso total al sistema, gestión de academias y usuarios de nivel superior |
| **PROPIETARIO** | Propietario de academia | Dashboard y gestión de su academia |
| **SECRETARIA** | Secretaría de academia | Gestión de alumnos, cursos, aulas, matrículas y reservas |
| **PROFESOR** | Profesor | Dashboard y consulta de cursos asignados |
| **ALUMNO** | Alumno | Dashboard y consulta de sus matrículas |

### Rutas Públicas
- `/login` - Página de inicio de sesión
- `/registro` - Registro de nuevos alumnos
- `/verificar-email` - Verificación de email
- `/reenviar-verificacion` - Reenvío de email de verificación
- `/css/**`, `/js/**` - Recursos estáticos

---

## Endpoints de Autenticación

### POST /login
Autenticar un usuario en el sistema.

**Parámetros de formulario:**
- `username` (String, requerido) - Nombre de usuario
- `password` (String, requerido) - Contraseña

**Respuestas:**
- `302` - Redirección al dashboard correspondiente según rol
- `302` - Redirección a `/login?error` si las credenciales son incorrectas

**Ejemplo de uso:**
```html
<form action="/login" method="post">
    <input type="text" name="username" required>
    <input type="password" name="password" required>
    <input type="hidden" name="_csrf" value="${_csrf.token}">
    <button type="submit">Iniciar Sesión</button>
</form>
```

---

### GET /registro
Muestra el formulario de registro para nuevos alumnos.

**Respuestas:**
- `200` - Página de registro con lista de academias disponibles

**Modelo de datos devuelto:**
- `registroDTO` - Objeto RegistroAlumnoDTO vacío
- `academias` - Lista de academias activas

---

### POST /registro
Registra un nuevo alumno en el sistema.

**Parámetros de formulario (RegistroAlumnoDTO):**
- `username` (String, requerido) - Nombre de usuario único
- `email` (String, requerido) - Correo electrónico único
- `password` (String, requerido) - Contraseña (mínimo 3 caracteres)
- `confirmPassword` (String, requerido) - Confirmación de contraseña
- `nombre` (String, requerido) - Nombre del alumno
- `apellidos` (String, requerido) - Apellidos del alumno
- `academiaId` (Long, requerido) - ID de la academia

**Respuestas:**
- `302` - Redirección a `/login` con mensaje de éxito
- `200` - Página de registro con errores si la validación falla

**Validaciones:**
- Las contraseñas deben coincidir
- La contraseña debe tener al menos 3 caracteres
- El username debe ser único
- El email debe ser único
- Debe seleccionar una academia válida

**Ejemplo de respuesta exitosa:**
```
Redirect: /login?registroExitoso
Mensaje: "Registro exitoso. Revisa tu email para verificar tu cuenta."
```

---

### GET /verificar-email
Verifica el email de un usuario mediante un token.

**Parámetros de consulta:**
- `token` (String, requerido) - Token de verificación enviado por email

**Respuestas:**
- `200` - Página de éxito con mensaje de verificación completada
- `200` - Página de error si el token es inválido o ha expirado

**Ejemplo de URL:**
```
/verificar-email?token=abc123def456ghi789
```

---

### GET /reenviar-verificacion
Muestra el formulario para reenviar el email de verificación.

**Respuestas:**
- `200` - Página con formulario para introducir email

---

### POST /reenviar-verificacion
Reenvía el email de verificación a un usuario.

**Parámetros de formulario:**
- `email` (String, requerido) - Correo electrónico del usuario

**Respuestas:**
- `200` - Página con mensaje de éxito
- `200` - Página con error si el email no existe o ya está verificado

---

## Endpoints de Administrador (ADMIN)

Prefijo de rutas: `/admin`

### Gestión de Academias

#### GET /admin/academias
Lista todas las academias del sistema.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con lista de academias

**Modelo devuelto:**
- `academias` - List\<Academia\>

---

#### GET /admin/academias/nueva
Muestra el formulario para crear una nueva academia.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con formulario de nueva academia

**Modelo devuelto:**
- `academia` - Objeto Academia vacío

---

#### POST /admin/academias/guardar
Guarda una nueva academia en el sistema.

**Permisos:** ADMIN

**Parámetros de formulario (Academia):**
- `nombre` (String, requerido, max 200) - Nombre de la academia
- `nifCif` (String, max 20) - NIF/CIF
- `emailContacto` (String, max 100) - Email de contacto
- `telefono` (String, max 20) - Teléfono
- `direccion` (String, max 300) - Dirección

**Respuestas:**
- `302` - Redirección a `/admin/academias` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /admin/academias/editar/{id}
Muestra el formulario para editar una academia existente.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID de la academia

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Academia no encontrada

**Modelo devuelto:**
- `academia` - Objeto Academia

---

#### POST /admin/academias/actualizar/{id}
Actualiza los datos de una academia.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID de la academia

**Parámetros de formulario:** Los mismos que para crear academia

**Respuestas:**
- `302` - Redirección a `/admin/academias` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /admin/academias/eliminar/{id}
Elimina (desactiva) una academia.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID de la academia

**Respuestas:**
- `302` - Redirección a `/admin/academias`
- `404` - Academia no encontrada

---

### Gestión de Profesores

#### GET /admin/profesores
Lista todos los profesores del sistema.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con lista de profesores

**Modelo devuelto:**
- `profesores` - List\<Profesor\>

---

#### GET /admin/profesores/nuevo
Muestra el formulario para crear un nuevo profesor.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con formulario de nuevo profesor

**Modelo devuelto:**
- `profesor` - Objeto Profesor vacío
- `academias` - List\<Academia\>

---

#### POST /admin/profesores/guardar
Crea un nuevo profesor en el sistema.

**Permisos:** ADMIN

**Parámetros de formulario:**
- `usuario.username` (String, requerido) - Nombre de usuario
- `usuario.password` (String, requerido) - Contraseña
- `usuario.email` (String, requerido) - Email
- `usuario.nombre` (String) - Nombre
- `usuario.apellidos` (String) - Apellidos
- `academia.id` (Long, requerido) - ID de la academia
- `especialidad` (String, max 200) - Especialidad del profesor
- `biografia` (String, max 1000) - Biografía

**Respuestas:**
- `302` - Redirección a `/admin/profesores` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /admin/profesores/editar/{id}
Muestra el formulario para editar un profesor.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID del profesor

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Profesor no encontrado

---

#### POST /admin/profesores/actualizar/{id}
Actualiza los datos de un profesor.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID del profesor

**Respuestas:**
- `302` - Redirección a `/admin/profesores` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /admin/profesores/eliminar/{id}
Elimina (desactiva) un profesor.

**Permisos:** ADMIN

**Parámetros de ruta:**
- `id` (Long) - ID del profesor

**Respuestas:**
- `302` - Redirección a `/admin/profesores`

---

### Gestión de Secretarias

#### GET /admin/secretarias
Lista todas las secretarias del sistema.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con lista de secretarias

---

#### GET /admin/secretarias/nueva
Muestra el formulario para crear una nueva secretaria.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página con formulario

**Modelo devuelto:**
- `usuario` - Usuario vacío
- `academias` - List\<Academia\>

---

#### POST /admin/secretarias/guardar
Crea una nueva secretaria en el sistema.

**Permisos:** ADMIN

**Parámetros de formulario:**
- `username` (String, requerido) - Nombre de usuario
- `password` (String, requerido) - Contraseña
- `email` (String, requerido) - Email
- `nombre` (String) - Nombre
- `apellidos` (String) - Apellidos
- `academia.id` (Long, requerido) - ID de la academia

**Respuestas:**
- `302` - Redirección a `/admin/secretarias` si tiene éxito
- `200` - Página de formulario con errores de validación

---

## Endpoints de Secretaría (SECRETARIA)

Prefijo de rutas: `/secretaria`

**Nota:** Todos estos endpoints filtran los datos por la academia del usuario autenticado.

### Gestión de Alumnos

#### GET /secretaria/alumnos
Lista todos los alumnos de la academia del usuario.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con lista de alumnos

**Modelo devuelto:**
- `alumnos` - List\<Alumno\> (filtrados por academia)

---

#### GET /secretaria/alumnos/nuevo
Muestra el formulario para crear un nuevo alumno.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con formulario de nuevo alumno

---

#### POST /secretaria/alumnos/guardar
Crea un nuevo alumno en la academia.

**Permisos:** SECRETARIA

**Parámetros de formulario:**
- `usuario.username` (String, requerido) - Nombre de usuario
- `usuario.password` (String, requerido) - Contraseña
- `usuario.email` (String, requerido) - Email
- `usuario.nombre` (String) - Nombre
- `usuario.apellidos` (String) - Apellidos
- `observaciones` (String, max 1000) - Observaciones

**Respuestas:**
- `302` - Redirección a `/secretaria/alumnos` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/alumnos/editar/{id}
Muestra el formulario para editar un alumno.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del alumno

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Alumno no encontrado
- `403` - Alumno no pertenece a la academia del usuario

---

#### POST /secretaria/alumnos/actualizar/{id}
Actualiza los datos de un alumno.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del alumno

**Respuestas:**
- `302` - Redirección a `/secretaria/alumnos` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/alumnos/eliminar/{id}
Elimina (desactiva) un alumno.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del alumno

**Respuestas:**
- `302` - Redirección a `/secretaria/alumnos`

---

### Gestión de Cursos

#### GET /secretaria/cursos
Lista todos los cursos de la academia.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con lista de cursos

**Modelo devuelto:**
- `cursos` - List\<Curso\> (filtrados por academia)

---

#### GET /secretaria/cursos/nuevo
Muestra el formulario para crear un nuevo curso.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con formulario de nuevo curso

**Modelo devuelto:**
- `curso` - Objeto Curso vacío
- `profesores` - List\<Profesor\> (de la academia)

---

#### POST /secretaria/cursos/guardar
Crea un nuevo curso en la academia.

**Permisos:** SECRETARIA

**Parámetros de formulario:**
- `nombre` (String, requerido, max 200) - Nombre del curso
- `descripcion` (String, max 1000) - Descripción
- `duracionHoras` (Integer, requerido, min 1) - Duración en horas
- `precio` (BigDecimal, min 0) - Precio del curso
- `fechaInicio` (LocalDate, requerido) - Fecha de inicio
- `fechaFin` (LocalDate, requerido) - Fecha de fin
- `categoria` (String, max 100) - Categoría
- `profesor` (String/Long, requerido) - ID del profesor
- `plazasDisponibles` (Integer, min 0) - Número de plazas

**Validaciones:**
- fechaFin debe ser posterior a fechaInicio
- duracionHoras debe ser al menos 1
- precio no puede ser negativo
- El profesor debe pertenecer a la academia

**Respuestas:**
- `302` - Redirección a `/secretaria/cursos` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/cursos/editar/{id}
Muestra el formulario para editar un curso.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del curso

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Curso no encontrado
- `403` - Curso no pertenece a la academia del usuario

---

#### POST /secretaria/cursos/actualizar/{id}
Actualiza los datos de un curso.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del curso

**Respuestas:**
- `302` - Redirección a `/secretaria/cursos` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/cursos/eliminar/{id}
Elimina (desactiva) un curso.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del curso

**Respuestas:**
- `302` - Redirección a `/secretaria/cursos`

---

### Gestión de Aulas

#### GET /secretaria/aulas
Lista todas las aulas de la academia.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con lista de aulas

**Modelo devuelto:**
- `aulas` - List\<Aula\> (filtradas por academia)

---

#### GET /secretaria/aulas/nueva
Muestra el formulario para crear una nueva aula.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con formulario de nueva aula

---

#### POST /secretaria/aulas/guardar
Crea una nueva aula en la academia.

**Permisos:** SECRETARIA

**Parámetros de formulario:**
- `nombre` (String, requerido, max 100) - Nombre del aula
- `capacidad` (Integer, requerido, min 1) - Capacidad de personas
- `recursos` (String, max 500) - Recursos disponibles

**Respuestas:**
- `302` - Redirección a `/secretaria/aulas` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/aulas/editar/{id}
Muestra el formulario para editar un aula.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del aula

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Aula no encontrada

---

#### POST /secretaria/aulas/actualizar/{id}
Actualiza los datos de un aula.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del aula

**Respuestas:**
- `302` - Redirección a `/secretaria/aulas` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/aulas/eliminar/{id}
Elimina (desactiva) un aula.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID del aula

**Respuestas:**
- `302` - Redirección a `/secretaria/aulas`

---

### Gestión de Matrículas

#### GET /secretaria/matriculas/curso/{cursoId}
Lista todas las matrículas de un curso específico.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `cursoId` (Long) - ID del curso

**Respuestas:**
- `200` - Página con lista de matrículas

**Modelo devuelto:**
- `matriculas` - List\<Matricula\>
- `curso` - Objeto Curso

---

#### GET /secretaria/matriculas/nueva
Muestra el formulario para crear una nueva matrícula.

**Permisos:** SECRETARIA

**Parámetros de consulta:**
- `cursoId` (Long, opcional) - ID del curso para preseleccionar

**Respuestas:**
- `200` - Página con formulario de nueva matrícula

**Modelo devuelto:**
- `matricula` - Objeto Matricula vacío
- `alumnos` - List\<Alumno\> (de la academia)
- `cursos` - List\<Curso\> (de la academia)

---

#### POST /secretaria/matriculas/guardar
Matricula un alumno en un curso.

**Permisos:** SECRETARIA

**Parámetros de formulario:**
- `alumno` (String/Long, requerido) - ID del alumno
- `curso` (String/Long, requerido) - ID del curso
- `observaciones` (String, max 500) - Observaciones

**Validaciones:**
- El alumno no puede estar ya matriculado en el mismo curso
- El curso debe estar activo
- El alumno debe pertenecer a la academia

**Respuestas:**
- `302` - Redirección a `/secretaria/matriculas/curso/{cursoId}` si tiene éxito
- `200` - Página de formulario con errores
- `409` - Conflicto: el alumno ya está matriculado en el curso

---

#### GET /secretaria/matriculas/cancelar/{id}
Cancela una matrícula.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID de la matrícula

**Respuestas:**
- `302` - Redirección a la página anterior
- `404` - Matrícula no encontrada

---

### Gestión de Reservas de Aulas

#### GET /secretaria/reservas
Lista todas las reservas de aulas de la academia.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con lista de reservas

**Modelo devuelto:**
- `reservas` - List\<ReservaAula\>

---

#### GET /secretaria/reservas/nueva
Muestra el formulario para crear una nueva reserva de aula.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página con formulario de nueva reserva

**Modelo devuelto:**
- `reserva` - Objeto ReservaAula vacío
- `aulas` - List\<Aula\> (activas de la academia)

---

#### POST /secretaria/reservas/guardar
Crea una nueva reserva de aula.

**Permisos:** SECRETARIA

**Parámetros de formulario:**
- `aula` (String/Long, requerido) - ID del aula
- `fechaInicio` (LocalDateTime, requerido) - Fecha y hora de inicio
- `fechaFin` (LocalDateTime, requerido) - Fecha y hora de fin
- `descripcion` (String, max 500) - Descripción del evento

**Validaciones:**
- fechaFin debe ser posterior a fechaInicio
- No puede solaparse con otras reservas activas del mismo aula
- El aula debe estar activa
- El aula debe pertenecer a la academia

**Respuestas:**
- `302` - Redirección a `/secretaria/reservas` si tiene éxito
- `200` - Página de formulario con errores de validación
- `409` - Conflicto: el aula ya está reservada en ese horario

---

#### GET /secretaria/reservas/editar/{id}
Muestra el formulario para editar una reserva.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID de la reserva

**Respuestas:**
- `200` - Página con formulario de edición
- `404` - Reserva no encontrada
- `403` - Solo se pueden editar reservas activas

---

#### POST /secretaria/reservas/actualizar/{id}
Actualiza los datos de una reserva.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID de la reserva

**Respuestas:**
- `302` - Redirección a `/secretaria/reservas` si tiene éxito
- `200` - Página de formulario con errores de validación

---

#### GET /secretaria/reservas/cancelar/{id}
Cancela una reserva de aula.

**Permisos:** SECRETARIA

**Parámetros de ruta:**
- `id` (Long) - ID de la reserva

**Respuestas:**
- `302` - Redirección a `/secretaria/reservas`
- `404` - Reserva no encontrada
- `403` - Solo se pueden cancelar reservas activas

---

## Endpoints de Dashboard

### GET /admin/dashboard
Dashboard del administrador.

**Permisos:** ADMIN

**Respuestas:**
- `200` - Página del dashboard con estadísticas generales

---

### GET /propietario/dashboard
Dashboard del propietario.

**Permisos:** PROPIETARIO

**Respuestas:**
- `200` - Página del dashboard con información de su academia

---

### GET /secretaria/dashboard
Dashboard de la secretaria.

**Permisos:** SECRETARIA

**Respuestas:**
- `200` - Página del dashboard con estadísticas de su academia

---

### GET /profesor/dashboard
Dashboard del profesor.

**Permisos:** PROFESOR

**Respuestas:**
- `200` - Página del dashboard con sus cursos asignados

---

### GET /alumno/dashboard
Dashboard del alumno.

**Permisos:** ALUMNO

**Respuestas:**
- `200` - Página del dashboard con sus matrículas

---

## Manejo de Errores

El sistema proporciona páginas personalizadas para diferentes tipos de errores:

### Códigos de Estado HTTP

| Código | Descripción | Página |
|--------|-------------|--------|
| 400 | Bad Request - Solicitud mal formada | `/error/400.html` |
| 403 | Forbidden - Acceso denegado | `/error/403.html` |
| 404 | Not Found - Recurso no encontrado | `/error/404.html` |
| 409 | Conflict - Conflicto (ej: matrícula duplicada) | `/error/409.html` |
| 500 | Internal Server Error - Error del servidor | `/error/500.html` |

### Respuestas de Error

```json
{
  "timestamp": "2026-02-03T15:30:00.000+00:00",
  "status": 404,
  "error": "Not Found",
  "message": "El recurso solicitado no existe",
  "path": "/api/cursos/999"
}
```

---

## Modelos de Datos

### Academia
```json
{
  "id": 1,
  "nombre": "Academia de Programación",
  "activa": true,
  "fechaAlta": "2026-01-01T10:00:00",
  "nifCif": "B12345678",
  "emailContacto": "contacto@academia.com",
  "telefono": "912345678",
  "direccion": "Calle Principal 123, Madrid"
}
```

### Usuario
```json
{
  "id": 1,
  "username": "usuario123",
  "email": "usuario@email.com",
  "nombre": "Juan",
  "apellidos": "García López",
  "rol": "ALUMNO",
  "activo": true,
  "emailVerificado": true,
  "academia": {
    "id": 1,
    "nombre": "Academia de Programación"
  }
}
```

### Curso
```json
{
  "id": 1,
  "academia": {
    "id": 1,
    "nombre": "Academia de Programación"
  },
  "nombre": "Desarrollo Web con Spring Boot",
  "descripcion": "Curso completo de desarrollo backend",
  "duracionHoras": 60,
  "precio": 499.99,
  "fechaInicio": "2026-03-01",
  "fechaFin": "2026-05-31",
  "categoria": "Programación",
  "profesor": {
    "id": 1,
    "usuario": {
      "nombre": "María",
      "apellidos": "Sánchez"
    }
  },
  "plazasDisponibles": 20,
  "activo": true
}
```

### Matrícula
```json
{
  "id": 1,
  "academia": {
    "id": 1,
    "nombre": "Academia de Programación"
  },
  "alumno": {
    "id": 1,
    "usuario": {
      "nombre": "Juan",
      "apellidos": "García"
    }
  },
  "curso": {
    "id": 1,
    "nombre": "Desarrollo Web con Spring Boot"
  },
  "fechaMatriculacion": "2026-02-01T10:30:00",
  "estado": "ACTIVA",
  "observaciones": "Pago al contado",
  "matriculadoPor": {
    "id": 3,
    "username": "secretaria1"
  }
}
```

### ReservaAula
```json
{
  "id": 1,
  "academia": {
    "id": 1,
    "nombre": "Academia de Programación"
  },
  "aula": {
    "id": 1,
    "nombre": "Aula A1",
    "capacidad": 30
  },
  "fechaInicio": "2026-02-10T09:00:00",
  "fechaFin": "2026-02-10T11:00:00",
  "estado": "ACTIVA",
  "descripcion": "Clase de Spring Boot",
  "creadaPor": {
    "id": 3,
    "username": "secretaria1"
  },
  "fechaCreacion": "2026-02-01T14:00:00"
}
```

---

## Flujos de Trabajo Comunes

### Flujo 1: Registro de Nuevo Alumno

1. **GET /registro** - El alumno accede al formulario de registro
2. Selecciona una academia de la lista
3. Completa sus datos personales
4. **POST /registro** - Envía el formulario
5. El sistema crea el usuario y el perfil de alumno
6. Se envía un email de verificación
7. Redirección a **/login** con mensaje de éxito
8. **GET /verificar-email?token=xxx** - El alumno hace clic en el enlace del email
9. La cuenta queda verificada y puede iniciar sesión

### Flujo 2: Matrícula de Alumno en Curso

1. La secretaria inicia sesión y accede al dashboard
2. **GET /secretaria/cursos** - Ve la lista de cursos disponibles
3. Selecciona un curso y hace clic en "Ver matrículas"
4. **GET /secretaria/matriculas/curso/{cursoId}** - Ve las matrículas actuales
5. Hace clic en "Nueva matrícula"
6. **GET /secretaria/matriculas/nueva?cursoId={cursoId}** - Ve el formulario
7. Selecciona el alumno de la lista desplegable
8. Añade observaciones si es necesario
9. **POST /secretaria/matriculas/guardar** - Confirma la matrícula
10. El sistema valida que no existe matrícula duplicada
11. Crea la matrícula con estado ACTIVA
12. Redirección a la lista de matrículas del curso

### Flujo 3: Reserva de Aula

1. La secretaria accede a **GET /secretaria/reservas**
2. Hace clic en "Nueva reserva"
3. **GET /secretaria/reservas/nueva** - Ve el formulario
4. Selecciona un aula de la lista
5. Indica fecha/hora de inicio y fin
6. Añade una descripción
7. **POST /secretaria/reservas/guardar** - Envía el formulario
8. El sistema valida que no hay solapamiento con otras reservas
9. Crea la reserva con estado ACTIVA
10. Registra al usuario que creó la reserva
11. Redirección a la lista de reservas

---

## Seguridad

### Protección CSRF
Todos los formularios POST requieren un token CSRF válido:
```html
<input type="hidden" name="_csrf" value="${_csrf.token}">
```

### Cifrado de Contraseñas
Las contraseñas se cifran usando BCrypt antes de almacenarse en la base de datos.

### Verificación de Email
Los usuarios deben verificar su email antes de poder acceder completamente al sistema. Los tokens de verificación expiran después de 24 horas.

### Control de Acceso
- Cada endpoint valida que el usuario autenticado tenga el rol adecuado
- Los datos se filtran por academia automáticamente para PROPIETARIO, SECRETARIA, PROFESOR y ALUMNO
- Solo ADMIN puede acceder a datos de todas las academias

### Sesiones
Las sesiones se gestionan mediante Spring Security con las siguientes configuraciones:
- Timeout de sesión: Configurable en application.properties
- Maximum sessions: 1 por usuario
- Session fixation protection: Habilitado

---

## Configuración de Aplicación

Archivo: `src/main/resources/application.properties`

```properties
# Configuración de Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/gestor_academias
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Configuración de Email
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=tu-email@gmail.com
spring.mail.password=tu-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# URL base de la aplicación
app.url=http://localhost:8080

# Configuración de internacionalización
spring.messages.basename=i18n/messages
spring.messages.encoding=UTF-8

# Configuración de Thymeleaf
spring.thymeleaf.cache=false
spring.thymeleaf.enabled=true
spring.thymeleaf.prefix=classpath:/templates/
spring.thymeleaf.suffix=.html
```

---

## Internacionalización (i18n)

El sistema soporta múltiples idiomas. Los mensajes están en:
- `src/main/resources/i18n/messages.properties` (español por defecto)
- `src/main/resources/i18n/messages_es.properties` (español)
- `src/main/resources/i18n/messages_en.properties` (inglés)

Cambio de idioma mediante parámetro:
```
?lang=es
?lang=en
```

---

## Ejemplos de Uso

### Ejemplo 1: Crear una Academia (cURL)
```bash
curl -X POST http://localhost:8080/admin/academias/guardar \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "nombre=Nueva Academia" \
  -d "nifCif=B98765432" \
  -d "emailContacto=info@nuevaacademia.com" \
  -d "telefono=912345678" \
  -d "direccion=Calle Nueva 456" \
  -d "_csrf=token-csrf-aqui" \
  --cookie "JSESSIONID=session-id-aqui"
```

### Ejemplo 2: Listar Cursos (JavaScript/Fetch)
```javascript
fetch('/secretaria/cursos', {
  method: 'GET',
  credentials: 'include'
})
.then(response => response.text())
.then(html => {
  // Procesar HTML devuelto
  console.log('Cursos obtenidos');
})
.catch(error => console.error('Error:', error));
```

---

## Notas Adicionales

### Buenas Prácticas
1. Siempre validar los datos en el servidor, no solo en el cliente
2. Utilizar transacciones para operaciones que modifican múltiples entidades
3. Implementar logging adecuado para auditoría
4. Realizar copias de seguridad periódicas de la base de datos

### Limitaciones Conocidas
- Las reservas de aulas no pueden modificarse una vez creadas, solo cancelarse
- No hay sistema de notificaciones en tiempo real
- La búsqueda y filtrado avanzados no están implementados

### Próximas Funcionalidades
- API REST para integración con aplicaciones móviles
- Sistema de notificaciones por email automáticas
- Generación de informes y estadísticas
- Sistema de pagos integrado
- Calendario visual para reservas de aulas

---

**Documento elaborado para el Hito 3 del proyecto Gestor de Academias**
*Fecha: Febrero 2026*
*Versión: 1.0*
