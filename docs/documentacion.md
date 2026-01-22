Documentación mínima — Gestor de Administración de Academias (multi-academia)

Para asegurar una buena organización y especificación del proyecto, el equipo debe mantener al menos la siguiente documentación:

- Documento de Requisitos: Describe funcionalidades esperadas, requisitos técnicos, objetivos y alcance.
- Plan de Proyecto: Cronograma, hitos, fases y asignación de tareas.
- Especificaciones Técnicas: Arquitectura, tecnologías, herramientas, modelo de datos, interfaz y seguridad.
- Manual de Desarrollo: Estándares, procedimientos, flujo Git y guías de trabajo.
- Registro de Cambios (Changelog): Historial de modificaciones por versión.

Estos documentos ayudan a mantener claridad, trazabilidad y alineación durante el desarrollo.

Plantillas mínimas por documento

Documento de Requisitos

- Título del Proyecto
- Introducción
- Objetivos
- Requisitos Funcionales
- Requisitos No Funcionales
- Restricciones
- Suposiciones Iniciales

Plan de Proyecto

- Título del Proyecto
- Objetivo del Proyecto
- Cronograma
- Hitos
- Recursos
- Roles y Responsabilidades
- Indicadores de Éxito
- Suposiciones y Dependencias

Especificaciones Técnicas

- Arquitectura
- Tecnologías
- Estándares
- Modelo de Datos
- Interfaz
- Seguridad
- Escalabilidad y Rendimiento
- Indicadores (KPIs)

Manual de Desarrollo

- Objetivo del Manual
- Procedimientos
- Estándares de Codificación
- Uso de Git
- Testing
- Resolución de Conflictos
- Gestión de Versiones
- Indicadores de Calidad

Registro de Cambios (Changelog)

- Fecha
- Versión
- Cambios Realizados
- Autor

A continuación se incluye una versión completa y coherente de estos documentos para el proyecto actual.

————————————————————————————————————

1. Documento de Requisitos del Proyecto: Gestor de Administración de Academias (multi-academia)
   ————————————————————————————————————
2. Título del Proyecto
   Gestor de Administración de Academias (multi-academia) con reservas de aulas.
3. Introducción
   El proyecto consiste en el desarrollo de una aplicación web para academias que desean digitalizar procesos administrativos y operativos. El sistema será multi-academia (multi-tenant): un rol ADMIN del programa podrá crear nuevas academias desde la interfaz, evitando duplicar despliegues o código para cada nuevo cliente. Cada academia trabajará con aislamiento lógico de datos y con control de acceso basado en roles.

El sistema incluirá paneles y funcionalidades específicas según el rol del usuario dentro de una academia: Propietario (orientado a datos de negocio), Secretaría (operativa administrativa), Profesor (información referente a su actividad) y Alumno (información referente a su situación académica). Además, se incorporará un sistema de reservas de aulas por horarios.

3. Objetivos del Proyecto

3.1. Objetivo General
Desarrollar un gestor robusto, seguro y escalable para la administración de academias, con soporte multi-academia, roles diferenciados y reservas de aulas por franjas horarias.

3.2. Objetivos Específicos

- Implementar un sistema multi-academia que permita al ADMIN crear academias desde la UI.
- Proveer autenticación y autorización (RBAC) con aislamiento de datos por academia (tenant scope).
- Facilitar la operativa diaria de Secretaría: altas/bajas de alumnos y gestión administrativa asociada.
- Proveer un panel de negocio para Propietario con KPIs relevantes.
- Proveer paneles de consulta para Profesor y Alumno.
- Implementar reservas de aulas por horarios con control de solapamientos y trazabilidad.
- Mantener validaciones robustas y manejo centralizado de excepciones (Bean Validation y Controller Advice).

4. Requisitos Funcionales

4.1. Gestión de Academias (ADMIN de plataforma)

