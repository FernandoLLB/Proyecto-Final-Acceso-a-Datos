Documentación mínima

Para asegurar una buena organización y especificación del proyecto, el equipo debería considerar al menos la siguiente documentación:

- **Documento de Requisitos:** Describe las funcionalidades esperadas, requisitos técnicos y objetivos del proyecto.
- **Plan de Proyecto:** Incluye el cronograma, hitos importantes y asignación de tareas.
- **Especificaciones Técnicas:** Detalles sobre la arquitectura, tecnologías y herramientas que se utilizarán.
- **Manual de Desarrollo:** Procedimientos para la colaboración, estándares de codificación y uso de herramientas como Git.
- **Registro de Cambios (Changelog):** Documenta las modificaciones realizadas a lo largo del proyecto.

Estos documentos ayudan a mantener la claridad y la alineación del equipo durante el desarrollo.

# Ejemplos de plantillas mínimas a rellenar para cada uno de los documentos

- **Documento de Requisitos:**
  - **Título del Proyecto:**
  - **Introducción:**
  - **Objetivos:**
  - **Requisitos Funcionales:**
  - **Requisitos No Funcionales:**
  - **Restricciones:**
- **Plan de Proyecto:**
  - **Título del Proyecto:**
  - **Cronograma:**
  - **Hitos:**
  - **Recursos:**
  - **Roles y Responsabilidades:**
- **Especificaciones Técnicas:**
  - **Arquitectura:**
  - **Tecnologías:**
  - **Estándares:**
  - **Interfaz:**
  - **Seguridad:**
- **Manual de Desarrollo:**
  - **Procedimientos:**
  - **Estándares de Codificación:**
  - **Uso de Git:**
  - **Resolución de Conflictos:**
- **Registro de Cambios (Changelog):**
  - **Fecha:**
  - **Versión:**
  - **Cambios Realizados:**
  - **Autor:**

Estos puntos son una guía y deben personalizarse según las necesidades del proyecto.

# Ejemplos de cada documento para un proyecto de una Plataforma de Cursos Online

## 1\. Documento de Requisitos del Proyecto: Plataforma de Cursos Online

### 1\. Título del Proyecto

**Plataforma de Cursos Online**

### 2\. Introducción

Este proyecto tiene como objetivo el desarrollo de una plataforma educativa en línea que permita la administración integral de cursos, estudiantes y profesores. La aplicación facilitará la gestión académica mediante funcionalidades para crear y administrar cursos, matricular estudiantes, asignar profesores y gestionar el contenido educativo. La plataforma está diseñada para instituciones educativas y centros de formación que buscan digitalizar su oferta académica en un entorno seguro y eficiente.

### 3\. Objetivos del Proyecto

#### 3.1. Objetivo General

Desarrollar una plataforma robusta y escalable que permita la gestión completa de cursos online, facilitando la administración de estudiantes, profesores y contenido educativo de manera eficiente y segura.

#### 3.2. Objetivos Específicos

- Diseñar una interfaz intuitiva y accesible para administradores, profesores y estudiantes.
- Implementar un sistema de gestión de usuarios con roles diferenciados (Administrador, Profesor, Estudiante).
- Permitir la creación y administración de cursos con asignación de profesores responsables.
- Facilitar la matriculación de estudiantes en múltiples cursos.
- Implementar validaciones robustas y manejo de excepciones mediante Bean Validation y Controller Advice.
- Garantizar la seguridad mediante Spring Security con autenticación y autorización basada en roles.

### 4\. Requisitos Funcionales

#### 4.1. Gestión de Usuarios

- Registro de usuarios con validación de datos (email, contraseña segura).
- Autenticación mediante usuario y contraseña.
- Asignación de roles: ADMIN, PROFESOR, ESTUDIANTE.
- Actualización y eliminación de perfiles de usuario.

#### 4.2. Gestión de Cursos

- Creación de cursos con información detallada (nombre, descripción, duración, precio, fecha de inicio).
- Asignación de un profesor responsable por curso (relación ManyToOne).
- Actualización y eliminación de cursos.
- Listado de cursos con filtros por categoría, profesor o disponibilidad.

#### 4.3. Gestión de Matriculaciones

- Matriculación de estudiantes en cursos (relación ManyToMany).
- Desinscripción de estudiantes de cursos.
- Consulta de cursos matriculados por estudiante.
- Consulta de estudiantes matriculados por curso.

#### 4.4. Panel de Control

- Dashboard para administradores con estadísticas de cursos, estudiantes y profesores.
- Dashboard para profesores con sus cursos asignados y estudiantes matriculados.
- Dashboard para estudiantes con sus cursos matriculados y progreso.

#### 4.5. Gestión de Profesores

- Listado de profesores registrados en la plataforma.
- Asignación y reasignación de profesores a cursos.
- Consulta de cursos impartidos por cada profesor.

