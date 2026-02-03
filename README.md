# Gestor de Administración de Academias (Multi-Academia)

Sistema web diseñado para academias que desean digitalizar procesos administrativos y operativos. El proyecto adopta un enfoque **multi-tenant**, permitiendo que un **ADMIN global cree nuevas academias** sin necesidad de duplicar código ni despliegues.

El sistema garantiza **aislamiento de datos por academia**, control de acceso por roles, trazabilidad y un módulo de **reservas de aulas** con validación anti-solapamiento.

**🎉 Versión Actual: 0.6.0 - BETA (Backend + Frontend + Módulo Académico + Seguridad Reforzada)**

---

## 📦 Estado del Proyecto

### ✅ Implementado (Fases 1, 2, 3 y 4 Completadas)

#### Backend (Fases 1, 3 y 4) ✅
- ✅ Entidades JPA completas: Academia, Usuario, Profesor, Alumno, Aula, ReservaAula, **Curso, Matrícula**
- ✅ Repositorios con consultas optimizadas e índices
- ✅ Servicios con validaciones y aislamiento por academia (tenant scope)
- ✅ Controladores MVC para ADMIN, SECRETARIA, PROPIETARIO, PROFESOR, ALUMNO
- ✅ **Seguridad Spring Security con CSRF habilitado**
- ✅ **Gestión de sesiones mejorada (1 sesión por usuario)**
- ✅ **Manejo global de excepciones con logging**
- ✅ Validación anti-solapamiento de reservas (transaccional)
- ✅ **Sistema de matriculación con control de plazas y validación de duplicados**
- ✅ Bean Validation en todas las entidades

#### Frontend (Fases 2 y 3) ✅
- ✅ **14 vistas Thymeleaf completas** con Bootstrap 5
- ✅ Dashboard de Secretaria con KPIs y accesos rápidos
- ✅ CRUD completo de Aulas (lista, crear, editar, activar/desactivar)
- ✅ CRUD completo de Reservas con filtros (por aula, fecha, estado)
- ✅ CRUD completo de Alumnos con filtros (por estado de matrícula)
- ✅ **CRUD completo de Cursos (lista, crear, editar, activar/desactivar)**
- ✅ **Sistema de Matriculación (matricular, completar, cancelar)**
- ✅ Diseño responsive y moderno
- ✅ Validaciones HTML5 + JavaScript
- ✅ Navegación intuitiva con iconos Bootstrap

### 🚧 Pendiente (Próximas Fases)

- [ ] **Fase 5**: Optimización y Performance (paginación, caché, consultas optimizadas)
- [ ] **Fase 6**: Suite completa de tests (unitarios, integración, UI)
- [ ] **Fase 7**: Vistas para Profesor y Alumno (ver cursos, matrículas)

---

## 🚀 Funcionalidades Principales

### Multi-Academia
- Alta, baja y gestión de academias desde la interfaz.
- Aislamiento completo de datos (tenant scope).
- Administración centralizada por rol **ADMIN**.

### Gestión por Roles
| Rol | Funcionalidades |
|-----|-----------------|
| **ADMIN** | Gestión global de academias. Configuración inicial. |
| **PROPIETARIO** | KPIs, datos de negocio, métricas y panel ejecutivo. |
| **SECRETARÍA** | Altas/bajas de alumnos, gestión administrativa, reservas. |
| **PROFESOR** | Consulta de información y reservas asociadas. |
| **ALUMNO** | Visualización de información personal y académica. |

---

## 🏫 Reservas de Aulas

- Creación y gestión de reservas por fecha/hora.
- Validación anti-solapamiento en tiempo real.
- Trazabilidad: creador, cancelador, fechas relevantes.
- Filtros por aula, fecha y estado.
- Asociado a la academia del usuario autenticado.

---

## 🛠️ Tecnologías Utilizadas

**Backend**
- Java 17  
- Spring Boot 3  
- Spring Security  
- Spring Data JPA  
- Maven

**Frontend**
- Thymeleaf  
- Bootstrap 5  
- JS para validaciones cliente

**Base de Datos**
- MySQL 8.x o PostgreSQL 13+

---

## 🧱 Arquitectura

Arquitectura MVC separada en capas:

- **Presentación**: Thymeleaf + Bootstrap  
- **Controladores**: Spring MVC  
- **Servicios**: lógica de negocio + transacciones  
- **Persistencia**: JPA + repositorios  
- **Seguridad**: RBAC + aislamiento por academia  

---

## 🗄️ Modelo de Datos (Resumen)

Entidades principales:
- `Academia`
- `Usuario` (ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO)
- `Profesor`
- `Alumno`
- `Aula`
- `ReservaAula`
- **`Curso`** ✅ Implementado
- **`Matricula`** ✅ Implementado

**Módulo Académico Completado:**
- Sistema de cursos con profesor asignado
- Control de plazas disponibles
- Matriculación de alumnos con validación de duplicados
- Estados de matrícula (ACTIVA, COMPLETADA, CANCELADA)
- Trazabilidad completa

---

## 🔐 Seguridad

- Cifrado de contraseñas con **BCrypt**  
- Aislamiento por academia (tenant scope)  
- **RBAC** completo  
- Protección frente a XSS, SQL Injection y CSRF  
- Controller Advice para gestión centralizada de errores
- **✅ Variables de entorno para credenciales sensibles**
- **✅ Perfiles de Spring (dev, prod) para diferentes entornos**
- **✅ Configuración segura sin credenciales en código**

### ⚙️ Configuración Segura

**⚠️ IMPORTANTE**: Las credenciales ya NO están en el código fuente.

**Para desarrollo local:**
```powershell
# Opción 1: Usar perfil de desarrollo (más fácil)
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Opción 2: Usar variables de entorno
copy .env.example .env
# Edita .env con tus credenciales
.\load-env.ps1
mvn spring-boot:run
```

**📖 Documentación completa:**
- [🚀 Inicio Rápido](docs/INICIO_RAPIDO.md) - Configuración en 2 minutos
- [🔐 Guía de Seguridad](docs/GUIA_SEGURIDAD_CONFIGURACION.md) - Configuración completa y producción

---

## 📈 Escalabilidad y Rendimiento

- Índices por academia, aulas y fechas  
- Paginación en listados  
- Prevención N+1  
- Tests unitarios e integración  
- Versionado semántico y Changelog  

---

## 📅 Planificación del Proyecto

Fases de desarrollo:
1. **Análisis y diseño**
2. **Backend**
3. **Frontend**
4. **Pruebas y despliegue**

---

## 📚 Documentación

Este repositorio incluye:

- Documento de requisitos  
- Plan de proyecto  
- Especificaciones técnicas  
- Manual de desarrollo  
- Changelog  

Documento completo usado como base:  
`Documentacion.md` (incluido en este repositorio).  

---

## 📌 Estado del Proyecto

Versión inicial en desarrollo.  
Consultar el **Changelog** para roadmap y cambios recientes.

---

## 📝 Licencia

Pendiente de definir.