- Crear una nueva academia desde la interfaz (alta de cliente).
- Activar/desactivar academias.
- Visualizar listado de academias y su estado (activa/inactiva).
- (Opcional) Configuración inicial por academia (parámetros básicos, datos de contacto, etc.).

4.2. Gestión de Usuarios y Roles (multi-academia)

- Autenticación mediante usuario y contraseña.
- Gestión de usuarios con roles:

  - ADMIN (plataforma)
  - PROPIETARIO (por academia)
  - SECRETARIA (por academia)
  - PROFESOR (por academia)
  - ALUMNO (por academia)
- Alta/edición/desactivación lógica de usuarios.
- Asignación de usuarios a una academia (obligatorio para todos salvo ADMIN).
- Restricción de acceso a datos y operaciones únicamente dentro de la academia del usuario (salvo ADMIN).

4.3. Panel Propietario (datos de negocio)

- Visualizar indicadores de negocio de su academia (ejemplos típicos):

  - Número de alumnos activos, altas/bajas por periodo.
  - (Si existe modelo económico) ingresos por periodo y por curso/servicio.
  - Uso de aulas: ocupación por franja horaria, aulas más usadas, reservas por periodo.
- Listados y consultas agregadas orientadas a toma de decisiones.

4.4. Panel Secretaría (operativa administrativa)

- Alta de alumnos (creación de usuario y ficha de alumno).
- Baja de alumnos (desactivación lógica y trazabilidad).
- Consulta y mantenimiento de información administrativa de alumnos.
- Gestión operativa relacionada con matrículas/inscripciones (si aplica al alcance).
- Gestión de reservas de aulas (según política de permisos definida).

4.5. Panel Profesor

- Visualizar información referente al profesor autenticado:

  - Datos de perfil asociados.
  - (Si se usa modelo académico) cursos/grupos asignados, horarios y alumnos relacionados según permisos.
- Visualizar/consultar reservas de aulas asociadas a su actividad (si se define).

4.6. Panel Alumno

- Visualizar información referente al alumno autenticado:

  - Perfil y estado (activo/inactivo).
  - (Si se usa modelo académico) matrículas/inscripciones, horarios, cursos asociados.
- Consultar información relevante publicada por la academia (si aplica).

4.7. Reservas de Aulas por Horarios

- Gestión de aulas por academia (alta/edición/baja lógica).
- Crear reservas de aula con:

  - Aula
  - Fecha/hora inicio
  - Fecha/hora fin
  - Descripción/motivo (opcional)
  - Creada por (usuario)
- Consultar reservas por:

  - Aula
  - Rango de fechas
  - Estado (activa/cancelada)
- Cancelar reservas (según rol).
- Validación de conflictos: impedir solapamientos en la misma aula dentro del mismo intervalo horario.
- Trazabilidad mínima: quién crea/cancela/modifica, y cuándo.

5. Requisitos No Funcionales

5.1. Seguridad

- Contraseñas cifradas con BCrypt.
- Autorización basada en roles (RBAC) y aislamiento por academia (tenant scope).
- Protección frente a ataques comunes: SQL Injection, XSS, CSRF.
- Validación de entradas y escapes de salida en vistas.

5.2. Rendimiento

- Respuesta de páginas < 2 segundos bajo carga normal.
- Operaciones CRUD habituales < 500 ms en condiciones normales.
- Optimización de consultas con JPA/Hibernate y uso de índices adecuados (especialmente por academia y fechas).

5.3. Compatibilidad

- Diseño responsivo para móvil, tablet y escritorio.
- Compatibilidad con navegadores principales: Chrome, Firefox, Edge, Safari.
- Backend Spring Boot 3.4.1 y Java 17.

5.4. Usabilidad

- Interfaz clara y adaptada por rol.
- Mensajes de validación en español.
- Feedback visual de operaciones exitosas/fallidas.

5.5. Mantenibilidad