### 5\. Requisitos No Funcionales

#### 5.1. Seguridad

- Autenticación mediante Spring Security con contraseñas cifradas (BCrypt).
- Autorización basada en roles con control de acceso a funcionalidades específicas.
- Protección contra ataques comunes (SQL Injection, XSS, CSRF).
- Implementación de políticas de privacidad para proteger datos personales.

#### 5.2. Rendimiento

- Respuesta de las páginas en menos de 2 segundos bajo condiciones normales de carga.
- Escalabilidad para manejar hasta 5,000 usuarios concurrentes.
- Optimización de consultas a base de datos mediante JPA/Hibernate.

#### 5.3. Compatibilidad

- Diseño responsivo compatible con dispositivos móviles, tablets y ordenadores.
- Compatibilidad con navegadores principales: Chrome, Firefox, Safari y Edge.
- Backend desarrollado con Spring Boot 2.x/3.x y Java 11+.

#### 5.4. Usabilidad

- Interfaz intuitiva con navegación clara entre las diferentes secciones.
- Mensajes de validación claros y en español.
- Sistema de ayuda contextual para usuarios.

#### 5.5. Mantenibilidad

- Código modular siguiendo el patrón MVC (Model-View-Controller).
- Uso de Bean Validation para validación de datos.
- Implementación de Controller Advice para manejo centralizado de excepciones.
- Testing unitario e integración con JUnit y Mockito.

### 6\. Restricciones

- La aplicación debe desarrollarse utilizando Spring Boot como framework backend.
- El frontend utilizará Thymeleaf como motor de plantillas.
- La base de datos debe ser relacional (MySQL o PostgreSQL).
- El desarrollo inicial está limitado a un plazo de 4 meses.
- Uso obligatorio de Spring Security para la gestión de autenticación y autorización.

### 7\. Suposiciones Iniciales

- Los usuarios tendrán acceso a una conexión a Internet estable.
- Los administradores serán responsables de crear las cuentas iniciales de profesores.
- Los estudiantes podrán auto-registrarse en la plataforma.
- El contenido de los cursos será gestionado exclusivamente por profesores y administradores.

## 2\. Documento Plan de Proyecto: Plataforma de Cursos Online

### 1\. Título del Proyecto

**Plataforma de Cursos Online**

### 2\. Objetivo del Proyecto

Planificar y organizar el desarrollo de una plataforma educativa web, asegurando una ejecución eficiente con cumplimiento de plazos, calidad del código y funcionalidades completas según los requisitos establecidos.

### 3\. Cronograma del Proyecto

El desarrollo del proyecto se dividirá en cuatro fases principales:

#### **Fase 1: Análisis y Diseño (2 semanas):**

- Análisis detallado de requisitos funcionales y no funcionales.
- Diseño de la arquitectura del sistema y modelo de datos.
- Diseño de prototipos de interfaz de usuario.
- Definición de entidades JPA (Usuario, Curso, Estudiante, Profesor, Matriculación).

#### **Fase 2: Desarrollo Backend (6 semanas):**

- Configuración del proyecto Spring Boot con dependencias necesarias.
- Implementación de entidades JPA con relaciones (ManyToOne, ManyToMany).
- Desarrollo de repositorios JPA para acceso a datos.
- Implementación de servicios de negocio con validaciones.
- Configuración de Spring Security con roles y permisos.
- Implementación de Controller Advice para manejo de excepciones.
- Desarrollo de controladores REST/MVC.

#### **Fase 3: Desarrollo Frontend (4 semanas):**

- Implementación de plantillas Thymeleaf con Bootstrap.
- Creación de formularios con validación del lado cliente.
- Desarrollo de dashboards personalizados por rol.
- Implementación de vistas para CRUD de cursos, estudiantes y profesores.
- Integración completa frontend-backend.

#### **Fase 4: Pruebas y Despliegue (2 semanas):**

- Pruebas unitarias de servicios y repositorios (JUnit, Mockito).
- Pruebas de integración de controladores.
- Pruebas de seguridad y validaciones.
- Corrección de errores identificados.
- Despliegue en servidor de pruebas.
- Documentación final del proyecto.

### 4\. Hitos del Proyecto

- **Semana 2:** Finalización del diseño de arquitectura y modelo de datos.
- **Semana 4:** Implementación completa de entidades JPA y repositorios.
- **Semana 6:** Servicios de negocio y Spring Security configurados.
- **Semana 8:** Backend completado con todos los controladores.
- **Semana 10:** Frontend completado con integración funcional.
- **Semana 12:** Pruebas finalizadas y correcciones implementadas.
- **Semana 14:** Despliegue y documentación completada.

### 5\. Recursos del Proyecto

#### 5.1. Humanos

