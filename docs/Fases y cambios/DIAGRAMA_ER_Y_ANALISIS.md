# Diagrama Entidad-Relación y Análisis del Sistema de Gestión de Academias

## 📊 Diagrama Entidad-Relación (ER)

```
┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                    DIAGRAMA ENTIDAD-RELACIÓN                                         │
│                               Sistema de Gestión de Academias AD                                     │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘

                                        ┌──────────────────────┐
                                        │    TOKEN_VERIFICACION │
                                        ├──────────────────────┤
                                        │ PK id                │
                                        │    token (UNIQUE)    │
                                        │ FK usuario_id        │
                                        │    fecha_creacion    │
                                        │    fecha_expiracion  │
                                        └──────────┬───────────┘
                                                   │ 1
                                                   │
                                                   │ 1
                                        ┌──────────┴───────────┐
                                        │       USUARIO        │
                                        ├──────────────────────┤
                                        │ PK id                │
                                        │    username (UNIQUE) │
                                        │    password          │
                                        │    email (UNIQUE)    │
                                        │    nombre            │
                                        │    apellidos         │
                                        │    rol               │──────────────────────┐
                                        │    activo            │                      │
                                        │    email_verificado  │                      │
                                        │ FK academia_id       │                      │
                                        └──────────┬───────────┘                      │
                                                   │                                  │
                        ┌──────────────────────────┼──────────────────────────┐       │
                        │ 1                        │ N                        │ 1     │
                        │                          │                          │       │
                   ┌────┴─────┐              ┌─────┴────┐              ┌──────┴─────┐ │
                   │  ALUMNO  │              │ ACADEMIA │              │  PROFESOR  │ │
                   ├──────────┤              ├──────────┤              ├────────────┤ │
                   │ PK id    │              │ PK id    │              │ PK id      │ │
                   │ FK usuario_id (UNIQUE)  │    nombre│              │ FK usuario_id (UNIQUE)
                   │ FK academia_id          │    activa│              │ FK academia_id
                   │    fecha_registro       │    fecha_alta           │    especialidad
                   │    estado_matricula     │    nif_cif│             │    biografia│
                   │    observaciones        │    email_contacto       │    fecha_contratacion
                   └────┬─────┘              │    telefono             └──────┬─────┘
                        │                    │    direccion                   │
                        │                    └─────┬────┘                     │
                        │                          │                          │
                        │ N                        │ 1                        │ N
                        │                          │                          │
                   ┌────┴─────────────────────┐    │    ┌─────────────────────┴──────┐
                   │       MATRICULA          │    │    │          CURSO             │
                   ├──────────────────────────┤    │    ├────────────────────────────┤
                   │ PK id                    │    │    │ PK id                      │
                   │ FK academia_id           │◄───┼───►│ FK academia_id             │
                   │ FK alumno_id             │    │    │ FK profesor_id             │◄──┘
                   │ FK curso_id              │◄───┼───►│    nombre                  │
                   │    fecha_matriculacion   │    │    │    descripcion             │
                   │    estado                │    │    │    duracion_horas          │
                   │    observaciones         │    │    │    precio                  │
                   │ FK matriculado_por       │    │    │    fecha_inicio            │
                   └──────────────────────────┘    │    │    fecha_fin               │
                                                   │    │    categoria               │
                                                   │    │    plazas_disponibles      │
                                                   │    │    activo                  │
                                                   │    └────────────────────────────┘
                                                   │
                        ┌──────────────────────────┼──────────────────────────┐
                        │                          │                          │
                        │ N                        │ 1                        │ N
                   ┌────┴─────────────────────┐    │    ┌─────────────────────┴──────┐
                   │      RESERVA_AULA        │    │    │          AULA              │
                   ├──────────────────────────┤    │    ├────────────────────────────┤
                   │ PK id                    │    │    │ PK id                      │
                   │ FK academia_id           │◄───┴───►│ FK academia_id             │
                   │ FK aula_id               │◄────────│                            │
                   │    fecha_inicio          │         │    nombre                  │
                   │    fecha_fin             │         │    capacidad               │
                   │    estado                │         │    activa                  │
                   │    descripcion           │         │    recursos                │
                   │ FK creada_por            │         └────────────────────────────┘
                   │ FK cancelada_por         │
                   │    fecha_creacion        │
                   │    fecha_cancelacion     │
                   └──────────────────────────┘


                                   ┌──────────────────────────┐
                                   │   ENUMERACIONES (ENUM)   │
                                   ├──────────────────────────┤
                                   │                          │
                                   │  ROL:                    │
                                   │  - ADMIN                 │
                                   │  - PROPIETARIO           │
                                   │  - SECRETARIA (*)        │
                                   │  - PROFESOR              │
                                   │  - ALUMNO                │
                                   │                          │
                                   │  ESTADO_MATRICULA:       │
                                   │  - ACTIVA                │
                                   │  - COMPLETADA            │
                                   │  - CANCELADA             │
                                   │                          │
                                   │  ESTADO_RESERVA:         │
                                   │  - ACTIVA                │
                                   │  - CANCELADA             │
                                   └──────────────────────────┘

(*) NOTA IMPORTANTE: SECRETARIA no tiene tabla propia.
    A diferencia de ALUMNO y PROFESOR que tienen entidades
    separadas con campos específicos, SECRETARIA es solo
    un rol asignado directamente a la tabla USUARIO.
    Ver sección "Diseño de Roles" para más detalles.
```