- Arquitectura MVC por capas.
- Validaciones consistentes.
- Controller Advice para manejo centralizado de errores.
- Testing unitario e integración con JUnit 5 y Mockito.

6. Restricciones

- Spring Boot como framework backend.
- Thymeleaf como motor de plantillas.
- Base de datos relacional (MySQL o PostgreSQL).
- Uso obligatorio de Spring Security.
- Plazo inicial de desarrollo estimado: 4 meses (ajustable por planificación real).

7. Suposiciones Iniciales

- El ADMIN creará academias y configurará el arranque inicial.
- La mayoría de usuarios se crearán por alta interna (Secretaría/Propietario), no por autorregistro público (si se desea autorregistro debe definirse explícitamente).
- Cada usuario no ADMIN pertenece exactamente a una academia.
- Las reservas de aulas se gestionan por intervalos de tiempo y requieren validación anti-solapamiento.

————————————————————————————————————2. Documento Plan de Proyecto: Gestor de Administración de Academias (multi-academia)————————————————————————————————————

3. Título del Proyecto
   Gestor de Administración de Academias (multi-academia) con reservas de aulas.
4. Objetivo del Proyecto
   Planificar y organizar el desarrollo de un sistema web multi-academia, asegurando entrega incremental, seguridad, aislamiento por academia y cumplimiento de requisitos por rol.
5. Cronograma del Proyecto
   El desarrollo se divide en cuatro fases principales:

Fase 1: Análisis y Diseño (2 semanas)

- Refinamiento de requisitos por rol.
- Diseño del modelo multi-academia (Academia como tenant).
- Diseño de arquitectura y seguridad (RBAC + tenant scope).
- Diseño del modelo de datos: usuarios, perfiles, aulas y reservas.
- Prototipos de UI por rol.

Fase 2: Desarrollo Backend (6 semanas)

- Configuración del proyecto Spring Boot con dependencias.
- Implementación de entidades JPA y repositorios.
- Implementación del módulo multi-academia:

  - CRUD de Academia (ADMIN).
  - Asociaciones y filtros por academia en el resto del dominio.
- Implementación de servicios de negocio y validaciones.
- Implementación de reservas con validación anti-solapamiento.
- Configuración de Spring Security con roles y autorización.
- Controller Advice para manejo centralizado de excepciones.

Fase 3: Desarrollo Frontend (4 semanas)

- Thymeleaf + Bootstrap, layout base y navegación por rol.
- Vistas:

  - ADMIN: gestión de academias.
  - Propietario: panel de negocio (KPIs).
  - Secretaría: alumnos y reservas.
  - Profesor: consulta de información.
  - Alumno: consulta de información.
- Formularios con validación cliente/servidor.
- Integración completa frontend-backend.

Fase 4: Pruebas y Despliegue (2 semanas)

- Pruebas unitarias e integración.
- Pruebas de seguridad (roles y aislamiento por academia).
- Pruebas funcionales de reservas (solapamientos, cancelación).
- Corrección de incidencias.
- Despliegue en entorno de pruebas.
- Documentación final.

4. Hitos del Proyecto

- Semana 2: Modelo multi-academia definido, entidades base y prototipos UI.
- Semana 4: Entidades JPA y repositorios completos (incluye Academia, Usuario, Aula, Reserva).
- Semana 6: Servicios y seguridad configurados (RBAC + tenant scope).
- Semana 8: Backend completo con controladores.
- Semana 10: Frontend completo e integrado.
- Semana 12: Pruebas finalizadas y correcciones aplicadas.
- Semana 14: Despliegue y documentación completada.

5. Recursos del Proyecto

5.1. Humanos

- Desarrollador Full Stack.
- Tester/QA.
- Coordinador Técnico.

5.2. Tecnológicos