- **Desarrollador Full Stack:** Responsable de backend (Spring Boot) y frontend (Thymeleaf).
- **Tester/QA:** Encargado de pruebas unitarias, integración y validación funcional.
- **Coordinador Técnico:** Supervisa el avance, resuelve bloqueos técnicos.

#### 5.2. Tecnológicos

- **Lenguajes:** Java 17, HTML5, CSS3, JavaScript.
- **Frameworks:** Spring Boot 3.4.1, Spring Security 6, Spring Data JPA, Thymeleaf.
- **Dependencias Adicionales:** 
  - Thymeleaf Extras Spring Security 6
  - Spring Data Rest WebMVC
  - MySQL Connector J (runtime)
- **Base de Datos:** MySQL 8.0.
- **Herramientas:** Maven 3.x, Git, GitHub, IntelliJ IDEA/Eclipse/Spring Tool Suite.
- **Testing:** JUnit 5, Mockito, Spring Boot Test, Spring Security Test.
- **Control de Versiones:** Git con repositorio en GitHub.

#### 5.3. Infraestructura

- Servidor de desarrollo local con MySQL/PostgreSQL.
- Servidor de pruebas para despliegue (Heroku, AWS, Azure o similar).

### 6\. Roles y Responsabilidades

#### **Desarrollador Full Stack:**

- Implementar la arquitectura backend con Spring Boot.
- Configurar Spring Security con roles y permisos.
- Desarrollar entidades, repositorios, servicios y controladores.
- Implementar vistas Thymeleaf con validaciones.
- Realizar pruebas unitarias del código desarrollado.

#### **Tester/QA:**

- Diseñar y ejecutar casos de prueba funcionales.
- Realizar pruebas de seguridad y validación de accesos.
- Documentar bugs y verificar correcciones.
- Validar el cumplimiento de requisitos funcionales.

#### **Coordinador Técnico:**

- Supervisar el progreso del proyecto según cronograma.
- Resolver impedimentos técnicos y arquitectónicos.
- Revisar código y aprobar pull requests.
- Asegurar la calidad del código y cumplimiento de estándares.

### 7\. Indicadores de Éxito

- Completar todas las funcionalidades definidas en requisitos funcionales.
- Lograr cobertura de pruebas unitarias > 70%.
- Sistema funcional con autenticación y autorización operativa.
- Cero vulnerabilidades críticas de seguridad.
- Documentación completa y actualizada.

### 8\. Suposiciones y Dependencias

#### **Suposiciones:**

- El equipo tendrá acceso continuo a herramientas y recursos de desarrollo.
- Los requisitos no sufrirán cambios significativos durante el desarrollo.
- La base de datos estará disponible desde el inicio del desarrollo.

#### **Dependencias:**

- Disponibilidad de servidor MySQL/PostgreSQL para desarrollo.
- Acceso a repositorio Git para control de versiones.
- Entorno de desarrollo configurado con Java 11+ y Maven.

## 3\. Especificaciones Técnicas del Proyecto: Plataforma de Cursos Online

### 1\. Arquitectura

La plataforma seguirá una arquitectura en capas basada en el patrón MVC (Model-View-Controller):

#### **Capa de Presentación (View):**

- Interfaz web desarrollada con Thymeleaf como motor de plantillas.
- Bootstrap 5 para diseño responsivo y componentes visuales.
- JavaScript para validaciones y mejoras de usabilidad del lado cliente.

#### **Capa de Control (Controller):**

- Controladores Spring MVC para gestionar peticiones HTTP.
- Manejo de formularios con Bean Validation.
- Controller Advice para manejo centralizado de excepciones.

#### **Capa de Servicio (Service):**

- Lógica de negocio implementada en servicios Spring (@Service).
- Incluye `CustomUserDetailsService` que implementa `UserDetailsService` para Spring Security.
- Validaciones de negocio (lanzamiento de excepciones como `IllegalStateException`, `UsernameNotFoundException`).
- Gestión de transacciones declarativa con @Transactional.

#### **Capa de Persistencia (Repository):**

- Repositorios Spring Data JPA para acceso a datos.
- Entidades JPA con anotaciones Hibernate.
- Relaciones entre entidades: ManyToOne (Curso-Profesor), ManyToMany (Curso-Estudiante).

#### **Capa de Seguridad:**

- Spring Security para autenticación y autorización.
- Control de acceso basado en roles (RBAC).
- Cifrado de contraseñas con BCrypt.

### 2\. Tecnologías Seleccionadas

#### **Backend:**

- **Lenguaje:** Java 17.
- **Framework Principal:** Spring Boot 3.4.1.
- **Módulos Spring:**
  - Spring Boot Starter Web: Controladores web y manejo de peticiones.
  - Spring Boot Starter Data JPA: Persistencia y acceso a datos.
  - Spring Boot Starter Security: Autenticación y autorización.
  - Spring Data Rest WebMVC: Para operaciones REST adicionales.