---

## 🎭 Diseño de Roles: ¿Por qué SECRETARIA no tiene tabla propia?

El sistema utiliza un **patrón de herencia mixto** para los roles de usuario:

### Roles CON entidad específica (Especialización):

| Rol | Tabla | Motivo |
|-----|-------|--------|
| **ALUMNO** | `alumno` | Requiere campos específicos: `fecha_registro`, `estado_matricula`, `observaciones`. También se relaciona con `Matricula`. |
| **PROFESOR** | `profesor` | Requiere campos específicos: `especialidad`, `biografia`, `fecha_contratacion`. También se relaciona con `Curso` como impartidor. |

### Roles SIN entidad específica (Solo rol en Usuario):

| Rol | Tabla | Motivo |
|-----|-------|--------|
| **ADMIN** | Solo `usuario` | No necesita datos adicionales. Opera a nivel global sin academia. |
| **PROPIETARIO** | Solo `usuario` | No necesita datos adicionales. Sus permisos se determinan por su `academia_id`. |
| **SECRETARIA** | Solo `usuario` | No necesita datos adicionales. Sus permisos se determinan por su `academia_id`. |

### Diagrama de Especialización:

```
                        ┌─────────────────┐
                        │     USUARIO     │
                        │  (Tabla base)   │
                        │                 │
                        │  - id           │
                        │  - username     │
                        │  - password     │
                        │  - email        │
                        │  - rol ────────────────────────────────┐
                        │  - academia_id  │                      │
                        └────────┬────────┘                      │
                                 │                               │
          ┌──────────────────────┼──────────────────────┐        │
          │                      │                      │        │
          ▼                      ▼                      ▼        ▼
   ┌─────────────┐       ┌─────────────┐        ┌──────────────────────┐
   │   ALUMNO    │       │  PROFESOR   │        │    ADMIN             │
   │  (Tabla)    │       │   (Tabla)   │        │    PROPIETARIO       │
   │             │       │             │        │    SECRETARIA        │
   │ + fecha_reg │       │ + especial. │        │                      │
   │ + estado    │       │ + biografia │        │  (Sin tabla propia,  │
   │ + observ.   │       │ + fecha_con │        │   solo valor en      │
   └─────────────┘       └─────────────┘        │   campo 'rol')       │
                                                └──────────────────────┘
```

### ¿Por qué este diseño?

