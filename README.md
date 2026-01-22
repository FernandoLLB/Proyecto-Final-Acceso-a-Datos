# Gestor de Administración de Academias (Multi-Academia)

Sistema web diseñado para academias que desean digitalizar procesos administrativos y operativos. El proyecto adopta un enfoque **multi-tenant**, permitiendo que un **ADMIN global cree nuevas academias** sin necesidad de duplicar código ni despliegues.

El sistema garantiza **aislamiento de datos por academia**, control de acceso por roles, trazabilidad y un módulo de **reservas de aulas** con validación anti-solapamiento.

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

Entidades opcionales (académico):
- `Curso`
- `Matricula`

---

## 🔐 Seguridad

- Cifrado de contraseñas con **BCrypt**  
- Aislamiento por academia (tenant scope)  
- **RBAC** completo  
- Protección frente a XSS, SQL Injection y CSRF  
- Controller Advice para gestión centralizada de errores  

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