- **ORM:** Hibernate (incluido en Spring Data JPA).

#### **Frontend:**

- **Motor de Plantillas:** Thymeleaf 3.x con Thymeleaf Extras Spring Security 6.
- **Framework CSS:** Bootstrap 5.
- **JavaScript:** Vanilla JS para validaciones y mejoras UX.

#### **Base de Datos:**

- **Motor:** MySQL 8.0 o PostgreSQL 13+.
- **Gestión:** Scripts SQL para inicialización de datos.

#### **Herramientas de Desarrollo:**

- **Gestor de Dependencias:** Maven 3.x.
- **Control de Versiones:** Git con repositorio en GitHub.
- **IDE:** IntelliJ IDEA, Eclipse o Spring Tool Suite.

#### **Testing:**

- **JUnit 5:** Framework de pruebas unitarias.
- **Mockito:** Mocking de dependencias.
- **Spring Boot Test:** Pruebas de integración.

### 3\. Estándares

#### **Arquitectura REST (opcional para APIs):**

- Endpoints RESTful siguiendo convenciones HTTP.
- Verbos HTTP: GET (consulta), POST (creación), PUT (actualización), DELETE (eliminación).
- Códigos de respuesta HTTP apropiados (200, 201, 400, 404, 500).

#### **Seguridad:**

- Contraseñas cifradas con BCryptPasswordEncoder.
- Configuración con `@EnableMethodSecurity` para usar anotaciones como `@PreAuthorize`.
- CSRF puede estar deshabilitado según necesidades (común en APIs REST).
- CORS configurado mediante clase `CorsConfig` si es necesario.
- Validación de datos en servidor (las validaciones se manejan en servicios y controladores).

#### **Desarrollo:**

- Convenciones de código Java (Oracle Code Conventions).
- Uso de anotaciones Spring (@Controller, @Service, @Repository, @Entity).
- Principios SOLID y Clean Code.
- Cobertura de pruebas mínima del 70%.

### 4\. Modelo de Datos

#### **Entidades Principales:**

**Usuario:**
- id (Long, PK)
- username (String, único)
- password (String, cifrado)
- email (String, único)
- nombre (String)
- apellidos (String)
- rol (Enum: ADMIN, PROFESOR, ESTUDIANTE)
- activo (Boolean)

**Curso:**
- id (Long, PK)
- nombre (String)
- descripcion (String)
- duracion (Integer, horas)
- precio (BigDecimal)
- fechaInicio (LocalDate)
- fechaFin (LocalDate)
- categoria (String)
- profesor (ManyToOne → Profesor)
- estudiantes (ManyToMany → Estudiante)

**Profesor:**
- id (Long, PK)
- usuario (OneToOne → Usuario)
- especialidad (String)
- biografia (String)
- cursos (OneToMany → Curso)

**Estudiante:**
- id (Long, PK)
- usuario (OneToOne → Usuario)
- fechaRegistro (LocalDate)
- cursos (ManyToMany → Curso)

**Matriculacion (Tabla de unión para ManyToMany con atributos adicionales):**
- id (Long, PK)
- estudiante (ManyToOne → Estudiante)
- curso (ManyToOne → Curso)
- fechaMatriculacion (LocalDateTime)
- estado (Enum: ACTIVA, COMPLETADA, CANCELADA)

### 5\. Interfaz de Usuario

#### **Diseño Responsivo:**

- Adaptación a móviles (< 768px), tablets (768px-1024px) y escritorio (> 1024px).
- Navigation bar con menú adaptativo según rol de usuario.

#### **Vistas Principales:**

- **Públicas:** Login, registro, catálogo de cursos.
- **Administrador:** Dashboard, gestión de usuarios, gestión de cursos, asignación de profesores.
- **Profesor:** Dashboard, mis cursos, lista de estudiantes matriculados, edición de contenido del curso.
- **Estudiante:** Dashboard, mis cursos, catálogo de cursos disponibles, matriculación.

#### **Formularios:**

- Validación del lado cliente con JavaScript.
- Validación del lado servidor con Bean Validation.
- Mensajes de error claros y en español.
- Feedback visual de operaciones exitosas/fallidas.

### 6\. Seguridad

#### **Autenticación:**

- Login form-based con Spring Security.
- Página de login personalizada: `/login`.
- Redirección tras login exitoso: `/default`.
- URL de logout: `/logout`.
- `CustomUserDetailsService` implementa `UserDetailsService` para cargar usuarios.

#### **Autorización:**

- Roles definidos como Enum: ROLE_ADMIN, ROLE_PROFESOR, ROLE_ESTUDIANTE.
- Control de acceso a URLs mediante `SecurityFilterChain`:
  - `/admin/**` → hasRole('ADMIN')
  - `/profesor/**` → hasRole('PROFESOR')  
  - `/estudiante/**` → hasRole('ESTUDIANTE')