1. **ALUMNO y PROFESOR** tienen relaciones con otras entidades:
   - `Alumno` → `Matricula` (un alumno tiene múltiples matrículas)
   - `Profesor` → `Curso` (un profesor imparte múltiples cursos)

2. **SECRETARIA, PROPIETARIO y ADMIN** no tienen relaciones específicas:
   - Solo necesitan el campo `rol` para determinar sus permisos
   - Sus acciones se auditan a través de campos como `matriculado_por` en `Matricula`

### Posible mejora futura:

Si en el futuro se necesitara agregar campos específicos para SECRETARIA (ej: `turno`, `departamento`), se podría crear una tabla `secretaria` similar a `alumno` y `profesor`:

```sql
-- Ejemplo de tabla futura (NO IMPLEMENTADA ACTUALMENTE)
CREATE TABLE secretaria (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_id BIGINT NOT NULL UNIQUE,
    academia_id BIGINT NOT NULL,
    turno VARCHAR(50),
    departamento VARCHAR(100),
    fecha_contratacion DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    FOREIGN KEY (academia_id) REFERENCES academia(id)
);
```

---

## 🔗 Relaciones entre Entidades

### **Relaciones Principales:**

| Entidad Origen | Relación | Entidad Destino | Cardinalidad | Descripción |
|----------------|----------|-----------------|--------------|-------------|
| **USUARIO** | tiene | **ACADEMIA** | N:1 | Un usuario pertenece a una academia (nullable solo para ADMIN) |
| **USUARIO** | tiene | **TOKEN_VERIFICACION** | 1:1 | Un usuario puede tener un token de verificación |
| **ALUMNO** | es un | **USUARIO** | 1:1 | Un alumno corresponde a exactamente un usuario |
| **ALUMNO** | pertenece a | **ACADEMIA** | N:1 | Un alumno pertenece a una academia |
| **PROFESOR** | es un | **USUARIO** | 1:1 | Un profesor corresponde a exactamente un usuario |
| **PROFESOR** | pertenece a | **ACADEMIA** | N:1 | Un profesor pertenece a una academia |
| **CURSO** | pertenece a | **ACADEMIA** | N:1 | Un curso pertenece a una academia |
| **CURSO** | impartido por | **PROFESOR** | N:1 | Un curso es impartido por un profesor |
| **MATRICULA** | asocia | **ALUMNO** - **CURSO** | N:M | Relación entre alumnos y cursos |
| **MATRICULA** | pertenece a | **ACADEMIA** | N:1 | Una matrícula pertenece a una academia |
| **MATRICULA** | registrada por | **USUARIO** | N:1 | Una matrícula es registrada por un usuario (secretaria/propietario) |
| **AULA** | pertenece a | **ACADEMIA** | N:1 | Un aula pertenece a una academia |
| **RESERVA_AULA** | reserva | **AULA** | N:1 | Una reserva corresponde a un aula |
| **RESERVA_AULA** | pertenece a | **ACADEMIA** | N:1 | Una reserva pertenece a una academia |
| **RESERVA_AULA** | creada por | **USUARIO** | N:1 | Una reserva es creada por un usuario (profesor) |
| **RESERVA_AULA** | cancelada por | **USUARIO** | N:1 | Una reserva puede ser cancelada por un usuario |

---

## 📋 Descripción Detallada de Entidades

### 1. **ACADEMIA** (Entidad Central)
La academia es la entidad central del sistema que actúa como **tenant** (multitenencia).

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| nombre | VARCHAR(200) | NOT NULL | Nombre de la academia |
| activa | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado de activación |
| fecha_alta | DATETIME | NOT NULL | Fecha de registro en el sistema |
| nif_cif | VARCHAR(20) | - | Identificación fiscal |
| email_contacto | VARCHAR(100) | - | Email de contacto |
| telefono | VARCHAR(20) | - | Teléfono de contacto |
| direccion | VARCHAR(300) | - | Dirección física |

