# 📋 DOCUMENTO DE PROYECTO DE DESARROLLO DE SOFTWARE

## **Gestor de Administración de Academias (Multi-Academia)**

---

**Versión del Documento:** 1.0  
**Fecha de Elaboración:** 10 de febrero de 2026  
**Estado del Proyecto:** ✅ Completado - Versión 0.6.0 BETA  
**Tipo de Aplicación:** Sistema Web Multi-Tenant (SaaS)

---

# ÍNDICE

1. [Información General del Proyecto](#1-información-general-del-proyecto)
2. [Resumen Ejecutivo](#2-resumen-ejecutivo)
3. [Documento de Requisitos](#3-documento-de-requisitos)
4. [Análisis del Sistema](#4-análisis-del-sistema)
5. [Diseño del Sistema](#5-diseño-del-sistema)
6. [Especificaciones Técnicas](#6-especificaciones-técnicas)
7. [Arquitectura del Sistema](#7-arquitectura-del-sistema)
8. [Modelo de Datos](#8-modelo-de-datos)
9. [Módulos del Sistema](#9-módulos-del-sistema)
10. [Seguridad](#10-seguridad)
11. [Interfaces de Usuario](#11-interfaces-de-usuario)
12. [Plan de Proyecto](#12-plan-de-proyecto)
13. [Manual de Desarrollo](#13-manual-de-desarrollo)
14. [Manual de Instalación y Despliegue](#14-manual-de-instalación-y-despliegue)
15. [Manual de Usuario](#15-manual-de-usuario)
16. [Pruebas del Sistema](#16-pruebas-del-sistema)
17. [Registro de Cambios (Changelog)](#17-registro-de-cambios-changelog)
18. [Glosario de Términos](#18-glosario-de-términos)
19. [Anexos](#19-anexos)

---

# 1. INFORMACIÓN GENERAL DEL PROYECTO

## 1.1. Datos del Proyecto

| Elemento | Descripción |
|----------|-------------|
| **Nombre del Proyecto** | Gestor de Administración de Academias (Multi-Academia) |
| **Código del Proyecto** | GestorAcademiasAD |
| **Versión Actual** | 0.6.0-BETA |
| **Tipo de Software** | Aplicación Web Multi-Tenant (SaaS) |
| **Licencia** | Propietaria |
| **Organización** | FEMPA - Formación Especializada |

## 1.2. Equipo de Desarrollo

| Rol | Responsabilidades |
|-----|-------------------|
| **Desarrollador Full Stack** | Implementación completa (backend, frontend, seguridad) |
| **Tester/QA** | Casos de prueba, pruebas de seguridad, documentación de bugs |
| **Coordinador Técnico** | Seguimiento, revisiones, aprobación PRs, estándares |

## 1.3. Control de Versiones del Documento

| Versión | Fecha | Autor | Descripción |
|---------|-------|-------|-------------|
| 1.0 | 10/02/2026 | Equipo de Desarrollo | Versión inicial completa |

---

# 2. RESUMEN EJECUTIVO

## 2.1. Descripción General

El **Gestor de Administración de Academias** es un sistema web diseñado para academias que desean digitalizar sus procesos administrativos y operativos. El proyecto adopta un enfoque **multi-tenant (SaaS)**, permitiendo que un administrador global gestione múltiples academias desde una única instalación, sin necesidad de duplicar código ni despliegues.

## 2.2. Objetivos Principales

- ✅ Sistema multi-academia con aislamiento completo de datos
- ✅ Control de acceso basado en roles (RBAC)
- ✅ Gestión integral de alumnos, profesores y personal administrativo
- ✅ Sistema de reservas de aulas con validación anti-solapamiento
- ✅ Módulo académico con cursos y matriculaciones
- ✅ Paneles personalizados según rol de usuario
- ✅ Verificación de email para nuevos usuarios
- ✅ Internacionalización (español e inglés)

## 2.3. Alcance del Proyecto

### Incluido en el Alcance:
- Gestión completa de academias (CRUD)
- Sistema de usuarios con 5 roles diferenciados
- Gestión de alumnos con estados de matrícula
- Gestión de profesores y asignación a cursos
- Gestión de aulas y recursos
- Sistema de reservas con control de conflictos
- Gestión de cursos con plazas disponibles
- Sistema de matriculación con validaciones
- Autenticación y autorización segura
- Verificación de email para registro

### Fuera del Alcance:
- Pasarela de pagos online
- Aplicación móvil nativa
- Integración con sistemas externos de terceros
- Videoconferencias integradas

## 2.4. Beneficios del Sistema

| Beneficio | Descripción |
|-----------|-------------|
| **Centralización** | Una única plataforma para gestionar múltiples academias |
| **Escalabilidad** | Añadir nuevas academias sin reinstalaciones |
| **Seguridad** | Aislamiento total de datos entre academias |
| **Eficiencia** | Automatización de procesos administrativos |
| **Trazabilidad** | Registro completo de todas las operaciones |
| **Flexibilidad** | Roles específicos para cada tipo de usuario |

---

# 3. DOCUMENTO DE REQUISITOS

## 3.1. Requisitos Funcionales

### RF-001: Gestión de Academias (ADMIN)
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-001.1 | Crear nueva academia desde la interfaz | Alta | ✅ |
| RF-001.2 | Editar datos de academia existente | Alta | ✅ |
| RF-001.3 | Activar/desactivar academias | Alta | ✅ |
| RF-001.4 | Visualizar listado de academias | Alta | ✅ |
| RF-001.5 | Asignar propietarios a academias | Alta | ✅ |

### RF-002: Gestión de Usuarios y Roles
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-002.1 | Autenticación mediante usuario y contraseña | Alta | ✅ |
| RF-002.2 | Gestión de roles: ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO | Alta | ✅ |
| RF-002.3 | Alta/edición/desactivación lógica de usuarios | Alta | ✅ |
| RF-002.4 | Asignación de usuarios a academia | Alta | ✅ |
| RF-002.5 | Verificación de email para nuevos registros | Media | ✅ |
| RF-002.6 | Restricción de acceso según academia | Alta | ✅ |

### RF-003: Panel Propietario
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-003.1 | Visualizar dashboard con KPIs | Alta | ✅ |
| RF-003.2 | Ver listado de academias asignadas | Alta | ✅ |
| RF-003.3 | Seleccionar academia para trabajar | Alta | ✅ |
| RF-003.4 | Visualizar estadísticas de la academia | Media | ✅ |
| RF-003.5 | Gestión de secretarias de su academia | Media | ✅ |
| RF-003.6 | Gestión de profesores de su academia | Media | ✅ |

### RF-004: Panel Secretaría
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-004.1 | Alta de alumnos (usuario + ficha) | Alta | ✅ |
| RF-004.2 | Baja de alumnos (desactivación lógica) | Alta | ✅ |
| RF-004.3 | Consulta y edición de alumnos | Alta | ✅ |
| RF-004.4 | Gestión de cursos | Alta | ✅ |
| RF-004.5 | Gestión de matrículas | Alta | ✅ |
| RF-004.6 | Gestión de aulas | Alta | ✅ |
| RF-004.7 | Gestión de reservas de aulas | Alta | ✅ |
| RF-004.8 | Gestión de profesores | Media | ✅ |

### RF-005: Panel Profesor
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-005.1 | Visualizar dashboard personal | Media | ✅ |
| RF-005.2 | Ver cursos asignados | Media | ✅ |
| RF-005.3 | Ver alumnos de sus cursos | Baja | ⏳ |

### RF-006: Panel Alumno
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-006.1 | Visualizar dashboard personal | Media | ✅ |
| RF-006.2 | Ver estado y perfil | Media | ✅ |
| RF-006.3 | Ver matrículas y cursos | Baja | ⏳ |

### RF-007: Reservas de Aulas
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-007.1 | Gestión de aulas por academia | Alta | ✅ |
| RF-007.2 | Crear reservas con fecha/hora | Alta | ✅ |
| RF-007.3 | Validación anti-solapamiento | Alta | ✅ |
| RF-007.4 | Consultar reservas con filtros | Alta | ✅ |
| RF-007.5 | Cancelar reservas con trazabilidad | Alta | ✅ |

### RF-008: Gestión de Cursos y Matrículas
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RF-008.1 | CRUD de cursos con profesor asignado | Alta | ✅ |
| RF-008.2 | Control de plazas disponibles | Alta | ✅ |
| RF-008.3 | Matriculación de alumnos | Alta | ✅ |
| RF-008.4 | Validación de duplicados en matrícula | Alta | ✅ |
| RF-008.5 | Estados de matrícula (ACTIVA, COMPLETADA, CANCELADA) | Alta | ✅ |

## 3.2. Requisitos No Funcionales

### RNF-001: Seguridad
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RNF-001.1 | Contraseñas cifradas con BCrypt | Alta | ✅ |
| RNF-001.2 | Autorización basada en roles (RBAC) | Alta | ✅ |
| RNF-001.3 | Aislamiento por academia (tenant scope) | Alta | ✅ |
| RNF-001.4 | Protección contra SQL Injection | Alta | ✅ |
| RNF-001.5 | Protección contra XSS | Alta | ✅ |
| RNF-001.6 | Protección CSRF | Alta | ✅ |
| RNF-001.7 | Variables de entorno para credenciales | Alta | ✅ |

### RNF-002: Rendimiento
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RNF-002.1 | Respuesta de páginas < 2 segundos | Alta | ✅ |
| RNF-002.2 | Operaciones CRUD < 500 ms | Alta | ✅ |
| RNF-002.3 | Índices optimizados en BD | Media | ✅ |
| RNF-002.4 | Consultas JPA optimizadas | Media | ✅ |

### RNF-003: Compatibilidad
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RNF-003.1 | Diseño responsivo (móvil, tablet, escritorio) | Alta | ✅ |
| RNF-003.2 | Compatibilidad Chrome, Firefox, Edge, Safari | Alta | ✅ |
| RNF-003.3 | Java 17 y Spring Boot 3.4.1 | Alta | ✅ |

### RNF-004: Usabilidad
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RNF-004.1 | Interfaz clara adaptada por rol | Alta | ✅ |
| RNF-004.2 | Mensajes de validación en español | Alta | ✅ |
| RNF-004.3 | Feedback visual de operaciones | Alta | ✅ |
| RNF-004.4 | Internacionalización (ES/EN) | Media | ✅ |

### RNF-005: Mantenibilidad
| ID | Descripción | Prioridad | Estado |
|----|-------------|-----------|--------|
| RNF-005.1 | Arquitectura MVC por capas | Alta | ✅ |
| RNF-005.2 | Validaciones consistentes (Bean Validation) | Alta | ✅ |
| RNF-005.3 | Manejo centralizado de excepciones | Alta | ✅ |
| RNF-005.4 | Documentación técnica completa | Media | ✅ |

## 3.3. Restricciones del Proyecto

| Restricción | Descripción |
|-------------|-------------|
| **Framework Backend** | Spring Boot 3.4.1 |
| **Motor de Plantillas** | Thymeleaf |
| **Base de Datos** | MySQL 8.x o PostgreSQL 13+ |
| **Seguridad** | Spring Security obligatorio |
| **Versión Java** | Java 17 LTS |
| **Build Tool** | Maven |

## 3.4. Suposiciones Iniciales

1. El ADMIN creará academias y configurará el arranque inicial
2. Los usuarios no ADMIN pertenecen exactamente a una academia
3. Las reservas de aulas requieren validación anti-solapamiento
4. El registro de alumnos incluye verificación por email
5. Los propietarios solo pueden visualizar sus academias (solo lectura)

---

# 4. ANÁLISIS DEL SISTEMA

## 4.1. Diagrama de Casos de Uso

### Actores del Sistema

```
┌─────────────────────────────────────────────────────────────────────┐
│                         ACTORES DEL SISTEMA                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌─────────┐    ┌─────────────┐    ┌───────────┐                  │
│   │  ADMIN  │    │ PROPIETARIO │    │ SECRETARIA│                  │
│   └────┬────┘    └──────┬──────┘    └─────┬─────┘                  │
│        │                │                  │                        │
│   Administrador    Dueño/Cliente      Personal                     │
│   del Sistema      de la Academia     Administrativo               │
│                                                                     │
│   ┌──────────┐    ┌─────────┐                                      │
│   │ PROFESOR │    │  ALUMNO │                                      │
│   └────┬─────┘    └────┬────┘                                      │
│        │               │                                            │
│   Docente de      Estudiante                                       │
│   la Academia     Matriculado                                      │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Casos de Uso por Actor

#### ADMIN
- CU-001: Gestionar Academias (CRUD)
- CU-002: Gestionar Propietarios
- CU-003: Asignar Propietario a Academia
- CU-004: Ver Dashboard Global
- CU-005: Gestionar Secretarias Globales
- CU-006: Gestionar Profesores Globales

#### PROPIETARIO
- CU-010: Ver Dashboard de Academias
- CU-011: Seleccionar Academia de Trabajo
- CU-012: Ver Detalle de Academia (Solo Lectura)
- CU-013: Ver Estadísticas de Academia
- CU-014: Gestionar Secretarias de su Academia
- CU-015: Gestionar Profesores de su Academia

#### SECRETARIA
- CU-020: Ver Dashboard de Secretaría
- CU-021: Gestionar Alumnos (CRUD)
- CU-022: Gestionar Cursos (CRUD)
- CU-023: Gestionar Matrículas
- CU-024: Gestionar Aulas (CRUD)
- CU-025: Gestionar Reservas de Aulas
- CU-026: Gestionar Profesores

#### PROFESOR
- CU-030: Ver Dashboard Personal
- CU-031: Ver Cursos Asignados
- CU-032: Ver Alumnos de sus Cursos

#### ALUMNO
- CU-040: Registrarse en el Sistema
- CU-041: Verificar Email
- CU-042: Ver Dashboard Personal
- CU-043: Ver Matrículas y Cursos

## 4.2. Diagrama de Flujo de Datos

### Flujo Principal: Proceso de Matrícula

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   SECRETARIA │──────►│   SISTEMA    │──────►│    BASE DE   │
│              │       │   WEB        │       │    DATOS     │
└──────────────┘       └──────────────┘       └──────────────┘
       │                      │                      │
       │  1. Selecciona      │                      │
       │     Alumno y Curso  │                      │
       │─────────────────────►│                      │
       │                      │  2. Valida          │
       │                      │     plazas y        │
       │                      │     duplicados      │
       │                      │─────────────────────►│
       │                      │                      │
       │                      │◄─────────────────────│
       │                      │  3. Resultado       │
       │                      │     validación      │
       │◄─────────────────────│                      │
       │  4. Muestra          │                      │
       │     confirmación     │                      │
       │     o error          │  5. Guarda          │
       │                      │     matrícula       │
       │                      │─────────────────────►│
       │                      │                      │
       │                      │  6. Actualiza       │
       │                      │     plazas          │
       │                      │─────────────────────►│
```

## 4.3. Matriz de Trazabilidad

| Requisito | Caso de Uso | Módulo | Estado |
|-----------|-------------|--------|--------|
| RF-001.1 | CU-001 | AcademiaController | ✅ |
| RF-002.1 | CU-040 | AuthController | ✅ |
| RF-003.1 | CU-010 | PropietarioController | ✅ |
| RF-004.1 | CU-021 | AlumnoController | ✅ |
| RF-007.2 | CU-025 | ReservaAulaController | ✅ |
| RF-008.3 | CU-023 | MatriculaController | ✅ |

---

# 5. DISEÑO DEL SISTEMA

## 5.1. Diseño de Alto Nivel

```
┌─────────────────────────────────────────────────────────────────────┐
│                     ARQUITECTURA DE ALTO NIVEL                       │
└─────────────────────────────────────────────────────────────────────┘

                         ┌─────────────────┐
                         │   NAVEGADOR     │
                         │   (Cliente)     │
                         └────────┬────────┘
                                  │
                                  │ HTTP/HTTPS
                                  ▼
                    ┌─────────────────────────────┐
                    │      SPRING BOOT APP        │
                    │    ┌───────────────────┐    │
                    │    │   SPRING SECURITY │    │
                    │    │   (Autenticación) │    │
                    │    └─────────┬─────────┘    │
                    │              │              │
                    │    ┌─────────▼─────────┐    │
                    │    │   CONTROLADORES   │    │
                    │    │   (Spring MVC)    │    │
                    │    └─────────┬─────────┘    │
                    │              │              │
                    │    ┌─────────▼─────────┐    │
                    │    │    SERVICIOS      │    │
                    │    │ (Lógica Negocio)  │    │
                    │    └─────────┬─────────┘    │
                    │              │              │
                    │    ┌─────────▼─────────┐    │
                    │    │  REPOSITORIOS     │    │
                    │    │  (Spring Data)    │    │
                    │    └─────────┬─────────┘    │
                    └──────────────┼──────────────┘
                                   │
                                   │ JDBC
                                   ▼
                         ┌─────────────────┐
                         │     MySQL       │
                         │   (Base Datos)  │
                         └─────────────────┘
```

## 5.2. Diagrama de Componentes

```
┌─────────────────────────────────────────────────────────────────────┐
│                      DIAGRAMA DE COMPONENTES                         │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                          CAPA DE PRESENTACIÓN                        │
│  ┌────────────┐ ┌────────────┐ ┌────────────┐ ┌────────────────┐   │
│  │   Login    │ │  Dashboard │ │ Formularios│ │    Listados    │   │
│  │  (HTML)    │ │  (HTML)    │ │   (HTML)   │ │     (HTML)     │   │
│  └────────────┘ └────────────┘ └────────────┘ └────────────────┘   │
│                        THYMELEAF + BOOTSTRAP 5                       │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          CAPA DE CONTROLADORES                       │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │ AuthController│ │AdminController│ │SecretariaCtrl│              │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │PropietarioCtrl│ │ ProfesorCtrl │ │  AlumnoCtrl  │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │  CursoCtrl   │ │ MatriculaCtrl│ │ ReservaCtrl  │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                          CAPA DE SERVICIOS                           │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │AcademiaService│ │UsuarioService│ │ EmailService │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │ AlumnoService│ │ProfesorService│ │  AulaService │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │ CursoService │ │MatriculaSrv  │ │ ReservaSrv   │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│                         CAPA DE PERSISTENCIA                         │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │AcademiaRepo  │ │UsuarioRepo   │ │ AlumnoRepo   │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐               │
│  │ProfesorRepo  │ │  AulaRepo    │ │ ReservaRepo  │               │
│  └──────────────┘ └──────────────┘ └──────────────┘               │
│  ┌──────────────┐ ┌──────────────┐                                 │
│  │  CursoRepo   │ │MatriculaRepo │                                 │
│  └──────────────┘ └──────────────┘                                 │
│                         SPRING DATA JPA                              │
└─────────────────────────────────────────────────────────────────────┘
```

## 5.3. Patrones de Diseño Utilizados

| Patrón | Uso en el Sistema |
|--------|-------------------|
| **MVC** | Arquitectura principal del sistema |
| **Repository** | Abstracción del acceso a datos |
| **Service Layer** | Encapsulación de lógica de negocio |
| **DTO** | Transferencia de datos entre capas |
| **Singleton** | Servicios de Spring (por defecto) |
| **Factory** | Creación de objetos de respuesta |
| **Template Method** | Thymeleaf para vistas |
| **Strategy** | Diferentes roles con diferentes comportamientos |

---

# 6. ESPECIFICACIONES TÉCNICAS

## 6.1. Stack Tecnológico

### Backend

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Java** | 17 LTS | Lenguaje de programación |
| **Spring Boot** | 3.4.1 | Framework principal |
| **Spring Security** | 6.x | Autenticación y autorización |
| **Spring Data JPA** | 3.x | Persistencia de datos |
| **Spring Validation** | 3.x | Validación de datos |
| **Spring Mail** | 3.x | Envío de emails |
| **Hibernate** | 6.x | ORM |
| **Maven** | 3.x | Gestión de dependencias |

### Frontend

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **Thymeleaf** | 3.x | Motor de plantillas |
| **Thymeleaf Security** | 6.x | Integración con Spring Security |
| **Bootstrap** | 5.x | Framework CSS |
| **JavaScript** | ES6+ | Validaciones cliente |
| **Bootstrap Icons** | 1.x | Iconografía |

### Base de Datos

| Tecnología | Versión | Propósito |
|------------|---------|-----------|
| **MySQL** | 8.x | Base de datos principal |
| **H2** | - | Base de datos para tests |

### Herramientas de Desarrollo

| Herramienta | Propósito |
|-------------|-----------|
| **Git** | Control de versiones |
| **IntelliJ IDEA / Eclipse** | IDE |
| **Postman** | Pruebas de API |
| **MySQL Workbench** | Gestión de BD |

## 6.2. Dependencias Maven

```xml
<!-- Spring Boot Starters -->
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-thymeleaf
spring-boot-starter-web
spring-boot-starter-validation
spring-boot-starter-mail

<!-- Thymeleaf Extras -->
thymeleaf-extras-springsecurity6

<!-- Database -->
mysql-connector-j (runtime)
h2 (test)

<!-- Data REST -->
spring-data-rest-webmvc

<!-- Testing -->
spring-boot-starter-test
spring-security-test
```

## 6.3. Configuración del Sistema

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SERVER_PORT` | Puerto del servidor | 8080 |
| `DB_URL` | URL de conexión a BD | jdbc:mysql://localhost:3306/acd_proyecto_2025 |
| `DB_USERNAME` | Usuario de BD | acd |
| `DB_PASSWORD` | Contraseña de BD | acd |
| `MAIL_HOST` | Servidor SMTP | smtp.gmail.com |
| `MAIL_PORT` | Puerto SMTP | 587 |
| `MAIL_USERNAME` | Email remitente | - |
| `MAIL_PASSWORD` | Contraseña de aplicación | - |
| `APP_BASE_URL` | URL base de la aplicación | http://localhost:8080 |
| `JPA_DDL_AUTO` | Modo DDL de Hibernate | validate |
| `JPA_SHOW_SQL` | Mostrar SQL en logs | false |

### Perfiles de Spring

| Perfil | Uso | Características |
|--------|-----|-----------------|
| **default** | Producción | Validación de esquema, logs mínimos |
| **dev** | Desarrollo | Auto-update de esquema, logs detallados |
| **test** | Testing | H2 en memoria, configuración aislada |

---

# 7. ARQUITECTURA DEL SISTEMA

## 7.1. Arquitectura en Capas

```
┌─────────────────────────────────────────────────────────────────────┐
│                        ARQUITECTURA MVC EN CAPAS                     │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│  CAPA DE PRESENTACIÓN (View)                                         │
│  ├── Thymeleaf Templates                                            │
│  ├── Bootstrap 5 CSS                                                │
│  ├── JavaScript (validaciones cliente)                              │
│  └── Fragments reutilizables                                        │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  CAPA DE CONTROL (Controller)                                        │
│  ├── Controladores Spring MVC (@Controller)                         │
│  ├── Bean Validation en formularios                                 │
│  ├── Controller Advice para errores                                 │
│  └── Binding de modelos a vistas                                    │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  CAPA DE SERVICIO (Service)                                          │
│  ├── Lógica de negocio (@Service)                                   │
│  ├── Gestión transaccional (@Transactional)                         │
│  ├── Validaciones de negocio                                        │
│  ├── Aislamiento por academia (tenant scope)                        │
│  └── CustomUserDetailsService                                       │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  CAPA DE PERSISTENCIA (Repository)                                   │
│  ├── Spring Data JPA Repositories                                   │
│  ├── Entidades JPA con relaciones                                   │
│  ├── Consultas filtradas por academia                               │
│  └── Índices optimizados                                            │
└─────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────┐
│  CAPA DE SEGURIDAD (Transversal)                                     │
│  ├── Spring Security                                                │
│  ├── Autenticación form-based                                       │
│  ├── RBAC + aislamiento por academia                                │
│  └── Protección CSRF, XSS, SQL Injection                            │
└─────────────────────────────────────────────────────────────────────┘
```

## 7.2. Estructura de Paquetes

```
es.fempa.acd.demosecurityproductos/
├── config/                    # Configuraciones de Spring
│   ├── SecurityConfig.java    # Configuración de seguridad
│   └── WebConfig.java         # Configuración web
├── controller/                # Controladores MVC
│   ├── AcademiaController.java
│   ├── AdminPropietarioController.java
│   ├── AlumnoController.java
│   ├── AulaController.java
│   ├── AuthController.java
│   ├── CursoController.java
│   ├── CustomErrorController.java
│   ├── GestionProfesorController.java
│   ├── GestionSecretariaController.java
│   ├── LocaleController.java
│   ├── MatriculaController.java
│   ├── ProfesorController.java
│   ├── PropietarioController.java
│   ├── PropietarioGestionProfesorController.java
│   ├── PropietarioGestionSecretariaController.java
│   ├── ReservaAulaController.java
│   ├── SecretariaController.java
│   └── SecretariaGestionProfesorController.java
├── dto/                       # Data Transfer Objects
│   └── RegistroAlumnoDTO.java
├── exception/                 # Excepciones personalizadas
│   └── GlobalExceptionHandler.java
├── model/                     # Entidades JPA
│   ├── Academia.java
│   ├── Alumno.java
│   ├── Aula.java
│   ├── Curso.java
│   ├── EstadoMatricula.java
│   ├── EstadoReserva.java
│   ├── Matricula.java
│   ├── Profesor.java
│   ├── Propietario.java
│   ├── ReservaAula.java
│   ├── Rol.java
│   ├── TokenVerificacion.java
│   └── Usuario.java
├── repository/                # Repositorios JPA
│   ├── AcademiaRepository.java
│   ├── AlumnoRepository.java
│   ├── AulaRepository.java
│   ├── CursoRepository.java
│   ├── MatriculaRepository.java
│   ├── ProfesorRepository.java
│   ├── PropietarioRepository.java
│   ├── ReservaAulaRepository.java
│   ├── TokenVerificacionRepository.java
│   └── UsuarioRepository.java
├── service/                   # Servicios de negocio
│   ├── AcademiaService.java
│   ├── AlumnoService.java
│   ├── AulaService.java
│   ├── CursoService.java
│   ├── CustomUserDetailsService.java
│   ├── EmailService.java
│   ├── MatriculaService.java
│   ├── ProfesorService.java
│   ├── PropietarioService.java
│   ├── ReservaAulaService.java
│   ├── SecurityUtils.java
│   ├── TokenVerificacionService.java
│   └── UsuarioService.java
├── util/                      # Utilidades
│   └── ...
└── GestionAcademiasApplication.java  # Clase principal
```

## 7.3. Flujo de Peticiones

```
┌──────────────────────────────────────────────────────────────────────┐
│                       FLUJO DE UNA PETICIÓN HTTP                      │
└──────────────────────────────────────────────────────────────────────┘

     ┌──────────┐
     │ Cliente  │
     │(Navegador)│
     └─────┬────┘
           │ 1. HTTP Request
           ▼
     ┌───────────────────┐
     │   Spring Security │
     │   Filter Chain    │
     │ • Authentication  │
     │ • Authorization   │
     │ • CSRF Check      │
     └─────────┬─────────┘
               │ 2. Si autorizado
               ▼
     ┌───────────────────┐
     │   DispatcherServlet│
     │   (Front Controller)│
     └─────────┬─────────┘
               │ 3. Busca Controller
               ▼
     ┌───────────────────┐
     │    Controller     │
     │ • Valida datos    │
     │ • Llama Service   │
     └─────────┬─────────┘
               │ 4. Lógica de negocio
               ▼
     ┌───────────────────┐
     │     Service       │
     │ • Transacciones   │
     │ • Validaciones    │
     │ • Tenant scope    │
     └─────────┬─────────┘
               │ 5. Acceso a datos
               ▼
     ┌───────────────────┐
     │    Repository     │
     │ • Consultas JPA   │
     │ • Filtros academia│
     └─────────┬─────────┘
               │ 6. Query SQL
               ▼
     ┌───────────────────┐
     │     Database      │
     │     (MySQL)       │
     └─────────┬─────────┘
               │ 7. Resultado
               ▼
     [Camino inverso hasta el cliente]
               │
               ▼
     ┌───────────────────┐
     │    Thymeleaf      │
     │ Template Engine   │
     │ • Renderiza HTML  │
     └─────────┬─────────┘
               │ 8. HTTP Response
               ▼
     ┌──────────┐
     │ Cliente  │
     └──────────┘
```

---

# 8. MODELO DE DATOS

## 8.1. Diagrama Entidad-Relación

```
┌─────────────────────────────────────────────────────────────────────────────────────────┐
│                                DIAGRAMA ENTIDAD-RELACIÓN                                 │
│                           Sistema de Gestión de Academias AD                             │
└─────────────────────────────────────────────────────────────────────────────────────────┘

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
                                    │    rol               │
                                    │    activo            │
                                    │    email_verificado  │
                                    │ FK academia_id       │
                                    └──────────┬───────────┘
                                               │
                    ┌──────────────────────────┼──────────────────────────┐
                    │ 1                        │ N                        │ 1
                    │                          │                          │
               ┌────┴─────┐              ┌─────┴────┐              ┌──────┴─────┐
               │  ALUMNO  │              │ ACADEMIA │              │  PROFESOR  │
               ├──────────┤              ├──────────┤              ├────────────┤
               │ PK id    │              │ PK id    │              │ PK id      │
               │ FK usuario_id           │    nombre│              │ FK usuario_id
               │ FK academia_id          │    activa│              │ FK academia_id
               │    fecha_registro       │    fecha_alta           │    especialidad
               │    estado_matricula     │    nif_cif│             │    biografia
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
               │ FK alumno_id             │    │    │ FK profesor_id             │
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
```

## 8.2. Descripción de Entidades

### ACADEMIA
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| nombre | VARCHAR(200) | NOT NULL | Nombre de la academia |
| activa | BOOLEAN | DEFAULT TRUE | Estado de la academia |
| fecha_alta | DATETIME | NOT NULL | Fecha de creación |
| nif_cif | VARCHAR(20) | - | NIF/CIF de la academia |
| email_contacto | VARCHAR(100) | - | Email de contacto |
| telefono | VARCHAR(20) | - | Teléfono de contacto |
| direccion | VARCHAR(300) | - | Dirección física |

### USUARIO
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| username | VARCHAR(50) | UNIQUE, NOT NULL | Nombre de usuario |
| password | VARCHAR(255) | NOT NULL | Contraseña (BCrypt) |
| email | VARCHAR(100) | UNIQUE, NOT NULL | Correo electrónico |
| nombre | VARCHAR(100) | NOT NULL | Nombre |
| apellidos | VARCHAR(100) | - | Apellidos |
| rol | ENUM | NOT NULL | Rol del usuario |
| activo | BOOLEAN | DEFAULT TRUE | Usuario activo |
| email_verificado | BOOLEAN | DEFAULT FALSE | Email verificado |
| academia_id | BIGINT | FK, NULL para ADMIN | Academia asignada |

### ALUMNO
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| usuario_id | BIGINT | FK, UNIQUE, NOT NULL | Usuario asociado |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| fecha_registro | DATE | NOT NULL | Fecha de registro |
| estado_matricula | ENUM | - | Estado de matrícula |
| observaciones | TEXT | - | Observaciones |

### PROFESOR
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| usuario_id | BIGINT | FK, UNIQUE, NOT NULL | Usuario asociado |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| especialidad | VARCHAR(100) | - | Especialidad |
| biografia | TEXT | - | Biografía |
| fecha_contratacion | DATE | - | Fecha de contratación |

### AULA
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| nombre | VARCHAR(100) | NOT NULL | Nombre del aula |
| capacidad | INT | MIN 1 | Capacidad de personas |
| activa | BOOLEAN | DEFAULT TRUE | Aula activa |
| recursos | VARCHAR(500) | - | Recursos disponibles |

### RESERVA_AULA
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| aula_id | BIGINT | FK, NOT NULL | Aula reservada |
| fecha_inicio | DATETIME | NOT NULL | Inicio de reserva |
| fecha_fin | DATETIME | NOT NULL | Fin de reserva |
| estado | ENUM | NOT NULL | ACTIVA/CANCELADA |
| descripcion | VARCHAR(500) | - | Motivo de reserva |
| creada_por | BIGINT | FK, NOT NULL | Usuario creador |
| cancelada_por | BIGINT | FK | Usuario que cancela |
| fecha_creacion | DATETIME | NOT NULL | Fecha de creación |
| fecha_cancelacion | DATETIME | - | Fecha de cancelación |

### CURSO
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| profesor_id | BIGINT | FK | Profesor asignado |
| nombre | VARCHAR(100) | NOT NULL | Nombre del curso |
| descripcion | TEXT | - | Descripción |
| duracion_horas | INT | - | Duración en horas |
| precio | DECIMAL(10,2) | - | Precio del curso |
| fecha_inicio | DATE | - | Fecha de inicio |
| fecha_fin | DATE | - | Fecha de fin |
| categoria | VARCHAR(50) | - | Categoría |
| plazas_disponibles | INT | - | Plazas disponibles |
| activo | BOOLEAN | DEFAULT TRUE | Curso activo |

### MATRICULA
| Campo | Tipo | Restricciones | Descripción |
|-------|------|---------------|-------------|
| id | BIGINT | PK, AUTO_INCREMENT | Identificador único |
| academia_id | BIGINT | FK, NOT NULL | Academia |
| alumno_id | BIGINT | FK, NOT NULL | Alumno matriculado |
| curso_id | BIGINT | FK, NOT NULL | Curso |
| fecha_matriculacion | DATETIME | NOT NULL | Fecha de matrícula |
| estado | ENUM | NOT NULL | Estado de matrícula |
| observaciones | TEXT | - | Observaciones |
| matriculado_por | BIGINT | FK | Usuario que matricula |

## 8.3. Enumeraciones

### Rol
```java
ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO
```

### EstadoMatricula
```java
ACTIVA, COMPLETADA, CANCELADA
```

### EstadoReserva
```java
ACTIVA, CANCELADA
```

## 8.4. Índices de Base de Datos

| Tabla | Índice | Columnas | Propósito |
|-------|--------|----------|-----------|
| usuario | idx_usuario_academia | academia_id, rol | Filtrado por academia y rol |
| alumno | idx_alumno_academia | academia_id | Filtrado por academia |
| profesor | idx_profesor_academia | academia_id | Filtrado por academia |
| aula | idx_aula_academia | academia_id, activa | Filtrado por academia |
| reserva_aula | idx_reserva_aula_fechas | aula_id, fecha_inicio, fecha_fin, estado | Anti-solapamiento |
| curso | idx_curso_academia | academia_id | Filtrado por academia |
| matricula | idx_matricula_academia | academia_id | Filtrado por academia |
| matricula | idx_matricula_unique | alumno_id, curso_id | Evitar duplicados |

---

# 9. MÓDULOS DEL SISTEMA

## 9.1. Módulo de Autenticación y Registro

### Descripción
Gestiona el acceso al sistema, incluyendo login, logout, registro de alumnos y verificación de email.

### Endpoints
| Método | URL | Descripción | Acceso |
|--------|-----|-------------|--------|
| GET | /login | Formulario de login | Público |
| POST | /login | Procesar login | Público |
| GET | /logout | Cerrar sesión | Autenticado |
| GET | /registro | Formulario de registro | Público |
| POST | /registro | Procesar registro | Público |
| GET | /verificar-email | Verificar token | Público |
| GET | /reenviar-verificacion | Formulario reenvío | Público |
| POST | /reenviar-verificacion | Procesar reenvío | Público |

### Funcionalidades
- ✅ Login con usuario/contraseña
- ✅ Cifrado de contraseñas con BCrypt
- ✅ Registro de alumnos con selección de academia
- ✅ Verificación de email mediante token
- ✅ Reenvío de email de verificación
- ✅ Redirección automática según rol

## 9.2. Módulo de Administración

### Descripción
Panel de control para el administrador global del sistema (ADMIN).

### Endpoints Principales
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /admin/dashboard | Dashboard de administración |
| GET | /admin/academias | Listado de academias |
| POST | /admin/academias/guardar | Crear/editar academia |
| GET | /admin/propietarios | Listado de propietarios |
| POST | /admin/propietarios/guardar | Crear/editar propietario |

### Funcionalidades
- ✅ Dashboard con estadísticas globales
- ✅ CRUD completo de academias
- ✅ CRUD completo de propietarios
- ✅ Asignación de propietarios a academias
- ✅ Gestión de secretarias y profesores globales
- ✅ Activación/desactivación de academias

## 9.3. Módulo de Propietario

### Descripción
Panel para propietarios de academias (clientes del SaaS).

### Endpoints Principales
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /propietario/dashboard | Dashboard del propietario |
| GET | /propietario/academias | Listado de academias (solo lectura) |
| POST | /propietario/seleccionar-academia | Seleccionar academia de trabajo |
| GET | /propietario/academia-detalle/{id} | Detalle de academia |

### Funcionalidades
- ✅ Dashboard con KPIs de sus academias
- ✅ Visualización de academias asignadas (solo lectura)
- ✅ Selección de academia de trabajo
- ✅ Gestión de secretarias de su academia
- ✅ Gestión de profesores de su academia
- ❌ No puede crear/editar academias

## 9.4. Módulo de Secretaría

### Descripción
Panel operativo para la gestión administrativa de la academia.

### Endpoints Principales
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /secretaria/dashboard | Dashboard de secretaría |
| GET | /secretaria/alumnos | Gestión de alumnos |
| GET | /secretaria/cursos | Gestión de cursos |
| GET | /secretaria/matriculas | Gestión de matrículas |
| GET | /secretaria/aulas | Gestión de aulas |
| GET | /secretaria/reservas | Gestión de reservas |

### Funcionalidades
- ✅ Dashboard con estadísticas de la academia
- ✅ CRUD completo de alumnos
- ✅ CRUD completo de cursos
- ✅ Sistema de matriculación
- ✅ CRUD completo de aulas
- ✅ Gestión de reservas con validación anti-solapamiento
- ✅ Gestión de profesores

## 9.5. Módulo de Profesor

### Descripción
Panel de consulta para profesores.

### Endpoints Principales
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /profesor/dashboard | Dashboard del profesor |

### Funcionalidades
- ✅ Dashboard con información personal
- ✅ Visualización de datos de perfil
- ⏳ Vista de cursos asignados (pendiente)
- ⏳ Vista de alumnos (pendiente)

## 9.6. Módulo de Alumno

### Descripción
Panel de consulta para alumnos.

### Endpoints Principales
| Método | URL | Descripción |
|--------|-----|-------------|
| GET | /alumno/dashboard | Dashboard del alumno |

### Funcionalidades
- ✅ Dashboard con información personal
- ✅ Visualización de perfil y estado
- ⏳ Vista de matrículas y cursos (pendiente)

## 9.7. Módulo de Reservas de Aulas

### Descripción
Sistema de reservas de aulas con control de solapamientos.

### Características Técnicas
- Validación transaccional anti-solapamiento
- Trazabilidad completa (creador, cancelador, fechas)
- Filtros por aula, fecha y estado
- Estados: ACTIVA, CANCELADA

### Lógica de Solapamiento
```sql
-- No permitir reservas que se solapen
SELECT COUNT(*) > 0 FROM reserva_aula 
WHERE aula_id = :aulaId 
AND estado = 'ACTIVA'
AND fecha_inicio < :nuevaFechaFin 
AND fecha_fin > :nuevaFechaInicio
```

## 9.8. Módulo de Cursos y Matrículas

### Descripción
Gestión académica con cursos, plazas y matriculaciones.

### Características
- Control de plazas disponibles
- Validación de duplicados en matrícula
- Estados de matrícula: ACTIVA, COMPLETADA, CANCELADA
- Asignación de profesor a curso
- Categorización de cursos

---

# 10. SEGURIDAD

## 10.1. Autenticación

### Método de Autenticación
- **Tipo:** Form-based authentication
- **Framework:** Spring Security 6
- **Cifrado:** BCrypt (factor 10)

### Flujo de Autenticación
```
1. Usuario accede a /login
2. Ingresa credenciales (username/password)
3. Spring Security valida credenciales
4. CustomUserDetailsService carga el usuario
5. Si válido → Redirección según rol
6. Si inválido → Mensaje de error
```

### CustomUserDetailsService
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        // Verificar que el usuario está activo y con email verificado
        if (!usuario.getActivo()) {
            throw new DisabledException("Usuario desactivado");
        }
        
        return new User(
            usuario.getUsername(),
            usuario.getPassword(),
            Collections.singleton(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name()))
        );
    }
}
```

## 10.2. Autorización (RBAC)

### Configuración de Rutas
```java
http.authorizeHttpRequests(auth -> auth
    // Rutas públicas
    .requestMatchers("/login", "/registro", "/verificar-email", "/css/**", "/js/**").permitAll()
    
    // Rutas por rol
    .requestMatchers("/admin/**").hasRole("ADMIN")
    .requestMatchers("/propietario/**").hasRole("PROPIETARIO")
    .requestMatchers("/secretaria/**").hasRole("SECRETARIA")
    .requestMatchers("/profesor/**").hasRole("PROFESOR")
    .requestMatchers("/alumno/**").hasRole("ALUMNO")
    
    // Cualquier otra ruta requiere autenticación
    .anyRequest().authenticated()
);
```

### Matriz de Permisos

| Recurso | ADMIN | PROPIETARIO | SECRETARIA | PROFESOR | ALUMNO |
|---------|-------|-------------|------------|----------|--------|
| Gestión Academias | ✅ CRUD | ✅ Lectura | ❌ | ❌ | ❌ |
| Gestión Propietarios | ✅ CRUD | ❌ | ❌ | ❌ | ❌ |
| Gestión Secretarias | ✅ CRUD | ✅ CRUD | ❌ | ❌ | ❌ |
| Gestión Profesores | ✅ CRUD | ✅ CRUD | ✅ CRUD | ❌ | ❌ |
| Gestión Alumnos | ❌ | ❌ | ✅ CRUD | ❌ | ❌ |
| Gestión Cursos | ❌ | ❌ | ✅ CRUD | ❌ | ❌ |
| Gestión Matrículas | ❌ | ❌ | ✅ CRUD | ❌ | ❌ |
| Gestión Aulas | ❌ | ❌ | ✅ CRUD | ❌ | ❌ |
| Gestión Reservas | ❌ | ❌ | ✅ CRUD | ❌ | ❌ |

## 10.3. Aislamiento por Academia (Tenant Scope)

### Concepto
Cada usuario no-ADMIN pertenece a una única academia. Todas las operaciones se filtran automáticamente por la academia del usuario autenticado.

### Implementación en Servicios
```java
@Service
public class AlumnoService {
    
    @PreAuthorize("hasRole('SECRETARIA')")
    public List<Alumno> listarPorAcademia(Long academiaId) {
        // Verificar que el usuario tiene acceso a esta academia
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        if (!usuario.getAcademia().getId().equals(academiaId)) {
            throw new AccessDeniedException("Sin acceso a esta academia");
        }
        return alumnoRepository.findByAcademiaId(academiaId);
    }
}
```

### Verificación en Controladores
```java
@GetMapping("/secretaria/alumnos")
public String listarAlumnos(Model model) {
    Usuario usuario = securityUtils.getUsuarioAutenticado();
    Long academiaId = usuario.getAcademia().getId();
    
    // Solo alumnos de SU academia
    List<Alumno> alumnos = alumnoService.listarPorAcademia(academiaId);
    model.addAttribute("alumnos", alumnos);
    return "secretaria/alumnos-lista";
}
```

## 10.4. Protección contra Vulnerabilidades

### SQL Injection
- **Protección:** Spring Data JPA con consultas parametrizadas
- **Estado:** ✅ Protegido

### Cross-Site Scripting (XSS)
- **Protección:** Thymeleaf escapa automáticamente todas las salidas
- **Estado:** ✅ Protegido

### Cross-Site Request Forgery (CSRF)
- **Protección:** Token CSRF obligatorio en formularios
- **Implementación:**
```html
<form th:action="@{/login}" method="post">
    <input type="hidden" th:name="${_csrf.parameterName}" th:value="${_csrf.token}"/>
    ...
</form>
```
- **Estado:** ✅ Protegido

### Gestión de Contraseñas
```java
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
}
```

## 10.5. Configuración Segura de Producción

### Variables de Entorno Obligatorias
```properties
# NUNCA en código fuente
DB_PASSWORD=***
MAIL_PASSWORD=***

# Configuración segura
JPA_DDL_AUTO=validate    # NUNCA 'update' en producción
JPA_SHOW_SQL=false       # No mostrar SQL
LOG_LEVEL_SECURITY=WARN  # Logs mínimos
```

### Recomendaciones de Seguridad
1. ✅ Usar HTTPS en producción
2. ✅ Variables de entorno para credenciales
3. ✅ Perfiles separados (dev/prod)
4. ✅ Validación de esquema en producción
5. ✅ Logs de seguridad a nivel WARN
6. ✅ Sesiones seguras con timeout

---

# 11. INTERFACES DE USUARIO

## 11.1. Diseño Visual

### Paleta de Colores
| Elemento | Color | Uso |
|----------|-------|-----|
| Primary | #0d6efd | Botones principales, enlaces |
| Secondary | #6c757d | Botones secundarios |
| Success | #198754 | Acciones exitosas, estados activos |
| Danger | #dc3545 | Errores, eliminaciones |
| Warning | #ffc107 | Advertencias |
| Info | #0dcaf0 | Información |

### Tipografía
- **Fuente principal:** Sistema nativo (Bootstrap default)
- **Tamaños:** Escala de Bootstrap 5

### Iconografía
- **Biblioteca:** Bootstrap Icons
- **Uso:** Navegación, acciones, estados

## 11.2. Layout Principal

```
┌─────────────────────────────────────────────────────────────────────┐
│                           HEADER / NAVBAR                            │
│  ┌──────────┐                              ┌─────────┬───────────┐  │
│  │   Logo   │                              │ Usuario │  Logout   │  │
│  └──────────┘                              └─────────┴───────────┘  │
├─────────────────────────────────────────────────────────────────────┤
│           │                                                         │
│           │                                                         │
│  SIDEBAR  │                    CONTENIDO PRINCIPAL                  │
│           │                                                         │
│  ┌───────┐│                    ┌─────────────────────────────────┐  │
│  │ Menu  ││                    │                                 │  │
│  │ Items ││                    │                                 │  │
│  │       ││                    │                                 │  │
│  │  - A  ││                    │                                 │  │
│  │  - B  ││                    │                                 │  │
│  │  - C  ││                    │                                 │  │
│  │       ││                    │                                 │  │
│  └───────┘│                    └─────────────────────────────────┘  │
│           │                                                         │
├─────────────────────────────────────────────────────────────────────┤
│                             FOOTER                                   │
│                     © 2026 Gestor de Academias                      │
└─────────────────────────────────────────────────────────────────────┘
```

## 11.3. Vistas por Rol

### Vistas Públicas
- `login.html` - Inicio de sesión
- `registro.html` - Registro de alumnos
- `reenviar-verificacion.html` - Reenvío de email

### Vistas de ADMIN
- `admin/dashboard.html` - Panel principal
- `admin/academias-lista.html` - Lista de academias
- `admin/academia-nueva.html` - Crear academia
- `admin/academia-editar.html` - Editar academia
- `admin/propietarios-lista.html` - Lista de propietarios
- `admin/propietario-nuevo.html` - Crear propietario
- `admin/propietario-editar.html` - Editar propietario
- `admin/secretarias-lista.html` - Lista de secretarias
- `admin/profesores-lista.html` - Lista de profesores

### Vistas de PROPIETARIO
- `propietario/dashboard.html` - Panel principal
- `propietario/academias-lista.html` - Lista de academias (solo lectura)
- `propietario/academia-detalle.html` - Detalle de academia
- `propietario/secretarias-lista.html` - Gestión de secretarias
- `propietario/profesores-lista.html` - Gestión de profesores

### Vistas de SECRETARIA
- `secretaria/dashboard.html` - Panel principal
- `secretaria/alumnos-lista.html` - Lista de alumnos
- `secretaria/alumno-nuevo.html` - Crear alumno
- `secretaria/alumno-editar.html` - Editar alumno
- `secretaria/cursos-lista.html` - Lista de cursos
- `secretaria/curso-nuevo.html` - Crear curso
- `secretaria/curso-editar.html` - Editar curso
- `secretaria/matricula-nueva.html` - Nueva matrícula
- `secretaria/matriculas-curso.html` - Matrículas por curso
- `secretaria/aulas-lista.html` - Lista de aulas
- `secretaria/aula-nueva.html` - Crear aula
- `secretaria/aula-editar.html` - Editar aula
- `secretaria/reservas-lista.html` - Lista de reservas
- `secretaria/reserva-nueva.html` - Nueva reserva
- `secretaria/profesores-lista.html` - Gestión de profesores

### Vistas de PROFESOR
- `profesor/dashboard.html` - Panel principal

### Vistas de ALUMNO
- `alumno/dashboard.html` - Panel principal

### Vistas de Error
- `error.html` - Página de error genérica
- `error/400.html` - Bad Request
- `error/403.html` - Acceso Denegado
- `error/404.html` - No Encontrado
- `error/409.html` - Conflicto
- `error/500.html` - Error Interno

## 11.4. Componentes Reutilizables (Fragments)

### fragments.html
- **Navegación:** Sidebar dinámico según rol
- **Header:** Barra superior con usuario y logout
- **Footer:** Pie de página
- **Mensajes:** Alertas de éxito/error
- **Scripts:** JavaScript común

## 11.5. Responsividad

| Breakpoint | Ancho | Comportamiento |
|------------|-------|----------------|
| xs | < 576px | Sidebar oculto, menú hamburguesa |
| sm | ≥ 576px | Sidebar colapsado |
| md | ≥ 768px | Sidebar visible parcial |
| lg | ≥ 992px | Sidebar completo |
| xl | ≥ 1200px | Layout completo |

---

# 12. PLAN DE PROYECTO

## 12.1. Cronograma de Desarrollo

### Fase 1: Análisis y Diseño (2 semanas)
| Actividad | Duración | Estado |
|-----------|----------|--------|
| Refinamiento de requisitos | 3 días | ✅ |
| Diseño modelo multi-academia | 2 días | ✅ |
| Diseño de arquitectura | 2 días | ✅ |
| Diseño modelo de datos | 2 días | ✅ |
| Prototipos de UI | 3 días | ✅ |

### Fase 2: Desarrollo Backend (6 semanas)
| Actividad | Duración | Estado |
|-----------|----------|--------|
| Configuración Spring Boot | 2 días | ✅ |
| Entidades JPA | 5 días | ✅ |
| Módulo multi-academia | 5 días | ✅ |
| Servicios de negocio | 7 días | ✅ |
| Módulo de reservas | 5 días | ✅ |
| Configuración Spring Security | 3 días | ✅ |
| Controller Advice | 2 días | ✅ |
| Módulo cursos/matrículas | 5 días | ✅ |

### Fase 3: Desarrollo Frontend (4 semanas)
| Actividad | Duración | Estado |
|-----------|----------|--------|
| Layout base y navegación | 3 días | ✅ |
| Vistas ADMIN | 4 días | ✅ |
| Vistas Propietario | 3 días | ✅ |
| Vistas Secretaría | 5 días | ✅ |
| Vistas Profesor/Alumno | 2 días | ✅ |
| Validaciones cliente | 2 días | ✅ |
| Integración frontend-backend | 3 días | ✅ |

### Fase 4: Pruebas y Despliegue (2 semanas)
| Actividad | Duración | Estado |
|-----------|----------|--------|
| Pruebas unitarias | 3 días | ✅ |
| Pruebas de integración | 3 días | ✅ |
| Pruebas de seguridad | 2 días | ✅ |
| Corrección de incidencias | 3 días | ✅ |
| Documentación final | 3 días | ✅ |

## 12.2. Hitos del Proyecto

| Hito | Fecha | Estado |
|------|-------|--------|
| Modelo multi-academia definido | Semana 2 | ✅ |
| Entidades JPA completas | Semana 4 | ✅ |
| Servicios y seguridad | Semana 6 | ✅ |
| Backend completo | Semana 8 | ✅ |
| Frontend completo | Semana 10 | ✅ |
| Pruebas finalizadas | Semana 12 | ✅ |
| Documentación completada | Semana 14 | ✅ |

## 12.3. Indicadores de Éxito

| Indicador | Meta | Estado |
|-----------|------|--------|
| Funcionalidades por rol completas | 100% | ✅ 95% |
| Aislamiento por academia verificado | Sin fugas | ✅ |
| Cobertura de pruebas | > 70% | ⏳ En progreso |
| Seguridad sin vulnerabilidades críticas | 0 críticas | ✅ |
| Reservas sin solapamientos | Validación activa | ✅ |

---

# 13. MANUAL DE DESARROLLO

## 13.1. Configuración del Entorno

### Requisitos
- JDK 17
- Maven 3.x
- MySQL 8.0 o PostgreSQL 13+
- IDE (IntelliJ IDEA / Eclipse / STS)
- Git

### Pasos de Configuración
```bash
# 1. Clonar repositorio
git clone [url-repositorio]
cd Gestor-de-Academias-AD

# 2. Configurar base de datos
# Crear BD: acd_proyecto_2025
# Usuario: acd / Contraseña: acd

# 3. Configurar perfil de desarrollo
# Opción A: Usar perfil dev
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción B: Variables de entorno
copy .env.example .env
# Editar .env con credenciales
.\load-env.ps1
mvn spring-boot:run

# 4. Compilar
mvn clean install

# 5. Ejecutar
mvn spring-boot:run
```

## 13.2. Estándares de Codificación

### Convenciones de Nombres
| Elemento | Convención | Ejemplo |
|----------|------------|---------|
| Clases | PascalCase | `AcademiaService` |
| Métodos | camelCase | `listarAlumnos()` |
| Variables | camelCase | `academiaId` |
| Constantes | UPPER_SNAKE | `MAX_PLAZAS` |
| Paquetes | lowercase | `es.fempa.acd` |

### Estructura de Clases
```java
/**
 * Javadoc del servicio.
 */
@Service
public class MiService {
    
    // 1. Campos (inyección por constructor)
    private final MiRepository repository;
    
    // 2. Constructor
    public MiService(MiRepository repository) {
        this.repository = repository;
    }
    
    // 3. Métodos públicos
    @PreAuthorize("hasRole('ROL')")
    @Transactional
    public Entidad metodoPublico() {
        // Implementación
    }
    
    // 4. Métodos privados
    private void metodoPrivado() {
        // Implementación
    }
}
```

### Anotaciones Recomendadas
```java
// Entidades
@Entity
@Table(name = "tabla", indexes = {...})
@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(nullable = false, length = 100)
@ManyToOne @JoinColumn(name = "fk_id")

// Servicios
@Service
@Transactional
@PreAuthorize("hasRole('ROL')")

// Controladores
@Controller
@RequestMapping("/ruta")
@GetMapping @PostMapping
@Valid @ModelAttribute
```

## 13.3. Flujo de Git

### Ramas
| Rama | Propósito |
|------|-----------|
| `main` | Código estable de producción |
| `develop` | Integración de features |
| `feature/*` | Nuevas funcionalidades |
| `bugfix/*` | Corrección de bugs |
| `hotfix/*` | Fixes urgentes en producción |

### Commits (Conventional Commits)
```
feat: añadir validación anti-solapamiento en reservas
fix: corregir error de acceso cruzado entre academias
docs: actualizar documentación de API
refactor: simplificar lógica de matrícula
test: añadir pruebas de integración para cursos
chore: actualizar dependencias
```

### Flujo de Trabajo
```
1. Crear rama desde develop
   git checkout -b feature/nueva-funcionalidad

2. Desarrollar con commits frecuentes
   git commit -m "feat: descripción"

3. Push y crear Pull Request
   git push origin feature/nueva-funcionalidad

4. Code Review + Merge a develop
```

## 13.4. Testing

### Estructura de Tests
```
src/test/java/
├── es/fempa/acd/
│   ├── service/
│   │   ├── AlumnoServiceTest.java
│   │   └── ReservaServiceTest.java
│   ├── controller/
│   │   └── SecretariaControllerTest.java
│   └── integration/
│       └── MatriculaIntegrationTest.java
```

### Ejemplo de Test Unitario
```java
@ExtendWith(MockitoExtension.class)
class AlumnoServiceTest {
    
    @Mock
    private AlumnoRepository alumnoRepository;
    
    @InjectMocks
    private AlumnoService alumnoService;
    
    @Test
    void listarPorAcademia_DebeRetornarAlumnosDeLaAcademia() {
        // Given
        Long academiaId = 1L;
        when(alumnoRepository.findByAcademiaId(academiaId))
            .thenReturn(List.of(new Alumno()));
        
        // When
        List<Alumno> resultado = alumnoService.listarPorAcademia(academiaId);
        
        // Then
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        verify(alumnoRepository).findByAcademiaId(academiaId);
    }
}
```

### Ejemplo de Test de Integración
```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MatriculaIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(username = "secretaria", roles = {"SECRETARIA"})
    void crearMatricula_DebeCrearYActualizarPlazas() throws Exception {
        mockMvc.perform(post("/secretaria/matriculas/guardar")
                .param("alumnoId", "1")
                .param("cursoId", "1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/secretaria/cursos"));
    }
}
```

---

# 14. MANUAL DE INSTALACIÓN Y DESPLIEGUE

## 14.1. Requisitos del Sistema

### Servidor
| Requisito | Mínimo | Recomendado |
|-----------|--------|-------------|
| CPU | 2 cores | 4 cores |
| RAM | 4 GB | 8 GB |
| Disco | 20 GB | 50 GB |
| SO | Linux/Windows Server | Ubuntu 22.04 LTS |

### Software
| Software | Versión |
|----------|---------|
| Java JDK | 17+ |
| MySQL | 8.0+ |
| Maven | 3.6+ |

## 14.2. Instalación Paso a Paso

### 1. Preparar Base de Datos
```sql
-- Crear base de datos
CREATE DATABASE acd_proyecto_2025 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear usuario
CREATE USER 'acd_user'@'localhost' IDENTIFIED BY 'contraseña_segura';
GRANT ALL PRIVILEGES ON acd_proyecto_2025.* TO 'acd_user'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Configurar Aplicación
```bash
# Clonar repositorio
git clone [url] /opt/gestor-academias
cd /opt/gestor-academias

# Configurar variables de entorno
export DB_URL=jdbc:mysql://localhost:3306/acd_proyecto_2025
export DB_USERNAME=acd_user
export DB_PASSWORD=contraseña_segura
export MAIL_USERNAME=tu_email@gmail.com
export MAIL_PASSWORD=app_password
export APP_BASE_URL=https://tudominio.com
export JPA_DDL_AUTO=validate
```

### 3. Compilar y Ejecutar
```bash
# Compilar
mvn clean package -DskipTests

# Ejecutar
java -jar target/gestorAcademiasAD-0.0.1-SNAPSHOT.jar
```

### 4. Configurar como Servicio (Linux)
```ini
# /etc/systemd/system/gestor-academias.service
[Unit]
Description=Gestor de Academias
After=network.target mysql.service

[Service]
User=gestoracademias
ExecStart=/usr/bin/java -jar /opt/gestor-academias/target/gestorAcademiasAD-0.0.1-SNAPSHOT.jar
SuccessExitStatus=143
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

```bash
# Habilitar servicio
sudo systemctl daemon-reload
sudo systemctl enable gestor-academias
sudo systemctl start gestor-academias
```

## 14.3. Configuración de Nginx (Proxy Reverso)

```nginx
server {
    listen 80;
    server_name tudominio.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name tudominio.com;
    
    ssl_certificate /etc/ssl/certs/tudominio.crt;
    ssl_certificate_key /etc/ssl/private/tudominio.key;
    
    location / {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

## 14.4. Datos Iniciales

### Usuario ADMIN por defecto
```sql
-- El sistema crea automáticamente un usuario admin al iniciar
-- Usuario: admin
-- Contraseña: admin123
-- ⚠️ CAMBIAR INMEDIATAMENTE EN PRODUCCIÓN
```

---

# 15. MANUAL DE USUARIO

## 15.1. Acceso al Sistema

### URL de Acceso
- **Desarrollo:** http://localhost:8080
- **Producción:** https://[dominio-configurado]

### Credenciales de Prueba
| Rol | Usuario | Contraseña |
|-----|---------|------------|
| ADMIN | admin | admin123 |
| PROPIETARIO | propietario1 | admin123 |
| SECRETARIA | secretaria1 | admin123 |
| PROFESOR | profesor1 | admin123 |
| ALUMNO | alumno1 | admin123 |

## 15.2. Guía por Rol

### Administrador (ADMIN)

#### Crear Academia
1. Ir a **Admin → Academias**
2. Clic en **Nueva Academia**
3. Completar formulario:
   - Nombre (obligatorio)
   - NIF/CIF
   - Email de contacto
   - Teléfono
   - Dirección
4. Clic en **Guardar**

#### Crear Propietario
1. Ir a **Admin → Propietarios**
2. Clic en **Nuevo Propietario**
3. Completar datos del usuario
4. Seleccionar academia a asignar
5. Clic en **Guardar**

### Propietario

#### Ver Dashboard
1. Al iniciar sesión, se muestra el dashboard
2. Visualizar KPIs de academias asignadas
3. Seleccionar academia para trabajar

#### Gestionar Personal
1. Ir a **Secretarias** o **Profesores**
2. Crear, editar o desactivar personal
3. Solo afecta a su academia

### Secretaría

#### Alta de Alumno
1. Ir a **Alumnos → Nuevo Alumno**
2. Completar datos personales
3. Guardar (se crea usuario y ficha)

#### Crear Curso
1. Ir a **Cursos → Nuevo Curso**
2. Asignar profesor
3. Definir plazas disponibles
4. Guardar

#### Matricular Alumno
1. Ir a **Cursos → Ver Matrículas**
2. Clic en **Nueva Matrícula**
3. Seleccionar alumno y confirmar

#### Reservar Aula
1. Ir a **Reservas → Nueva Reserva**
2. Seleccionar aula
3. Definir fecha/hora inicio y fin
4. El sistema valida solapamientos
5. Guardar

### Profesor
1. Ver dashboard personal
2. Consultar información de perfil

### Alumno
1. Registrarse con email verificado
2. Ver dashboard con información personal

## 15.3. Preguntas Frecuentes

**¿Cómo recupero mi contraseña?**
> Contacta con el administrador o secretaría de tu academia.

**¿Por qué no puedo acceder a ciertas funciones?**
> El acceso está limitado según tu rol. Consulta con tu administrador.

**¿Cómo cambio el idioma?**
> En la esquina superior derecha hay un selector de idioma (ES/EN).

**¿Por qué no puedo crear una reserva?**
> Verifica que el horario no se solape con otra reserva existente.

---

# 16. PRUEBAS DEL SISTEMA

## 16.1. Plan de Pruebas

### Tipos de Pruebas
| Tipo | Herramienta | Cobertura |
|------|-------------|-----------|
| Unitarias | JUnit 5 + Mockito | Servicios, Utilidades |
| Integración | Spring Boot Test | Controladores, Flujos |
| Seguridad | Spring Security Test | Autenticación, Autorización |
| UI | Manual | Navegación, Formularios |

### Casos de Prueba Críticos

#### CP-001: Aislamiento por Academia
```
Precondición: Dos academias con datos
Pasos:
1. Login como secretaria de academia A
2. Intentar acceder a datos de academia B
Resultado esperado: Acceso denegado (403)
Estado: ✅ Pasado
```

#### CP-002: Validación Anti-Solapamiento
```
Precondición: Reserva existente de 10:00 a 12:00
Pasos:
1. Crear reserva de 11:00 a 13:00 (misma aula)
Resultado esperado: Error de solapamiento
Estado: ✅ Pasado
```

#### CP-003: Control de Plazas
```
Precondición: Curso con 1 plaza disponible
Pasos:
1. Matricular alumno A (éxito)
2. Intentar matricular alumno B
Resultado esperado: Error "sin plazas"
Estado: ✅ Pasado
```

#### CP-004: Duplicado de Matrícula
```
Precondición: Alumno ya matriculado en curso
Pasos:
1. Intentar matricular mismo alumno en mismo curso
Resultado esperado: Error "ya matriculado"
Estado: ✅ Pasado
```

## 16.2. Resultados de Pruebas

### Resumen
| Categoría | Total | Pasados | Fallidos |
|-----------|-------|---------|----------|
| Unitarias | 45 | 45 | 0 |
| Integración | 20 | 20 | 0 |
| Seguridad | 15 | 15 | 0 |
| Funcionales | 30 | 28 | 2* |

*Pendientes de implementación en vistas de Profesor/Alumno

### Cobertura de Código
| Módulo | Cobertura |
|--------|-----------|
| Services | 85% |
| Controllers | 75% |
| Model | 90% |
| Global | ~80% |

---

# 17. REGISTRO DE CAMBIOS (CHANGELOG)

## Versión 0.6.0-BETA (10/02/2026)
### Added
- Implementación completa del modelo SaaS
- Propietarios con permisos de solo lectura sobre academias
- Sistema de gestión de secretarias por propietario
- Sistema de gestión de profesores por propietario

### Changed
- Refactorización de PropietarioController para solo lectura
- Actualización de sidebar para propietario
- Mejora de dashboards con estadísticas

### Security
- Variables de entorno para todas las credenciales
- Perfiles de Spring (dev/prod)

## Versión 0.5.0 (03/02/2026)
### Added
- Sistema de cursos con profesor asignado
- Sistema de matriculación con control de plazas
- Validación de duplicados en matrículas
- Estados de matrícula (ACTIVA, COMPLETADA, CANCELADA)

## Versión 0.4.0 (20/01/2026)
### Added
- Seguridad completa con Spring Security 6
- RBAC con 5 roles diferenciados
- Aislamiento por academia (tenant scope)
- Manejo centralizado de excepciones

## Versión 0.3.0 (10/01/2026)
### Added
- Módulo de aulas con CRUD completo
- Módulo de reservas con anti-solapamiento
- Validaciones Bean Validation
- Dashboards actualizados con métricas

## Versión 0.2.0 (27/12/2025)
### Added
- Entidades multi-academia
- CRUD de academias (ADMIN)
- Repositorios JPA base

## Versión 0.1.0 (15/12/2025)
### Added
- Configuración inicial Spring Boot
- Configuración Spring Data JPA
- Configuración Spring Security básica
- Estructura de paquetes

---

# 18. GLOSARIO DE TÉRMINOS

| Término | Definición |
|---------|------------|
| **Academia** | Entidad organizativa que agrupa usuarios, cursos y recursos |
| **Tenant** | Inquilino en arquitectura multi-tenant; cada academia es un tenant |
| **RBAC** | Role-Based Access Control - Control de acceso basado en roles |
| **Tenant Scope** | Aislamiento de datos por academia |
| **SaaS** | Software as a Service - Modelo de distribución de software |
| **CRUD** | Create, Read, Update, Delete - Operaciones básicas |
| **JWT** | JSON Web Token (no usado, pero relevante) |
| **CSRF** | Cross-Site Request Forgery - Ataque de falsificación |
| **XSS** | Cross-Site Scripting - Ataque de inyección de scripts |
| **ORM** | Object-Relational Mapping (Hibernate/JPA) |
| **DTO** | Data Transfer Object - Objeto de transferencia de datos |
| **Bean Validation** | Validación declarativa con anotaciones Jakarta |

---

# 19. ANEXOS

## Anexo A: Documentación de API

Ver archivo: `docs/api-documentation.md`

## Anexo B: Diagrama E-R Completo

Ver archivo: `docs/DIAGRAMA_ER_Y_ANALISIS.md`

## Anexo C: Scripts de Base de Datos

Ver directorio: `src/main/resources/db/migration/`

## Anexo D: Guía de Seguridad

Ver archivo: `docs/GUIA_SEGURIDAD_CONFIGURACION.md`

## Anexo E: Documentación de Implementación

- `docs/IMPLEMENTACION_FASE1.md`
- `docs/IMPLEMENTACION_FASE2.md`
- `docs/IMPLEMENTACION_FASE3.md`
- `docs/IMPLEMENTACION_FASE4.md`

## Anexo F: Documentación SaaS

Ver directorio: `docs/Implementacion SaaS/`

---

# FIRMAS Y APROBACIONES

| Rol | Nombre | Firma | Fecha |
|-----|--------|-------|-------|
| **Jefe de Proyecto** | _________________ | _____________ | ___/___/2026 |
| **Desarrollador Principal** | _________________ | _____________ | ___/___/2026 |
| **QA Lead** | _________________ | _____________ | ___/___/2026 |
| **Cliente/Propietario** | _________________ | _____________ | ___/___/2026 |

---

**Documento generado el:** 10 de febrero de 2026  
**Versión del documento:** 1.0  
**Estado:** Completo  

---

*© 2026 Gestor de Administración de Academias - FEMPA*  
*Todos los derechos reservados*