- Anotación `@PreAuthorize` en métodos de controladores (habilitada con `@EnableMethodSecurity`).
- `DaoAuthenticationProvider` con `PasswordEncoder` (BCrypt).

#### **Protección de Datos:**

- Contraseñas cifradas con BCryptPasswordEncoder.
- Validación de entrada para prevenir SQL Injection.
- Escapado de salida en Thymeleaf para prevenir XSS.
- Protección CSRF habilitada en formularios.

#### **Manejo de Excepciones:**

- **WebExceptionHandler:** `@ControllerAdvice` específico para controladores web que maneja:
  - `MethodArgumentNotValidException` → error/400
  - `UsernameNotFoundException` → error/404
  - `IllegalStateException` → error/409
  - `AccessDeniedException` → error/403
  - `NoResourceFoundException` → error/404
  - `Exception` genérica → error/500
- **GlobalExceptionHandler:** Para APIs REST que retorna `ResponseEntity`.
- Páginas de error personalizadas en `templates/error/` (400.html, 403.html, 404.html, 409.html, 500.html).

### 7\. Escalabilidad y Rendimiento

#### **Optimización de Consultas:**

- Uso de JPQL con fetch joins para evitar N+1 queries.
- Paginación en listados con Pageable de Spring Data.
- Índices en columnas de búsqueda frecuente.

#### **Caching (opcional):**

- Spring Cache para reducir acceso a base de datos.
- Cache de consultas frecuentes (listado de cursos, categorías).

#### **Pool de Conexiones:**

- HikariCP como pool de conexiones (default en Spring Boot).
- Configuración optimizada según carga esperada.

### 8\. Indicadores de Rendimiento (KPIs)

- Tiempo de respuesta de páginas < 2 segundos.
- Tiempo de respuesta de operaciones CRUD < 500 ms.
- Soporte para 5,000 usuarios concurrentes.
- Disponibilidad del sistema > 99%.

## 4\. Manual de Desarrollo: Plataforma de Cursos Online

### 1\. Objetivo del Manual de Desarrollo

Establecer guías claras y procedimientos estandarizados para el desarrollo de la Plataforma de Cursos Online, asegurando la calidad del código, colaboración eficiente y consistencia en la implementación utilizando Spring Boot y mejores prácticas de Java.

### 2\. Procedimientos

#### **2.1. Configuración del Entorno:**

- **Requisitos:**
  - JDK 11 o superior instalado y configurado.
  - Maven 3.6+ para gestión de dependencias.
  - IDE (IntelliJ IDEA, Eclipse o Spring Tool Suite).
  - MySQL 8.0 o PostgreSQL 13+ instalado y ejecutándose.
  - Git instalado para control de versiones.

- **Pasos Iniciales:**
  - Clonar el repositorio desde GitHub.
  - Importar el proyecto Maven en el IDE.
  - Configurar application.properties con credenciales de base de datos.
  - Ejecutar scripts de inicialización de base de datos.
  - Verificar que el proyecto compila correctamente: `mvn clean install`.

#### **2.2. Creación de Ramas:**

- **Convención de Nombres:** tipo/descripcion-breve
  - `feature/crear-entidad-curso`
  - `feature/implementar-servicio-matriculacion`
  - `bugfix/corregir-validacion-email`
  - `hotfix/seguridad-sesiones`

- **Flujo de Trabajo Git:**
  - Crear rama desde `main` o `develop`.
  - Desarrollar la funcionalidad con commits frecuentes.
  - Escribir pruebas unitarias para el código desarrollado.
  - Crear Pull Request cuando la funcionalidad esté completa y probada.

#### **2.3. Revisión de Código:**

- **Pull Requests (PR):**
  - Descripción detallada del cambio realizado.
  - Referencia a issues o tareas relacionadas.
  - Capturas de pantalla si hay cambios visuales.
  - Confirmación de que las pruebas pasan exitosamente.

- **Proceso de Revisión:**
  - Al menos un revisor debe aprobar el PR.
  - Verificar cumplimiento de estándares de código.
  - Validar que las pruebas cubren los casos principales.
  - Comprobar que no se introducen vulnerabilidades de seguridad.

### 3\. Estándares de Codificación

#### **3.1. Convenciones de Nombres:**

- **Clases:** PascalCase - `CursoController`, `EstudianteService`, `ProfesorRepository`.
- **Interfaces:** PascalCase con 'I' opcional - `ICursoService` o `CursoService`.
- **Métodos y Variables:** camelCase - `obtenerCursosPorProfesor()`, `listaEstudiantes`.
- **Constantes:** UPPER_SNAKE_CASE - `MAX_ESTUDIANTES_POR_CURSO`, `TIMEOUT_SESION`.
- **Paquetes:** lowercase - `es.fempa.acd.cursosplatforma.controller`.