**Reglas de negocio:**
- Una academia puede ser activada/desactivada por el ADMIN
- Solo academias activas aparecen disponibles para registro de alumnos
- Eliminar una academia desactiva todos sus usuarios y entidades relacionadas

---

### 2. **USUARIO** (Autenticación y Autorización)
Representa a cualquier persona que interactúa con el sistema.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| username | VARCHAR(50) | NOT NULL, UNIQUE | Nombre de usuario para login |
| password | VARCHAR(255) | NOT NULL | Contraseña encriptada (BCrypt) |
| email | VARCHAR(100) | NOT NULL, UNIQUE | Correo electrónico |
| nombre | VARCHAR(100) | - | Nombre del usuario |
| apellidos | VARCHAR(100) | - | Apellidos del usuario |
| rol | ENUM | NOT NULL | Rol del usuario en el sistema |
| activo | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado de activación de la cuenta |
| email_verificado | BOOLEAN | NOT NULL, DEFAULT FALSE | Indica si el email ha sido verificado |
| academia_id | BIGINT | FK, NULLABLE | Academia a la que pertenece (NULL solo para ADMIN) |

**Reglas de negocio:**
- Username y email deben ser únicos en todo el sistema
- La contraseña se almacena encriptada con BCrypt
- ADMIN y PROPIETARIO no requieren verificación de email
- ALUMNO, PROFESOR y SECRETARIA requieren verificación de email
- Solo usuarios activos y con email verificado pueden iniciar sesión

---

### 3. **TOKEN_VERIFICACION** (Verificación de Email)
Almacena tokens temporales para verificación de email.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| token | VARCHAR(255) | NOT NULL, UNIQUE | Token UUID generado |
| usuario_id | BIGINT | FK, NOT NULL | Usuario asociado al token |
| fecha_creacion | DATETIME | NOT NULL | Momento de creación |
| fecha_expiracion | DATETIME | NOT NULL | Momento de expiración (24h después) |

**Reglas de negocio:**
- Token válido por 24 horas
- Solo puede existir un token activo por usuario
- Al verificar, el token se elimina
- Se puede regenerar si expira

---

### 4. **ALUMNO** (Especialización de Usuario)
Información específica de usuarios con rol ALUMNO.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| usuario_id | BIGINT | FK, NOT NULL, UNIQUE | Usuario asociado |
| academia_id | BIGINT | FK, NOT NULL | Academia del alumno |
| fecha_registro | DATE | NOT NULL | Fecha de registro como alumno |
| estado_matricula | VARCHAR(50) | DEFAULT 'ACTIVO' | Estado general del alumno |
| observaciones | VARCHAR(1000) | - | Notas sobre el alumno |

**Reglas de negocio:**
- Un usuario solo puede tener un perfil de alumno
- El estado puede ser: ACTIVO, INACTIVO, COMPLETADO, SUSPENDIDO
- Solo alumnos ACTIVOS pueden matricularse en cursos

---

### 5. **PROFESOR** (Especialización de Usuario)
Información específica de usuarios con rol PROFESOR.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| usuario_id | BIGINT | FK, NOT NULL, UNIQUE | Usuario asociado |
| academia_id | BIGINT | FK, NOT NULL | Academia del profesor |
| especialidad | VARCHAR(200) | - | Área de conocimiento |
| biografia | VARCHAR(1000) | - | Descripción del profesor |
| fecha_contratacion | DATE | - | Fecha de contratación |

**Reglas de negocio:**
- Un usuario solo puede tener un perfil de profesor
- Un profesor puede impartir múltiples cursos
- Los profesores pueden crear reservas de aulas

---

