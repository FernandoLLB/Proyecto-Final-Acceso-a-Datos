# Prototipo MVP - Gestor de Administración de Academias Multi-Academia

## Descripción del Prototipo

Este es el primer prototipo funcional (MVP) del sistema de gestión multi-academia con control de acceso basado en roles (RBAC). El objetivo principal es demostrar la viabilidad técnica del concepto de multi-tenancy y la navegación diferenciada por roles.

### Alcance del Prototipo (Hito 2)

✅ **Implementado:**
- Sistema multi-academia con aislamiento de datos (tenant scope)
- 5 roles diferenciados: ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO
- Autenticación con Spring Security y BCrypt
- Dashboards personalizados por rol con datos agregados
- Gestión básica de academias (CRUD) por ADMIN
- Base de datos MySQL con modelo relacional completo
- Navegación adaptada según permisos de usuario

🔄 **Pendiente para próximas fases:**
- Gestión completa de usuarios desde la aplicación
- Módulo de reservas de aulas con validación anti-solapamiento
- Sistema de cursos y matrículas
- Reportes avanzados y gráficas
- Optimizaciones de rendimiento

## Arquitectura Técnica

### Stack Tecnológico
- **Backend:** Spring Boot 3.4.1, Spring Security 6, Spring Data JPA
- **Frontend:** Thymeleaf 3.x + Bootstrap 5
- **Base de Datos:** MySQL 8.0
- **Lenguaje:** Java 17
- **Build Tool:** Maven

### Estructura del Proyecto
```
src/main/java/es/fempa/acd/demosecurityproductos/
├── config/          # Configuración de seguridad y manejo de excepciones
├── controller/      # Controladores MVC por rol
├── model/           # Entidades JPA (Academia, Usuario, Profesor, Alumno)
├── repository/      # Repositorios Spring Data JPA
└── service/         # Lógica de negocio y servicios
src/main/resources/
├── templates/       # Vistas Thymeleaf organizadas por rol
├── static/          # CSS, JS (Bootstrap 5)
├── application.properties
└── data.sql         # Datos de prueba iniciales
```

## Requisitos Previos

1. **JDK 17** o superior instalado
2. **Maven 3.x** instalado
3. **MySQL 8.0** o superior en ejecución
4. Puerto **3306** disponible para MySQL (o ajustar en `application.properties`)
5. Puerto **8090** disponible para la aplicación (o ajustar en `application.properties`)

## Instrucciones de Instalación y Ejecución

### 1. Configurar Base de Datos MySQL

Crear la base de datos en MySQL:

```sql
CREATE DATABASE acd_gestion_academias CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

Verificar que el usuario `root` tiene permisos (o ajustar credenciales en `application.properties`):

```sql
GRANT ALL PRIVILEGES ON acd_gestion_academias.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 2. Compilar el Proyecto

Desde la raíz del proyecto:

```bash
mvn clean install
```

### 3. Ejecutar la Aplicación

```bash
mvn spring-boot:run
```

La aplicación estará disponible en: **http://localhost:8090**

### 4. Datos de Prueba Precargados

Al iniciar la aplicación, se cargan automáticamente datos de prueba desde `data.sql`:

#### Credenciales de Acceso

**Contraseña para todos los usuarios:** `password123`

| Usuario        | Rol          | Academia       | Descripción                        |
|----------------|--------------|----------------|------------------------------------|
| `admin`        | ADMIN        | (Global)       | Administrador del sistema          |
| `propietario1` | PROPIETARIO  | TechLearn      | Propietario Academia TechLearn     |
| `secretaria1`  | SECRETARIA   | TechLearn      | Secretaría Academia TechLearn      |
| `profesor1`    | PROFESOR     | TechLearn      | Profesor de Programación Web       |
| `profesor2`    | PROFESOR     | TechLearn      | Profesora de Bases de Datos        |
| `alumno1`      | ALUMNO       | TechLearn      | Alumno de Desarrollo Web           |
| `alumno2`      | ALUMNO       | TechLearn      | Alumna interesada en Frontend      |
| `alumno3`      | ALUMNO       | TechLearn      | Alumno con conocimientos previos   |
| `propietario2` | PROPIETARIO  | InnovaEdu      | Propietaria Academia InnovaEdu     |
| `secretaria2`  | SECRETARIA   | InnovaEdu      | Secretaría Academia InnovaEdu      |
| `profesor3`    | PROFESOR     | InnovaEdu      | Profesor de Diseño Gráfico         |
| `profesor4`    | PROFESOR     | InnovaEdu      | Profesora de Marketing Digital     |
| `alumno4`      | ALUMNO       | InnovaEdu      | Alumno de Diseño Gráfico           |
| `alumno5`      | ALUMNO       | InnovaEdu      | Alumna de Marketing Digital        |
| `alumno6`      | ALUMNO       | InnovaEdu      | Alumno dado de baja temporalmente  |

## Funcionalidades por Rol

### 🔴 ADMIN (Administrador del Sistema)
- Ver dashboard con estadísticas globales (total academias, activas/inactivas, usuarios)
- Gestionar academias: crear, editar, activar/desactivar
- Acceso global a todas las academias sin restricciones
- **URL:** `/admin/dashboard`

### 🔵 PROPIETARIO (Propietario de Academia)
- Dashboard de negocio con KPIs de su academia:
  - Total alumnos (activos/inactivos)
  - Total profesores
  - Tasa de ocupación
- Vista limitada a datos de su propia academia
- **URL:** `/propietario/dashboard`