#### **3.2. Estructura de Paquetes:**

```
es.fempa.acd.cursosplatforma
├── model (entidades JPA)
├── repository (interfaces Spring Data JPA)
├── service (lógica de negocio y UserDetailsService)
├── controller (controladores MVC)
└── config (configuración: SecurityConfig, WebExceptionHandler, GlobalExceptionHandler, CorsConfig)
```

**Nota:** Siguiendo el patrón del proyecto base, no se separa la configuración de seguridad en un paquete aparte, sino que todo va en `config`.

#### **3.3. Anotaciones Spring:**

- **Entidades JPA:** `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`.
- **Relaciones:** `@ManyToOne`, `@OneToMany`, `@ManyToMany`, `@JoinColumn`, `@JoinTable`.
- **Validaciones:** Las validaciones se realizan principalmente en la capa de servicio mediante lógica de negocio. Se pueden usar anotaciones Bean Validation (`@NotNull`, `@NotBlank`, `@Email`, `@Size`) si se requiere, capturadas por `MethodArgumentNotValidException` en el `WebExceptionHandler`.
- **Componentes:** `@Controller`, `@Service`, `@Repository`, `@Component`, `@Configuration`.
- **Seguridad:** `@PreAuthorize`, `@EnableMethodSecurity`.
- **Transacciones:** `@Transactional` en métodos de servicio que modifican datos.

#### **3.4. Estilo de Código:**

- **Formato:**
  - Indentación: 4 espacios (no tabs).
  - Longitud máxima de línea: 120 caracteres.
  - Llaves de apertura en la misma línea.

- **Comentarios:**
  - Javadoc para clases y métodos públicos.
  - Comentarios inline para lógica compleja.
  - Evitar comentarios obvios.