- Lenguajes: Java 17, HTML5, CSS3, JavaScript.
- Frameworks: Spring Boot 3.4.1, Spring Security 6, Spring Data JPA, Thymeleaf.
- Dependencias: Thymeleaf Extras Spring Security 6, Spring Data Rest WebMVC, MySQL Connector J (runtime).
- Base de datos: MySQL 8.0 o PostgreSQL 13+.
- Herramientas: Maven, Git, GitHub, IDE (IntelliJ/Eclipse/STS).
- Testing: JUnit 5, Mockito, Spring Boot Test, Spring Security Test.

5.3. Infraestructura

- Servidor local con MySQL/PostgreSQL.
- Entorno de pruebas (AWS/Azure/Heroku o similar).

6. Roles y Responsabilidades

- Desarrollador Full Stack: implementación completa (backend, frontend, seguridad, reservas).
- Tester/QA: casos de prueba, pruebas de seguridad, documentación de bugs.
- Coordinador Técnico: seguimiento, revisiones, aprobación PRs, estándares.

7. Indicadores de Éxito

- Funcionalidades por rol completas.
- Aislamiento por academia verificado (sin fugas de datos).
- Cobertura de pruebas > 70%.
- Seguridad operativa sin vulnerabilidades críticas.
- Reservas sin solapamientos y con trazabilidad.

8. Suposiciones y Dependencias
   Suposiciones: requisitos estables en lo esencial; disponibilidad de BD y herramientas.
   Dependencias: servidor BD, repositorio Git, entorno Java 17 + Maven.

————————————————————————————————————3. Especificaciones Técnicas del Proyecto: Gestor de Administración de Academias (multi-academia)————————————————————————————————————

4. Arquitectura
   Arquitectura en capas (MVC):

Capa de Presentación (View)

- Thymeleaf + Bootstrap 5.
- JavaScript para validación cliente y mejoras UX.

Capa de Control (Controller)

- Controladores Spring MVC.
- Bean Validation en formularios.
- Controller Advice para errores centralizados.

Capa de Servicio (Service)

- Lógica de negocio en servicios (@Service).
- Gestión transaccional con @Transactional.
- Validaciones de negocio (incluida prevención de solapamientos en reservas).
- CustomUserDetailsService (UserDetailsService).

Capa de Persistencia (Repository)

- Spring Data JPA.
- Entidades JPA con relaciones.
- Consultas filtradas por academia (tenant scope).

Capa de Seguridad

- Spring Security: autenticación y autorización.
- RBAC + aislamiento por academia.

2. Tecnologías Seleccionadas
   Se mantienen las tecnologías ya definidas:

- Backend: Java 17, Spring Boot 3.4.1, Spring Web, Spring Data JPA, Spring Security 6, Spring Data Rest WebMVC.
- Frontend: Thymeleaf 3.x + Thymeleaf Extras Spring Security 6, Bootstrap 5, JS.
- BD: MySQL 8.0 o PostgreSQL 13+.
- Build: Maven.
- Testing: JUnit 5, Mockito, Spring Boot Test.

3. Estándares

Arquitectura REST (opcional)

- Endpoints RESTful siguiendo convenciones HTTP si se exponen APIs.

Seguridad

- BCryptPasswordEncoder.
- @EnableMethodSecurity y @PreAuthorize si aplica.
- CSRF habilitado en formularios.
- CORS mediante CorsConfig si es necesario.

Desarrollo

- Convenciones Java, SOLID, Clean Code.
- Cobertura mínima de pruebas 70%.

4. Modelo de Datos (actualizado — incluye cambios relacionales)

4.1. Entidades principales

Academia

- id (Long, PK)
- nombre (String)
- activa (Boolean)
- fechaAlta (LocalDateTime)
- (opcionales) nifCif, emailContacto, telefono, direccion

Usuario

- id (Long, PK)
- username (String, único)
- password (String, cifrado)
- email (String, único)
- nombre (String)
- apellidos (String)
- rol (Enum: ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO)
- activo (Boolean)
- academia (ManyToOne → Academia, nullable únicamente si rol=ADMIN)