### 6. **CURSO** (Formación)
Representa los cursos ofrecidos por las academias.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia que ofrece el curso |
| nombre | VARCHAR(200) | NOT NULL | Nombre del curso |
| descripcion | VARCHAR(1000) | - | Descripción detallada |
| duracion_horas | INTEGER | NOT NULL, MIN 1 | Duración en horas |
| precio | DECIMAL(10,2) | MIN 0 | Costo del curso |
| fecha_inicio | DATE | NOT NULL | Fecha de inicio |
| fecha_fin | DATE | NOT NULL | Fecha de finalización |
| categoria | VARCHAR(100) | - | Categoría temática |
| profesor_id | BIGINT | FK, NOT NULL | Profesor que imparte el curso |
| plazas_disponibles | INTEGER | MIN 0 | Máximo de alumnos |
| activo | BOOLEAN | NOT NULL, DEFAULT TRUE | Estado del curso |

**Reglas de negocio:**
- La fecha de fin debe ser posterior a la fecha de inicio
- El profesor debe pertenecer a la misma academia que el curso
- No se puede eliminar un curso que tiene matrículas
- Las plazas disponibles limitan las matrículas activas

**Índices:**
- `idx_curso_academia` (academia_id)
- `idx_curso_profesor` (profesor_id)
- `idx_curso_fechas` (fecha_inicio, fecha_fin)

---

### 7. **AULA** (Infraestructura)
Representa las aulas disponibles en las academias.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia propietaria |
| nombre | VARCHAR(100) | NOT NULL | Nombre/identificador del aula |
| capacidad | INTEGER | NOT NULL, MIN 1 | Capacidad máxima |
| activa | BOOLEAN | NOT NULL, DEFAULT TRUE | Disponibilidad |
| recursos | VARCHAR(500) | - | Equipamiento disponible |

**Reglas de negocio:**
- Solo aulas activas pueden ser reservadas
- Un aula pertenece a una única academia

**Índices:**
- `idx_aula_academia` (academia_id)
- `idx_aula_academia_activa` (academia_id, activa)

---

### 8. **MATRICULA** (Inscripción)
Relación entre alumnos y cursos.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia de la matrícula |
| alumno_id | BIGINT | FK, NOT NULL | Alumno matriculado |
| curso_id | BIGINT | FK, NOT NULL | Curso de la matrícula |
| fecha_matriculacion | DATETIME | NOT NULL | Momento de la matrícula |
| estado | ENUM | NOT NULL | Estado de la matrícula |
| observaciones | VARCHAR(500) | - | Notas adicionales |
| matriculado_por | BIGINT | FK | Usuario que registró la matrícula |

**Estados posibles:**
- `ACTIVA`: El alumno está cursando actualmente
- `COMPLETADA`: El alumno finalizó el curso
- `CANCELADA`: La matrícula fue anulada

**Reglas de negocio:**
- No se permite matrícula duplicada activa (mismo alumno + curso)
- Se validan las plazas disponibles del curso
- El alumno debe estar activo
- El curso debe estar activo
- Se registra quién realizó la matrícula

**Índices:**
- `idx_matricula_alumno` (alumno_id)
- `idx_matricula_curso` (curso_id)
- `idx_matricula_academia` (academia_id)
- `idx_matricula_estado` (estado)

---

### 9. **RESERVA_AULA** (Gestión de Espacios)
Gestiona las reservas de aulas por profesores.

| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia de la reserva |
| aula_id | BIGINT | FK, NOT NULL | Aula reservada |
| fecha_inicio | DATETIME | NOT NULL | Inicio de la reserva |
| fecha_fin | DATETIME | NOT NULL | Fin de la reserva |
| estado | ENUM | NOT NULL | Estado de la reserva |
| descripcion | VARCHAR(500) | - | Motivo de la reserva |
| creada_por | BIGINT | FK, NOT NULL | Usuario que creó la reserva |
| cancelada_por | BIGINT | FK | Usuario que canceló (si aplica) |
| fecha_creacion | DATETIME | NOT NULL | Momento de creación |
| fecha_cancelacion | DATETIME | - | Momento de cancelación |

**Estados posibles:**
- `ACTIVA`: La reserva está vigente
- `CANCELADA`: La reserva fue anulada