### ⚫ SECRETARIA (Secretaría de Academia)
- Dashboard operativo con:
  - Estadísticas de alumnos por estado
  - Últimos 5 alumnos registrados
  - Accesos rápidos (placeholder para próximas fases)
- Vista limitada a datos de su propia academia
- **URL:** `/secretaria/dashboard`

### 🟢 PROFESOR (Profesor de Academia)
- Dashboard personal con:
  - Información de perfil (especialidad, biografía, fecha contratación)
  - Datos de la academia asignada
  - Placeholder para cursos asignados (próxima fase)
- Vista limitada a su información personal y de su academia
- **URL:** `/profesor/dashboard`

### 🟡 ALUMNO (Alumno de Academia)
- Dashboard personal con:
  - Información de perfil (fecha registro, estado matrícula, observaciones)
  - Estado académico actual
  - Placeholder para horarios y cursos (próxima fase)
- Vista limitada a su información personal
- **URL:** `/alumno/dashboard`

## Pruebas Funcionales Sugeridas

### Escenario 1: Verificar Aislamiento Multi-Academia
1. Iniciar sesión como `propietario1` (TechLearn)
2. Observar estadísticas: solo datos de Academia TechLearn
3. Cerrar sesión
4. Iniciar sesión como `propietario2` (InnovaEdu)
5. Observar estadísticas: solo datos de Academia InnovaEdu
6. ✅ **Resultado esperado:** Los propietarios solo ven datos de su propia academia

### Escenario 2: Gestión de Academias (ADMIN)
1. Iniciar sesión como `admin`
2. Navegar a "Academias" → "Nueva Academia"
3. Crear academia "Academia Test" con datos completos
4. Verificar que aparece en el listado
5. Desactivar la academia
6. Verificar cambio de estado
7. ✅ **Resultado esperado:** CRUD de academias funcional solo para ADMIN

### Escenario 3: Navegación por Roles
1. Probar login con usuarios de cada rol
2. Verificar redirección automática al dashboard correspondiente
3. Intentar acceder a URLs de otros roles (ej: `/admin/dashboard` como ALUMNO)
4. ✅ **Resultado esperado:** Acceso denegado (403) al intentar acceder a recursos no autorizados

### Escenario 4: Dashboards con Datos Agregados
1. Iniciar sesión como `secretaria1`
2. Verificar listado de últimos alumnos registrados
3. Verificar contadores de alumnos activos/inactivos
4. Iniciar sesión como `profesor1`
5. Verificar información de perfil (especialidad, biografía)
6. ✅ **Resultado esperado:** Datos agregados correctos según rol

## Configuración Avanzada

### Cambiar Puerto de la Aplicación
Editar `src/main/resources/application.properties`:
```properties
server.port=8080
```

### Cambiar Credenciales MySQL
Editar `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/acd_gestion_academias?useSSL=false&serverTimezone=UTC
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Desactivar Recreación de BD en cada Arranque
Editar `src/main/resources/application.properties`:
```properties
spring.jpa.hibernate.ddl-auto=update  # En lugar de 'create'
spring.sql.init.mode=never             # Evita ejecutar data.sql
```

## Estructura de Base de Datos

### Tablas Principales
- **academia:** Información de cada academia (multi-tenant principal)
- **usuario:** Usuarios del sistema con rol y academia asignada
- **profesor:** Información adicional de profesores (1:1 con usuario)
- **alumno:** Información adicional de alumnos (1:1 con usuario)

### Relaciones Clave
- Usuario → Academia (ManyToOne, nullable solo para ADMIN)
- Profesor → Usuario (OneToOne)
- Profesor → Academia (ManyToOne)
- Alumno → Usuario (OneToOne)
- Alumno → Academia (ManyToOne)

## Troubleshooting

### Error: "Access denied for user 'javi'@'localhost'"
Verificar que el usuario MySQL existe y tiene permisos:
```sql
CREATE USER 'javi'@'localhost' IDENTIFIED BY '1qaz2wsx';
GRANT ALL PRIVILEGES ON acd_gestion_academias.* TO 'javi'@'localhost';
```

### Error: "Port 8090 already in use"
Cambiar el puerto en `application.properties` o detener el proceso que usa el puerto.

### Error: "Cannot resolve table 'academia'"
Warnings normales del IDE. La tabla se crea al ejecutar la aplicación.

### Las vistas Thymeleaf no cargan CSS correctamente
Verificar que los archivos Bootstrap están en: `src/main/resources/static/css/bootstrap/`

## Próximos Pasos (Roadmap)

### Fase 3: Gestión de Usuarios
- CRUD completo de usuarios por SECRETARIA/PROPIETARIO
- Alta de alumnos con validaciones
- Asignación de profesores

### Fase 4: Reservas de Aulas
- Gestión de aulas por academia
- Sistema de reservas con validación anti-solapamiento
- Calendario visual de reservas

### Fase 5: Módulo Académico
- Cursos y programas formativos
- Sistema de matrículas
- Asignación profesor-curso
- Horarios

### Fase 6: Optimizaciones
- Paginación en listados
- Índices de BD optimizados
- Caché para dashboards
- Tests unitarios y de integración

## Contacto y Soporte

Para dudas o incidencias sobre este prototipo, contactar al equipo de desarrollo.

---

**Versión del Prototipo:** 0.1.0-SNAPSHOT  
**Fecha:** 20/01/2026  
**Equipo:** Desarrollo Full Stack - Gestor de Academias