Profesor

- id (Long, PK)
- usuario (OneToOne → Usuario)
- academia (ManyToOne → Academia) (coherente con el usuario)
- especialidad (String)
- biografia (String, opcional)

Alumno

- id (Long, PK)
- usuario (OneToOne → Usuario)
- academia (ManyToOne → Academia)
- fechaRegistro (LocalDate)
- estado (Enum o Boolean, opcional)

Aula

- id (Long, PK)
- academia (ManyToOne → Academia)
- nombre (String)
- capacidad (Integer)
- activa (Boolean)
- (opcional) recursos (String)

ReservaAula

- id (Long, PK)
- academia (ManyToOne → Academia)
- aula (ManyToOne → Aula)
- fechaInicio (LocalDateTime)
- fechaFin (LocalDateTime)
- estado (Enum: ACTIVA, CANCELADA)
- descripcion (String, opcional)
- creadaPor (ManyToOne → Usuario)
- canceladaPor (ManyToOne → Usuario, opcional)
- fechaCreacion (LocalDateTime)
- fechaCancelacion (LocalDateTime, opcional)

Opcional (si mantienes parte académica tipo cursos/matrículas; recomendado en academias si procede)Curso

- id (Long, PK)
- academia (ManyToOne → Academia)
- nombre (String)
- descripcion (String)
- duracion (Integer, horas)
- precio (BigDecimal)
- fechaInicio (LocalDate)
- fechaFin (LocalDate)
- categoria (String)
- profesor (ManyToOne → Profesor)

Matricula (sustituye ManyToMany simple por entidad con atributos)

- id (Long, PK)
- academia (ManyToOne → Academia)
- alumno (ManyToOne → Alumno)
- curso (ManyToOne → Curso)
- fechaMatriculacion (LocalDateTime)
- estado (Enum: ACTIVA, COMPLETADA, CANCELADA)

4.2. Relaciones (resumen)

- Academia 1—N Usuario (salvo ADMIN global).
- Usuario 1—1 Profesor (solo si rol=PROFESOR).
- Usuario 1—1 Alumno (solo si rol=ALUMNO).
- Academia 1—N Aula.
- Aula 1—N ReservaAula.
- Usuario 1—N ReservaAula (creadaPor).
- (Opcional académico) Academia 1—N Curso; Profesor 1—N Curso; Alumno N—N Curso mediante Matricula (tabla intermedia con atributos).

4.3. Restricciones e índices recomendados

- Índices por academia:

  - Usuario(academia_id, rol)
  - Alumno(academia_id)
  - Profesor(academia_id)
  - Aula(academia_id, activa)
  - ReservaAula(academia_id, aula_id, fechaInicio, fechaFin, estado)
- Validación anti-solapamiento (a nivel de servicio, dentro de transacción):

  - Antes de crear/modificar, verificar que no existan reservas ACTIVA para esa aula dentro de un rango que solape (fechaInicio < nuevaFechaFin && fechaFin > nuevaFechaInicio).
- Integridad:

  - Todas las entidades de negocio incluyen academia_id para garantizar aislamiento.
- Unicidad:

  - username único global.
  - email único global (o por academia si se define esa necesidad; si se opta por unicidad por academia, ajustar índice compuesto).

5. Interfaz de Usuario (actualizada)

Diseño Responsivo

- Móvil (< 768px), tablet (768–1024px), escritorio (> 1024px).
- Menú adaptativo según rol y academia.

Vistas Principales

- Públicas: login (y, si aplica, recuperación de contraseña).
- ADMIN (plataforma):

  - Dashboard y gestión de academias.
- PROPIETARIO (academia):

  - Dashboard de negocio (KPIs y agregados).
- SECRETARIA (academia):

  - Gestión de alumnos (alta/baja, listados).
  - Gestión de reservas de aulas (crear/cancelar/listar).
- PROFESOR (academia):

  - Mi información (perfil, vistas asociadas).
  - Reservas asociadas (si aplica).