**Reglas de negocio:**
- La fecha de fin debe ser posterior a la de inicio
- No se permiten reservas en el pasado
- **Anti-solapamiento**: No puede haber dos reservas activas que se superpongan para la misma aula
- El aula debe estar activa
- El aula debe pertenecer a la misma academia

**Índices:**
- `idx_reserva_academia` (academia_id)
- `idx_reserva_aula` (aula_id)
- `idx_reserva_fechas` (fecha_inicio, fecha_fin)
- `idx_reserva_estado` (estado)
- `idx_reserva_aula_fechas` (aula_id, fecha_inicio, fecha_fin, estado)

---

## 🔐 Sistema de Roles y Permisos

### Jerarquía de Roles:

```
┌─────────────────────────────────────────────────────────────────┐
│                          ADMIN                                   │
│  ► Acceso total al sistema                                      │
│  ► Gestión de TODAS las academias                               │
│  ► No requiere academia asignada                                │
│  ► No requiere verificación de email                            │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                       PROPIETARIO                                │
│  ► Gestión completa de SU academia                              │
│  ► Crear/gestionar profesores                                   │
│  ► Crear/gestionar secretarias                                  │
│  ► Gestionar cursos, aulas, matrículas                          │
│  ► No requiere verificación de email                            │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                       SECRETARIA                                 │
│  ► Gestión de alumnos y matrículas de SU academia               │
│  ► Ver cursos y profesores                                      │
│  ► Registrar matrículas                                         │
│  ► Requiere verificación de email                               │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                        PROFESOR                                  │
│  ► Ver sus propios cursos                                       │
│  ► Crear reservas de aulas                                      │
│  ► Ver alumnos de sus cursos                                    │
│  ► Requiere verificación de email                               │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                         ALUMNO                                   │
│  ► Ver sus propias matrículas                                   │
│  ► Ver información de sus cursos                                │
│  ► Auto-registro público disponible                             │
│  ► Requiere verificación de email                               │
└─────────────────────────────────────────────────────────────────┘
```

### Matriz de Permisos por Recurso:

| Recurso | ADMIN | PROPIETARIO | SECRETARIA | PROFESOR | ALUMNO |
|---------|-------|-------------|------------|----------|--------|
| **Academias** | CRUD | Ver propia | - | - | - |
| **Usuarios** | CRUD | CRUD (su academia) | Ver alumnos | - | - |
| **Profesores** | CRUD | CRUD (su academia) | Ver | - | - |
| **Secretarias** | CRUD | CRUD (su academia) | - | - | - |
| **Alumnos** | CRUD | Ver | CRUD | Ver (sus cursos) | Ver propio |
| **Cursos** | CRUD | CRUD (su academia) | Ver | Ver propios | Ver matriculados |
| **Matrículas** | CRUD | CRUD (su academia) | CRUD | Ver (sus cursos) | Ver propias |
| **Aulas** | CRUD | CRUD (su academia) | Ver | Ver | - |
| **Reservas** | CRUD | CRUD (su academia) | Ver | CRUD propias | - |

---

## 🔄 Flujos de Trabajo Principales

### 1. **Flujo de Registro de Alumno:**

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Formulario │────►│   Validar   │────►│   Crear     │────►│   Crear     │
│  de Registro│     │   Datos     │     │   Usuario   │     │   Alumno    │
└─────────────┘     └─────────────┘     └─────────────┘     └──────┬──────┘
                                                                   │
                                                                   ▼
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│   Acceder   │◄────│  Verificar  │◄────│  Enviar     │◄────│   Generar   │
│   al Sistema│     │    Token    │     │   Email     │     │    Token    │
└─────────────┘     └─────────────┘     └─────────────┘     └─────────────┘
```

### 2. **Flujo de Matrícula:**

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Seleccionar│────►│  Validar    │────►│  Validar    │────►│   Crear     │
│  Alumno/Curso     │  Alumno     │     │   Curso     │     │  Matrícula  │
└─────────────┘     │  (activo)   │     │  (activo,   │     └─────────────┘
                    └─────────────┘     │   plazas)   │
                                        └─────────────┘
```