- **Buenas Prácticas:**
  - Principios SOLID y Clean Code.
  - Métodos pequeños con responsabilidad única.
  - Evitar código duplicado (DRY - Don't Repeat Yourself).
  - Usar Optional<T> para valores que pueden ser null.
  - Manejo explícito de excepciones.

### 4\. Uso de Git

#### **4.1. Flujo de Trabajo GitHub:**

- **main:** Rama principal con código estable en producción.
- **develop:** Rama de integración para nuevas funcionalidades.
- **feature/*** Ramas para desarrollo de nuevas funcionalidades.
- **bugfix/*** Ramas para corrección de errores.
- **hotfix/*** Ramas para correcciones urgentes en producción.

#### **4.2. Mensajes de Commit:**

Seguir convención Conventional Commits:

```
<tipo>(<alcance>): <descripción breve>

[cuerpo opcional con más detalles]

[footer opcional con referencias]
```

**Tipos:**
- `feat`: Nueva funcionalidad.
- `fix`: Corrección de bug.
- `docs`: Cambios en documentación.
- `style`: Cambios de formato (espacios, punto y coma, etc.).
- `refactor`: Refactorización sin cambio funcional.
- `test`: Agregar o modificar pruebas.
- `chore`: Tareas de mantenimiento.

**Ejemplos:**
```
feat(curso): implementar creación de curso con validaciones
fix(seguridad): corregir vulnerabilidad en autenticación
docs(readme): actualizar instrucciones de instalación
test(estudiante): agregar pruebas unitarias para servicio
```

### 5\. Testing

#### **5.1. Pruebas Unitarias:**

- **Framework:** JUnit 5.
- **Mocking:** Mockito para simular dependencias.
- **Cobertura:** Mínimo 70% de cobertura de código.

**Ejemplo de Test de Servicio:**
```java
@ExtendWith(MockitoExtension.class)
class CursoServiceTest {
    @Mock
    private CursoRepository cursoRepository;
    
    @InjectMocks
    private CursoService cursoService;
    
    @Test
    void testObtenerCursoPorId() {
        // Given, When, Then
    }
}
```

#### **5.2. Pruebas de Integración:**

- **Framework:** Spring Boot Test.
- **Scope:** Controladores, repositorios con base de datos H2.

**Ejemplo:**
```java
@SpringBootTest
@AutoConfigureMockMvc
class CursoControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testListarCursos() throws Exception {
        // Test completo del endpoint
    }
}
```

### 6\. Resolución de Conflictos

#### **6.1. Antes de Merge:**

- Actualizar rama local: `git pull origin develop`.
- Resolver conflictos localmente antes de crear PR.
- Ejecutar pruebas para asegurar que todo funciona.

#### **6.2. Durante el Merge:**

- Usar herramientas de merge del IDE o comandos Git.
- Comunicar conflictos complejos al equipo.
- Documentar decisiones de resolución en el commit.

### 7\. Gestión de Versiones

#### **7.1. Versionado Semántico:**

- **MAJOR.MINOR.PATCH** (e.g., 1.2.3)
- **MAJOR:** Cambios incompatibles con versiones anteriores.
- **MINOR:** Nuevas funcionalidades compatibles.
- **PATCH:** Correcciones de bugs.

#### **7.2. Tags en Git:**

```bash
git tag -a v1.0.0 -m "Primera versión estable"
git push origin v1.0.0
```

### 8\. Indicadores de Calidad

- Cobertura de pruebas > 70%.
- Cero vulnerabilidades críticas (uso de herramientas como Snyk).
- Código revisado antes de merge.
- Documentación actualizada con cada cambio significativo.

## 5 .Registro de Cambios (Changelog): Plataforma de Cursos Online

### 1\. Propósito del Registro de Cambios

Documentar de manera detallada y estructurada todas las modificaciones realizadas en la Plataforma de Cursos Online, facilitando la trazabilidad de cambios, identificación de versiones y comunicación efectiva entre los miembros del equipo de desarrollo.

### 2\. Formato del Registro

#### **Encabezado:**

Cada entrada debe contener:

- **Fecha:** Fecha de implementación de los cambios (formato YYYY-MM-DD).
- **Versión:** Versionado semántico MAJOR.MINOR.PATCH.
- **Cambios Realizados:** Descripción detallada de las modificaciones.
- **Autor:** Nombre del desarrollador o equipo responsable.

### 3\. Ejemplo de Registro

#### **Versión 0.1.0**

- **Fecha:** 2026-01-20
- **Cambios Realizados:**
  - Configuración inicial del proyecto Spring Boot con Maven.
  - Configuración de Spring Data JPA y MySQL.
  - Configuración básica de Spring Security.
  - Creación de estructura de paquetes y arquitectura base.
- **Autor:** Equipo de Desarrollo Backend.

#### **Versión 0.2.0**

- **Fecha:** 2026-02-03
- **Cambios Realizados:**
  - Implementación de entidades JPA: Usuario, Profesor, Estudiante, Curso.
  - Definición de relaciones ManyToOne (Curso-Profesor) y ManyToMany (Curso-Estudiante).
  - Creación de repositorios Spring Data JPA.
  - Implementación de Bean Validation en entidades.
- **Autor:** Juan Pérez (Backend).

#### **Versión 0.3.0**

- **Fecha:** 2026-02-17
- **Cambios Realizados:**
  - Implementación de servicios de negocio para Curso, Estudiante y Profesor.
  - Desarrollo de lógica de matriculación de estudiantes en cursos.
  - Implementación de validaciones de negocio.
  - Creación de pruebas unitarias con JUnit y Mockito (cobertura 75%).
- **Autor:** María García (Backend).

#### **Versión 0.4.0**

- **Fecha:** 2026-03-03
- **Cambios Realizados:**
  - Configuración completa de Spring Security con roles (ADMIN, PROFESOR, ESTUDIANTE).
  - Implementación de autenticación basada en formulario.
  - Cifrado de contraseñas con BCrypt.
  - Control de acceso a URLs según roles.
- **Autor:** Carlos López (Seguridad).

#### **Versión 0.5.0**

- **Fecha:** 2026-03-17
- **Cambios Realizados:**
  - Desarrollo de controladores MVC para gestión de cursos.
  - Implementación de Controller Advice para manejo de excepciones.
  - Creación de páginas de error personalizadas (403, 404, 500).
  - Validación de formularios con Bean Validation.
- **Autor:** Ana Martínez (Backend).

#### **Versión 0.6.0**

- **Fecha:** 2026-03-31
- **Cambios Realizados:**
  - Implementación de vistas Thymeleaf con Bootstrap 5.
  - Creación de formularios para CRUD de cursos, estudiantes y profesores.
  - Desarrollo de dashboards personalizados por rol.
  - Implementación de navegación responsiva.
- **Autor:** Luis Fernández (Frontend).

#### **Versión 0.7.0**

- **Fecha:** 2026-04-14
- **Cambios Realizadas:**
  - Implementación completa de funcionalidad de matriculación.
  - Desarrollo de vistas para gestión de estudiantes matriculados.
  - Implementación de listados con paginación.
  - Integración completa frontend-backend.
- **Autor:** Juan Pérez (Full Stack).

#### **Versión 1.0.0**

- **Fecha:** 2026-04-30
- **Cambios Realizados:**
  - Finalización de pruebas de integración.
  - Corrección de bugs identificados en fase de testing.
  - Optimización de consultas a base de datos.
  - Documentación técnica completada.
  - Primera versión estable lista para producción.
- **Autor:** Equipo Completo de Desarrollo.

#### **Versión 1.0.1**

- **Fecha:** 2026-05-10
- **Cambios Realizados:**
  - Corrección de error en validación de fechas de cursos.
  - Mejora en mensajes de error de formularios.
  - Ajuste de responsividad en dispositivos móviles.
- **Autor:** Luis Fernández (Frontend).

#### **Versión 1.1.0**

- **Fecha:** 2026-05-25
- **Cambios Realizados:**
  - Implementación de búsqueda avanzada de cursos.
  - Adición de filtros por categoría y profesor.
  - Mejora en dashboard con gráficos estadísticos.
  - Implementación de exportación de listas a PDF.
- **Autor:** María García (Backend), Luis Fernández (Frontend).

### 4\. Lineamientos para el Registro de Cambios

#### **Versionado Semántico:**

- **MAJOR (X.0.0):** Cambios significativos, refactorización mayor, incompatibilidad con versiones anteriores.
- **MINOR (0.X.0):** Nuevas funcionalidades manteniendo compatibilidad.
- **PATCH (0.0.X):** Correcciones de bugs, mejoras menores, hotfixes.

#### **Categorías de Cambios:**

- **Added:** Nuevas funcionalidades.
- **Changed:** Cambios en funcionalidades existentes.
- **Deprecated:** Funcionalidades que se eliminarán en futuras versiones.
- **Removed:** Funcionalidades eliminadas.
- **Fixed:** Corrección de bugs.
- **Security:** Cambios relacionados con seguridad.

#### **Buenas Prácticas:**

- Actualizar el changelog inmediatamente después de cada merge significativo.
- Usar descripciones claras y técnicas pero comprensibles.
- Incluir referencias a issues o tickets relacionados.
- Mantener el registro en orden cronológico inverso (más reciente primero).
- Asignar crédito apropiado a los contribuidores.

### 5\. Herramientas Recomendadas

#### **Control de Versiones:**

- Git tags para marcar versiones: `git tag -a v1.0.0 -m "Primera versión estable"`.
- GitHub Releases para publicar versiones con notas de lanzamiento.

#### **Documentación:**

- Mantener archivo CHANGELOG.md en la raíz del proyecto.
- Usar formato Markdown para mejor legibilidad.

#### **Automatización:**

- Conventional Commits para generar changelog automáticamente.
- Herramientas como Release Drafter para automatizar notas de versión.
- Integration con CI/CD para actualizar versiones automáticamente.

### 6\. Ejemplo de Estructura del Archivo CHANGELOG.md

```markdown
# Changelog: Plataforma de Cursos Online

Todos los cambios notables en este proyecto serán documentados en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Versionado Semántico](https://semver.org/lang/es/).

## [1.1.0] - 2026-05-25

### Added
- Búsqueda avanzada de cursos con filtros múltiples.
- Filtros por categoría y profesor en listado de cursos.
- Gráficos estadísticos en dashboard de administrador.
- Exportación de listas de estudiantes y cursos a PDF.

### Changed
- Mejora en diseño de dashboard con mejor visualización de datos.

### Autor(es)
- María García (Backend)
- Luis Fernández (Frontend)

## [1.0.1] - 2026-05-10

### Fixed
- Corrección de validación de fechas en formulario de cursos.
- Ajuste de responsividad en vista de listado de cursos en móviles.
- Mejora en mensajes de error de validación de formularios.

### Autor(es)
- Luis Fernández (Frontend)

## [1.0.0] - 2026-04-30

### Added
- Primera versión estable de la plataforma.
- Sistema completo de gestión de cursos, estudiantes y profesores.
- Matriculación de estudiantes en cursos con relación ManyToMany.
- Asignación de profesores a cursos con relación ManyToOne.
- Autenticación y autorización con Spring Security.
- Dashboards personalizados por rol.

### Security
- Implementación de BCrypt para cifrado de contraseñas.
- Protección CSRF en todos los formularios.
- Control de acceso basado en roles.

### Autor(es)
- Equipo Completo de Desarrollo

## [0.7.0] - 2026-04-14

### Added
- Implementación completa de funcionalidad de matriculación.
- Vistas para gestión de estudiantes matriculados por curso.
- Paginación en listados de cursos y estudiantes.

### Autor(es)
- Juan Pérez (Full Stack)

## [0.6.0] - 2026-03-31

### Added
- Vistas Thymeleaf con Bootstrap 5.
- Formularios CRUD para cursos, estudiantes y profesores.
- Dashboards específicos por rol de usuario.
- Navegación responsiva con menú adaptativo.

### Autor(es)
- Luis Fernández (Frontend)

[Versiones anteriores omitidas por brevedad...]
```

### 7\. Integración con Flujo de Desarrollo

- **Pre-commit:** Validar formato de mensajes de commit.
- **PR Review:** Verificar que se actualice el changelog en PRs significativos.
- **Release:** Generar tag y actualizar changelog antes de cada release.
- **Post-release:** Comunicar cambios al equipo y stakeholders.

Estos ejemplos y lineamientos deben adaptarse según las necesidades específicas del proyecto y el equipo de desarrollo.