- ALUMNO (academia):

  - Mi información (perfil, estado académico).
  - (Opcional) cursos/matrículas/horarios.

Formularios

- Validación cliente (JS) + servidor (Bean Validation/lógica de servicio).
- Mensajes de error claros en español.
- Feedback visual de éxito/error.

6. Seguridad (actualizada a multi-academia)

Autenticación

- Login form-based con Spring Security.
- Login: /login, logout: /logout, redirección: /default.
- CustomUserDetailsService para carga de usuarios.

Autorización

- Roles: ROLE_ADMIN, ROLE_PROPIETARIO, ROLE_SECRETARIA, ROLE_PROFESOR, ROLE_ALUMNO.
- Control de acceso a URLs (ejemplo):

  - /admin/** → hasRole('ADMIN')
  - /propietario/** → hasRole('PROPIETARIO')
  - /secretaria/** → hasRole('SECRETARIA')
  - /profesor/** → hasRole('PROFESOR')
  - /alumno/** → hasRole('ALUMNO')

Aislamiento por academia (tenant scope)

- Para cualquier operación no ADMIN: todas las consultas/acciones deben filtrarse por academia del usuario autenticado.
- Validación adicional en servicios: cualquier recurso solicitado por ID debe pertenecer a la academia del usuario (evitar IDOR).
- El ADMIN no está ligado a academia, y opera únicamente sobre recursos globales (Academias) y, si se decide, soporte técnico con acceso explícito (solo si se implementa y se audita).

Protección de Datos

- BCrypt.
- Prevención SQL Injection vía JPA + validaciones.
- XSS: escapado en Thymeleaf.
- CSRF habilitado en formularios.

Manejo de Excepciones

- WebExceptionHandler (@ControllerAdvice) para MVC.
- GlobalExceptionHandler para APIs REST (si existen).
- Páginas de error en templates/error (400, 403, 404, 409, 500).

7. Escalabilidad y Rendimiento

- Paginación con Pageable.
- Índices por academia y fechas.
- Evitar N+1 con fetch joins cuando proceda.
- Pool HikariCP (por defecto).
- Caché opcional para catálogos o dashboards si se necesita.

8. Indicadores de Rendimiento (KPIs)

- Tiempo de respuesta páginas < 2 s.
- CRUD < 500 ms.
- Disponibilidad > 99%.
- (Negocio) ocupación de aulas, reservas por periodo, alumnos activos, altas/bajas.

————————————————————————————————————4. Manual de Desarrollo: Gestor de Administración de Academias (multi-academia)————————————————————————————————————

5. Objetivo del Manual
   Establecer guías y procedimientos estandarizados para desarrollo con Spring Boot, garantizando consistencia, colaboración y calidad.
6. Procedimientos

2.1. Configuración del EntornoRequisitos:

- JDK 17
- Maven 3.x
- IDE (IntelliJ/Eclipse/STS)
- MySQL 8.0 o PostgreSQL 13+
- Git

Pasos:

- Clonar repositorio.
- Configurar application.properties/yml (BD, credenciales, perfiles).
- Inicializar BD (migraciones o scripts).
- Compilar: mvn clean install.
- Ejecutar: mvn spring-boot:run.

2.2. Creación de RamasConvención: tipo/descripcion-breve

- feature/gestion-academias
- feature/reservas-aulas
- feature/panel-propietario
- bugfix/validacion-solapamientos

Flujo:

- Crear rama desde develop.
- Commits frecuentes.
- Pruebas asociadas.
- PR con descripción y checklist.

2.3. Revisión de Código

- PR con alcance y pruebas.
- Revisión por al menos un revisor.
- Verificar seguridad (roles + tenant scope), rendimiento y estilo.

3. Estándares de Codificación

3.1. Nombres

- Clases: PascalCase
- Métodos/variables: camelCase
- Paquetes: lowercase

3.2. Estructura de Paquetes (ejemplo)
es.fempa.acd.academymanager
├── model
├── repository
├── service
├── controller
└── config

3.3. Anotaciones

- @Entity, @Table, @Id, @GeneratedValue, @Column
- @ManyToOne, @OneToOne, @OneToMany, @JoinColumn
- @Transactional en servicios
- @PreAuthorize si se aplica seguridad a nivel método

3.4. Estilo

- Indentación 4 espacios
- Máx. 120 caracteres
- Javadoc en públicos
- DRY, SOLID, Clean Code

4. Uso de Git

- main: estable
- develop: integración
- feature/_, bugfix/_, hotfix/*

Mensajes de commit: Conventional Commits (feat, fix, docs, refactor, test, chore).

5. Testing

- Unit tests con JUnit 5 + Mockito.
- Integración con Spring Boot Test + MockMvc.
- Pruebas específicas:

  - Aislamiento por academia (no acceso cruzado).
  - Acceso por rol.
  - Reservas: solapamientos, cancelaciones, rangos inválidos.

6. Resolución de Conflictos

- Pull frecuente desde develop.
- Resolver localmente.
- Ejecutar suite de tests antes de merge.

7. Gestión de Versiones

- Versionado semántico MAJOR.MINOR.PATCH.
- Tags y releases en GitHub.

8. Indicadores de Calidad

- Cobertura > 70%.
- Sin vulnerabilidades críticas.
- PR revisados.
- Documentación actualizada.

————————————————————————————————————5. Registro de Cambios (Changelog): Gestor de Administración de Academias————————————————————————————————————

6. Propósito
   Documentar cambios para trazabilidad, control de versiones y comunicación.
7. Formato
   Cada entrada incluye:

- Fecha (YYYY-MM-DD)
- Versión (MAJOR.MINOR.PATCH)
- Cambios Realizados
- Autor

3. Ejemplo de Registro

Versión 0.1.0

- Fecha: 2026-01-20
- Cambios:

  - Configuración inicial Spring Boot + Maven.
  - Configuración Spring Data JPA + MySQL.
  - Configuración básica Spring Security.
  - Estructura de paquetes base.
- Autor: Equipo Backend.

Versión 0.2.0

- Fecha: 2026-02-03
- Cambios:

  - Implementación de entidades multi-academia: Academia, Usuario (con rol y academia).
  - CRUD de academias (ADMIN).
  - Repositorios JPA y servicios base.
- Autor: Backend.

Versión 0.3.0

- Fecha: 2026-02-17
- Cambios:

  - Implementación Aula y ReservaAula.
  - Validación anti-solapamiento en servicio de reservas.
  - Pruebas unitarias del módulo de reservas.
- Autor: Backend / QA.

Versión 0.4.0

- Fecha: 2026-03-03
- Cambios:

  - Seguridad completa por roles (ADMIN/PROPIETARIO/SECRETARIA/PROFESOR/ALUMNO).
  - Aislamiento por academia (tenant scope) en servicios y repositorios.
  - Manejo de excepciones centralizado y páginas de error.
- Autor: Seguridad / Backend.

Versión 0.5.0

- Fecha: 2026-03-17
- Cambios:

  - Vistas Thymeleaf: login y navegación por rol.
  - Panel ADMIN: gestión de academias.
  - Panel SECRETARIA: alumnos + reservas.
- Autor: Full Stack.

Versión 1.0.0

- Fecha: 2026-04-30
- Cambios:

  - Pruebas de integración finalizadas.
  - Verificación de aislamiento multi-academia y permisos por rol.
  - Optimización de consultas e índices.
  - Documentación técnica completada.
- Autor: Equipo completo.

4. Lineamientos

- Actualizar changelog tras merges relevantes.
- Orden cronológico inverso (más reciente arriba).
- Categorías: Added, Changed, Deprecated, Removed, Fixed, Security.