### 3. **Flujo de Reserva de Aula:**

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Seleccionar│────►│  Validar    │────►│  Verificar  │────►│   Crear     │
│  Aula/Fechas│     │  Aula       │     │   Anti-     │     │  Reserva    │
└─────────────┘     │  (activa)   │     │  solapamiento│    └─────────────┘
                    └─────────────┘     └─────────────┘
```

---

## 🏗️ Arquitectura de la Aplicación

### Capas del Sistema:

```
┌─────────────────────────────────────────────────────────────────┐
│                     CAPA DE PRESENTACIÓN                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                │
│  │  Templates  │ │ Controllers │ │    DTOs     │                │
│  │  (Thymeleaf)│ │  (Spring    │ │  (Transfer  │                │
│  │             │ │   MVC)      │ │   Objects)  │                │
│  └─────────────┘ └─────────────┘ └─────────────┘                │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CAPA DE SEGURIDAD                            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                │
│  │  Security   │ │ UserDetails │ │ Security    │                │
│  │  Config     │ │ Service     │ │ Utils       │                │
│  │  (Spring    │ │ (Custom)    │ │ (Helper)    │                │
│  │   Security) │ │             │ │             │                │
│  └─────────────┘ └─────────────┘ └─────────────┘                │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CAPA DE NEGOCIO                              │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                │
│  │  Services   │ │ Validations │ │   Email     │                │
│  │  (Academia, │ │ (Business   │ │  Service    │                │
│  │   Curso,    │ │  Rules)     │ │             │                │
│  │   etc.)     │ │             │ │             │                │
│  └─────────────┘ └─────────────┘ └─────────────┘                │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     CAPA DE PERSISTENCIA                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐                │
│  │ Repositories│ │  Entities   │ │  JPA/       │                │
│  │ (Spring     │ │  (Model)    │ │  Hibernate  │                │
│  │  Data JPA)  │ │             │ │             │                │
│  └─────────────┘ └─────────────┘ └─────────────┘                │
└───────────────────────────┬─────────────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────────────┐
│                     BASE DE DATOS                                │
│                     (MySQL/H2/PostgreSQL)                        │
└─────────────────────────────────────────────────────────────────┘
```

### Estructura de Paquetes:

```
es.fempa.acd.demosecurityproductos/
├── config/                    # Configuraciones (Security, CORS, Locale)
│   ├── SecurityConfig.java
│   ├── CorsConfig.java
│   ├── LocaleConfig.java
│   └── Converters/
├── controller/                # Controladores MVC
│   ├── AuthController.java
│   ├── AcademiaController.java
│   ├── CursoController.java
│   ├── MatriculaController.java
│   └── ...
├── dto/                       # Data Transfer Objects
│   └── RegistroAlumnoDTO.java
├── exception/                 # Excepciones personalizadas
│   ├── CursoConMatriculasException.java
│   └── ...
├── model/                     # Entidades JPA
│   ├── Academia.java
│   ├── Usuario.java
│   ├── Alumno.java
│   ├── Profesor.java
│   ├── Curso.java
│   ├── Matricula.java
│   ├── Aula.java
│   ├── ReservaAula.java
│   ├── TokenVerificacion.java
│   └── Enums/
├── repository/                # Repositorios Spring Data JPA
│   ├── AcademiaRepository.java
│   ├── UsuarioRepository.java
│   └── ...
├── service/                   # Servicios de negocio
│   ├── AcademiaService.java
│   ├── UsuarioService.java
│   ├── SecurityUtils.java
│   ├── EmailService.java
│   └── ...
└── util/                      # Utilidades
```

---

## 🔒 Mecanismos de Seguridad

### 1. **Autenticación:**
- Spring Security con DaoAuthenticationProvider
- Contraseñas encriptadas con BCrypt
- Sesiones HTTP con cookies JSESSIONID
- Máximo 1 sesión concurrente por usuario

### 2. **Autorización:**
- Basada en roles (ROLE_ADMIN, ROLE_PROPIETARIO, etc.)
- Control de acceso a URLs por rol
- @PreAuthorize en métodos de servicio
- Aislamiento por tenant (academia)

### 3. **Protección CSRF:**
- Tokens CSRF automáticos con Thymeleaf
- Habilitado por defecto en todos los formularios

### 4. **Validación de Email:**
- Tokens UUID aleatorios
- Expiración en 24 horas
- Verificación requerida para roles sensibles

### 5. **Multitenencia (Tenant Isolation):**
- Cada usuario está asociado a una academia
- SecurityUtils valida acceso a recursos por academia
- Los datos de una academia no son accesibles desde otra

---

## 📊 Consultas Optimizadas

### Índices de la Base de Datos:

```sql
-- Curso
CREATE INDEX idx_curso_academia ON curso (academia_id);
CREATE INDEX idx_curso_profesor ON curso (profesor_id);
CREATE INDEX idx_curso_fechas ON curso (fecha_inicio, fecha_fin);

-- Matricula
CREATE INDEX idx_matricula_alumno ON matricula (alumno_id);
CREATE INDEX idx_matricula_curso ON matricula (curso_id);
CREATE INDEX idx_matricula_academia ON matricula (academia_id);
CREATE INDEX idx_matricula_estado ON matricula (estado);

-- Aula
CREATE INDEX idx_aula_academia ON aula (academia_id);
CREATE INDEX idx_aula_academia_activa ON aula (academia_id, activa);

-- Reserva Aula
CREATE INDEX idx_reserva_academia ON reserva_aula (academia_id);
CREATE INDEX idx_reserva_aula ON reserva_aula (aula_id);
CREATE INDEX idx_reserva_fechas ON reserva_aula (fecha_inicio, fecha_fin);
CREATE INDEX idx_reserva_estado ON reserva_aula (estado);
CREATE INDEX idx_reserva_aula_fechas ON reserva_aula (aula_id, fecha_inicio, fecha_fin, estado);
```

### Consultas JPQL Personalizadas:

- **Verificación de solapamiento de reservas:**
```sql
SELECT COUNT(r) > 0 FROM ReservaAula r 
WHERE r.aula.id = :aulaId 
AND r.estado = 'ACTIVA' 
AND r.fechaInicio < :fechaFin 
AND r.fechaFin > :fechaInicio 
AND (:reservaId IS NULL OR r.id != :reservaId)
```

- **Verificación de matrícula duplicada:**
```sql
SELECT COUNT(m) > 0 FROM Matricula m 
WHERE m.alumno.id = :alumnoId 
AND m.curso.id = :cursoId 
AND m.estado = 'ACTIVA'
```

---

## 📝 Resumen Ejecutivo

El **Sistema de Gestión de Academias AD** es una aplicación web multi-tenant desarrollada con:

- **Backend:** Spring Boot 3.x, Spring Security, Spring Data JPA
- **Frontend:** Thymeleaf, Bootstrap
- **Base de Datos:** JPA/Hibernate (compatible con MySQL, H2, PostgreSQL)
- **Autenticación:** Form-based con verificación de email
- **Internacionalización:** Soporte para español e inglés

**Características principales:**
1. ✅ Gestión de múltiples academias independientes
2. ✅ Sistema de roles jerárquico con 5 niveles
3. ✅ Registro público de alumnos con verificación de email
4. ✅ Gestión de cursos con validación de plazas
5. ✅ Sistema de reservas de aulas con anti-solapamiento
6. ✅ Aislamiento de datos entre academias (multitenencia)
7. ✅ Seguridad robusta con CSRF y encriptación

---

*Documento generado automáticamente basado en el análisis del código fuente.*
*Fecha: Febrero 2026*
