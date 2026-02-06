# Guía de Implementación - Modelo SaaS Multi-Propietario

## Estado Actual de la Implementación

Se ha completado la refactorización arquitectónica del sistema de gestión de academias para transformarlo en un modelo SaaS donde:

- **ADMIN**: Superadministrador del sistema (propietario del software)
- **PROPIETARIO**: Cliente que puede tener múltiples academias
- **SECRETARIA, PROFESOR, ALUMNO**: Siguen asociados a una academia específica

### ✅ Completado

1. **Modelo de Datos**
   - ✅ Entidad `Propietario` creada con relaciones correctas
   - ✅ Entidad `Academia` actualizada con campo `propietario_id`
   - ✅ Script de migración SQL (`V2__add_propietario_entity.sql`) creado

2. **Repositorios**
   - ✅ `PropietarioRepository` con métodos de consulta
   - ✅ `AcademiaRepository` actualizado con consultas por propietario

3. **Servicios**
   - ✅ `PropietarioService` con lógica de negocio completa
   - ✅ `AcademiaService` actualizado para trabajar con propietarios
   - ✅ `UsuarioService` con método `guardar()` añadido

4. **Controladores**
   - ✅ `AdminPropietarioController` para gestión de propietarios por ADMIN
   - ✅ `PropietarioController` actualizado para gestionar múltiples academias
   - ✅ `AcademiaController` actualizado con estadísticas de propietarios

5. **Internacionalización**
   - ✅ Claves i18n añadidas en `messages_es.properties`
   - ✅ Claves i18n añadidas en `messages_en.properties`

### 🔨 Pendiente de Implementación

#### 1. Vistas Thymeleaf (Alta Prioridad)

##### Para ADMIN - Gestión de Propietarios

**a) Lista de Propietarios** (`/src/main/resources/templates/admin/propietarios-lista.html`)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments :: head(#{owner.list})}"></head>
<body>
    <nav th:replace="~{fragments :: navbar('admin', ${#authentication.name})}"></nav>
    <aside th:replace="~{fragments :: sidebar-admin('propietarios')}"></aside>

    <main class="main-content fade-in">
        <div class="page-header">
            <h1 class="page-title">
                <i class="bi bi-people-fill"></i> 
                <span th:text="#{owner.list}">Lista de Propietarios</span>
            </h1>
            <a th:href="@{/admin/propietarios/nuevo}" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> 
                <span th:text="#{owner.new}">Nuevo Propietario</span>
            </a>
        </div>

        <!-- Mensajes de éxito/error -->
        <div th:if="${success}" class="alert alert-success">
            <i class="bi bi-check-circle"></i> <span th:text="${success}"></span>
        </div>
        <div th:if="${error}" class="alert alert-danger">
            <i class="bi bi-exclamation-triangle"></i> <span th:text="${error}"></span>
        </div>

        <!-- Tabla de propietarios -->
        <div class="card">
            <div class="card-body">
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th th:text="#{owner.business.name}">Razón Social</th>
                                <th th:text="#{owner.nif}">NIF/CIF</th>
                                <th th:text="#{owner.user.username}">Usuario</th>
                                <th th:text="#{owner.user.email}">Email</th>
                                <th th:text="#{owner.total.academies}">Academias</th>
                                <th th:text="#{owner.status}">Estado</th>
                                <th th:text="#{app.actions}">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr th:each="propietario : ${propietarios}">
                                <td th:text="${propietario.id}">1</td>
                                <td th:text="${propietario.razonSocial}">Mi Academia S.L.</td>
                                <td th:text="${propietario.nifCif}">B12345678</td>
                                <td th:text="${propietario.usuario.username}">usuario1</td>
                                <td th:text="${propietario.usuario.email}">email@example.com</td>
                                <td th:text="${propietario.academias.size()}">3</td>
                                <td>
                                    <span th:if="${propietario.activo}" class="badge bg-success">
                                        <span th:text="#{app.active}">Activo</span>
                                    </span>
                                    <span th:unless="${propietario.activo}" class="badge bg-danger">
                                        <span th:text="#{app.inactive}">Inactivo</span>
                                    </span>
                                </td>
                                <td>
                                    <a th:href="@{/admin/propietarios/{id}/editar(id=${propietario.id})}" 
                                       class="btn btn-warning btn-sm">
                                        <i class="bi bi-pencil"></i> <span th:text="#{app.edit}">Editar</span>
                                    </a>
                                    <a th:href="@{/admin/propietarios/{id}/detalle(id=${propietario.id})}" 
                                       class="btn btn-info btn-sm">
                                        <i class="bi bi-eye"></i> <span th:text="#{app.view}">Ver</span>
                                    </a>
                                    <form th:if="${propietario.activo}" 
                                          th:action="@{/admin/propietarios/{id}/desactivar(id=${propietario.id})}" 
                                          method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-danger btn-sm">
                                            <i class="bi bi-x-circle"></i> 
                                            <span th:text="#{owner.deactivate}">Desactivar</span>
                                        </button>
                                    </form>
                                    <form th:unless="${propietario.activo}" 
                                          th:action="@{/admin/propietarios/{id}/activar(id=${propietario.id})}" 
                                          method="post" style="display: inline;">
                                        <button type="submit" class="btn btn-success btn-sm">
                                            <i class="bi bi-check-circle"></i> 
                                            <span th:text="#{owner.activate}">Activar</span>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </main>

    <script th:src="@{/js/bootsprap/bootstrap.bundle.min.js}"></script>
</body>
</html>
```

**b) Formulario Nuevo Propietario** (`/src/main/resources/templates/admin/propietario-nuevo.html`)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments :: head(#{owner.new})}"></head>
<body>
    <nav th:replace="~{fragments :: navbar('admin', ${#authentication.name})}"></nav>
    <aside th:replace="~{fragments :: sidebar-admin('propietarios')}"></aside>

    <main class="main-content fade-in">
        <div class="page-header">
            <h1 class="page-title">
                <i class="bi bi-person-plus-fill"></i> 
                <span th:text="#{owner.new}">Nuevo Propietario</span>
            </h1>
        </div>

        <!-- Mensajes -->
        <div th:if="${error}" class="alert alert-danger">
            <i class="bi bi-exclamation-triangle"></i> <span th:text="${error}"></span>
        </div>

        <div class="card">
            <div class="card-body">
                <form th:action="@{/admin/propietarios/crear}" method="post">
                    
                    <!-- Datos de Usuario -->
                    <h5 class="mb-3"><i class="bi bi-person"></i> <span th:text="#{owner.user.data}">Datos de Usuario</span></h5>
                    
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="username" th:text="#{owner.user.username}">Usuario</label>
                                <input type="text" class="form-control" id="username" name="username" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="email" th:text="#{owner.user.email}">Email</label>
                                <input type="email" class="form-control" id="email" name="email" required>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="password" th:text="#{login.password}">Contraseña</label>
                                <input type="password" class="form-control" id="password" name="password" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="nombre" th:text="#{app.name.label}">Nombre</label>
                                <input type="text" class="form-control" id="nombre" name="nombre">
                            </div>
                        </div>
                    </div>

                    <hr class="my-4">

                    <!-- Datos de Propietario -->
                    <h5 class="mb-3"><i class="bi bi-building"></i> <span th:text="#{owner.detail}">Datos del Propietario</span></h5>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="razonSocial" th:text="#{owner.business.name}">Razón Social</label>
                                <input type="text" class="form-control" id="razonSocial" name="razonSocial" required>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="nifCif" th:text="#{owner.nif}">NIF/CIF</label>
                                <input type="text" class="form-control" id="nifCif" name="nifCif">
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="telefono" th:text="#{owner.phone}">Teléfono</label>
                                <input type="text" class="form-control" id="telefono" name="telefono">
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label for="direccion" th:text="#{owner.address}">Dirección</label>
                                <input type="text" class="form-control" id="direccion" name="direccion">
                            </div>
                        </div>
                    </div>

                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-check-circle"></i> <span th:text="#{app.save}">Guardar</span>
                        </button>
                        <a th:href="@{/admin/propietarios/lista}" class="btn btn-secondary">
                            <i class="bi bi-x-circle"></i> <span th:text="#{app.cancel}">Cancelar</span>
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <script th:src="@{/js/bootsprap/bootstrap.bundle.min.js}"></script>
</body>
</html>
```

**c) Formulario Editar Propietario** (`/src/main/resources/templates/admin/propietario-editar.html`)
Similar al formulario nuevo pero con campos prellenados usando `th:field` y sin el campo de contraseña obligatorio.

**d) Detalle de Propietario** (`/src/main/resources/templates/admin/propietario-detalle.html`)
Vista de solo lectura con la información del propietario y lista de sus academias.

##### Para PROPIETARIO - Gestión de sus Academias

**a) Dashboard de Propietario Mejorado** (`/src/main/resources/templates/propietario/dashboard.html`)
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments :: head(#{owner.dashboard})}"></head>
<body>
    <nav th:replace="~{fragments :: navbar('propietario', ${#authentication.name})}"></nav>
    <aside th:replace="~{fragments :: sidebar-propietario('dashboard')}"></aside>

    <main class="main-content fade-in">
        <div class="page-header">
            <h1 class="page-title">
                <i class="bi bi-speedometer2"></i> 
                <span th:text="#{owner.dashboard}">Dashboard de Propietario</span>
            </h1>
        </div>

        <!-- Selector de Academia -->
        <div class="card mb-4" th:if="${academias != null && !academias.isEmpty()}">
            <div class="card-header">
                <h5><i class="bi bi-building"></i> <span th:text="#{owner.select.academy}">Seleccionar Academia</span></h5>
            </div>
            <div class="card-body">
                <form th:action="@{/propietario/seleccionar-academia}" method="post">
                    <div class="row align-items-end">
                        <div class="col-md-8">
                            <label for="academiaId" th:text="#{academy.title}">Academia</label>
                            <select class="form-control" id="academiaId" name="academiaId" required>
                                <option value="" th:text="#{academy.select}">Seleccione una academia</option>
                                <option th:each="academia : ${academias}" 
                                        th:value="${academia.id}" 
                                        th:text="${academia.nombre}"
                                        th:selected="${academiaSeleccionada != null && academiaSeleccionada.id == academia.id}">
                                    Academia 1
                                </option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <button type="submit" class="btn btn-primary w-100">
                                <i class="bi bi-check-circle"></i> <span th:text="#{app.confirm}">Confirmar</span>
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>

        <!-- Mensaje si no hay academias -->
        <div class="alert alert-info" th:if="${totalAcademias == 0}">
            <i class="bi bi-info-circle"></i> 
            <span th:text="#{owner.no.academies.message}">Aún no tienes academias. Crea una nueva para comenzar.</span>
            <a th:href="@{/propietario/academias/nueva}" class="btn btn-primary btn-sm ms-3">
                <i class="bi bi-plus-circle"></i> <span th:text="#{owner.create.academy}">Crear Nueva Academia</span>
            </a>
        </div>

        <!-- Resumen de Academias -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-primary">
                        <i class="bi bi-building"></i>
                    </div>
                    <div class="stat-content">
                        <div class="stat-value" th:text="${totalAcademias}">0</div>
                        <div class="stat-label" th:text="#{owner.total.academies}">Total de Academias</div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-success">
                        <i class="bi bi-check-circle"></i>
                    </div>
                    <div class="stat-content">
                        <div class="stat-value" th:text="${academias.size()}">0</div>
                        <div class="stat-label" th:text="#{owner.active.academies}">Academias Activas</div>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                <div class="stat-card">
                    <div class="stat-icon bg-info">
                        <i class="bi bi-person-badge"></i>
                    </div>
                    <div class="stat-content">
                        <div class="stat-value" th:text="${propietario.razonSocial}">Propietario</div>
                        <div class="stat-label" th:text="#{owner.business.name}">Razón Social</div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Estadísticas de Academia Seleccionada -->
        <div th:if="${stats != null}">
            <div class="card mb-4">
                <div class="card-header">
                    <h5>
                        <i class="bi bi-bar-chart-fill"></i> 
                        <span th:text="#{owner.stats.overview}">Estadísticas</span>: 
                        <strong th:text="${stats.nombreAcademia}">Academia Seleccionada</strong>
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-value" th:text="${stats.totalAlumnos}">0</div>
                                <div class="stat-label" th:text="#{student.total}">Total Alumnos</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-value" th:text="${stats.totalProfesores}">0</div>
                                <div class="stat-label" th:text="#{teacher.total}">Total Profesores</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-value" th:text="${stats.totalAulas}">0</div>
                                <div class="stat-label" th:text="#{classroom.total}">Total Aulas</div>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="stat-card">
                                <div class="stat-value" th:text="${stats.reservasActivas}">0</div>
                                <div class="stat-label" th:text="#{reservation.active}">Reservas Activas</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Acceso rápido -->
        <div class="card">
            <div class="card-header">
                <h5><i class="bi bi-lightning-fill"></i> <span th:text="#{app.quick.access}">Acceso Rápido</span></h5>
            </div>
            <div class="card-body">
                <div class="row">
                    <div class="col-md-4">
                        <a th:href="@{/propietario/academias/lista}" class="quick-link-card">
                            <i class="bi bi-building"></i>
                            <span th:text="#{owner.my.academies}">Mis Academias</span>
                        </a>
                    </div>
                    <div class="col-md-4">
                        <a th:href="@{/propietario/academias/nueva}" class="quick-link-card">
                            <i class="bi bi-plus-circle"></i>
                            <span th:text="#{owner.create.academy}">Crear Nueva Academia</span>
                        </a>
                    </div>
                    <div class="col-md-4" th:if="${academiaSeleccionada != null}">
                        <a th:href="@{/propietario/academias/{id}/editar(id=${academiaSeleccionada.id})}" class="quick-link-card">
                            <i class="bi bi-pencil"></i>
                            <span th:text="#{academy.edit}">Editar Academia Actual</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>
    </main>

    <script th:src="@{/js/bootsprap/bootstrap.bundle.min.js}"></script>
</body>
</html>
```

**b) Lista de Academias del Propietario** (`/src/main/resources/templates/propietario/academias-lista.html`)

**c) Formulario Nueva Academia** (`/src/main/resources/templates/propietario/academia-nueva.html`)

**d) Formulario Editar Academia** (`/src/main/resources/templates/propietario/academia-editar.html`)

#### 2. Actualizar Fragmentos (fragments.html)

Necesitas actualizar el sidebar para:
- **Admin**: Añadir enlace a "Gestión de Propietarios"
- **Propietario**: Mantener/actualizar enlaces a gestión de academias

```html
<!-- En fragments.html, añadir en sidebar-admin -->
<li th:classappend="${activeMenu == 'propietarios'} ? 'active' : ''">
    <a th:href="@{/admin/propietarios/lista}">
        <i class="bi bi-people-fill"></i>
        <span th:text="#{owner.list}">Propietarios</span>
    </a>
</li>
```

#### 3. Ejecutar Migración de Base de Datos

**IMPORTANTE**: Antes de ejecutar la aplicación:

1. **Backup de la base de datos actual**
```bash
mysqldump -u root -p nombre_base_datos > backup_antes_migracion.sql
```

2. **Configurar Flyway** (opcional pero recomendado)
En `pom.xml`:
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

En `application.properties`:
```properties
# Flyway configuration
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

3. **O ejecutar manualmente el script SQL**
```bash
mysql -u root -p nombre_base_datos < src/main/resources/db/migration/V2__add_propietario_entity.sql
```

4. **Verificar la migración**
```sql
-- Verificar que la tabla propietario existe
DESCRIBE propietario;

-- Verificar que la columna propietario_id existe en academia
DESCRIBE academia;

-- Verificar que todas las academias tienen propietario asignado
SELECT COUNT(*) FROM academia WHERE propietario_id IS NULL;
```

#### 4. Actualizar SecurityConfig (si es necesario)

Verificar que las rutas estén correctamente configuradas:

```java
// En SecurityConfig.java
http
    .authorizeHttpRequests(auth -> auth
        .requestMatchers("/admin/propietarios/**").hasRole("ADMIN")
        .requestMatchers("/propietario/**").hasRole("PROPIETARIO")
        // ... resto de configuración
    )
```

#### 5. Tests

Crear tests para las nuevas funcionalidades:

**PropietarioServiceTest.java**
```java
@SpringBootTest
class PropietarioServiceTest {
    
    @Autowired
    private PropietarioService propietarioService;
    
    @Test
    void testCrearPropietario() {
        // Test de creación de propietario
    }
    
    @Test
    void testObtenerAcademiasPropietario() {
        // Test de obtención de academias
    }
}
```

**PropietarioControllerTest.java**
```java
@SpringBootTest
@AutoConfigureMockMvc
class PropietarioControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(roles = "PROPIETARIO")
    void testDashboardPropietario() throws Exception {
        // Test del dashboard
    }
}
```

#### 6. Documentación

Actualizar la documentación existente:

1. **DIAGRAMA_ER_Y_ANALISIS.md**: Actualizar diagrama ER con la nueva entidad Propietario
2. **documentacion.md**: Añadir sección sobre el modelo SaaS
3. Crear **MIGRACION_MODELO_SAAS.md**: Documentar los cambios y cómo revertirlos si es necesario

#### 7. Datos de Prueba

Crear datos de prueba para probar el sistema:

```sql
-- Crear usuario admin si no existe
INSERT INTO usuario (username, password, email, rol, activo, email_verificado) 
VALUES ('admin', '$2a$10$...', 'admin@sistema.com', 'ADMIN', 1, 1);

-- Crear propietario de prueba
INSERT INTO propietario (usuario_id, razon_social, nif_cif, activo, fecha_alta)
VALUES (1, 'Propietario de Prueba S.L.', 'B12345678', 1, NOW());

-- Crear academias de prueba para el propietario
INSERT INTO academia (nombre, activa, fecha_alta, propietario_id)
VALUES 
('Academia Central', 1, NOW(), 1),
('Academia Norte', 1, NOW(), 1),
('Academia Sur', 1, NOW(), 1);
```

## Orden de Implementación Recomendado

1. ✅ **Ejecutar migración de BD** (script SQL ya creado)
2. ✅ **Verificar errores de compilación** (ya resueltos excepto las vistas)
3. 🔨 **Crear vistas para Admin** (propietarios-lista, propietario-nuevo, propietario-editar)
4. 🔨 **Crear vistas para Propietario** (dashboard mejorado, academias-lista, academia-nueva, academia-editar)
5. 🔨 **Actualizar fragments.html** (sidebar con nuevos enlaces)
6. 🔨 **Probar flujo completo**: Admin crea propietario → Propietario crea academias → Propietario gestiona academias
7. 🔨 **Crear tests unitarios y de integración**
8. 🔨 **Actualizar documentación**

## Consideraciones Importantes

### Sesión y Academia Seleccionada

El sistema usa `HttpSession` para almacenar la academia seleccionada por el propietario:
- Clave: `academiaSeleccionadaId`
- Se establece al seleccionar una academia en el dashboard
- Se usa para filtrar datos en las diferentes vistas del propietario

### Validación de Acceso

Todos los métodos del `PropietarioController` verifican que la academia pertenezca al propietario antes de permitir operaciones:

```java
if (!academia.getPropietario().getId().equals(propietario.getId())) {
    throw new RuntimeException("No tienes acceso a esta academia");
}
```

### Migración de Datos Existentes

El script de migración crea un propietario inicial asociado al primer usuario ADMIN y le asigna todas las academias existentes. Si necesitas un comportamiento diferente, modifica el script SQL.

## Problemas Potenciales y Soluciones

### 1. Error: "Cannot resolve column 'propietario_id'"
**Solución**: Normal antes de ejecutar la migración. La columna se creará con el script SQL.

### 2. Usuario PROPIETARIO sin acceso a academias
**Solución**: Verificar que el propietario tenga academias asignadas y que `propietario_id` esté correctamente establecido.

### 3. Sesión pierde la academia seleccionada
**Solución**: Implementar un interceptor que verifique y restaure la sesión si es necesario.

### 4. Conflict con rol PROPIETARIO existente
**Solución**: Verificar que no haya usuarios con rol PROPIETARIO pero sin entrada en la tabla `propietario`. Ejecutar script de limpieza si es necesario.

## Próximos Pasos Avanzados (Futuro)

1. **Multi-tenancy completo**: Separar datos por propietario a nivel de BD (schema por propietario)
2. **Planes y facturación**: Sistema de suscripciones para propietarios
3. **Límites y cuotas**: Limitar número de academias, usuarios, etc. por plan
4. **Dashboard analítico**: Gráficos y reportes avanzados para propietarios
5. **White-label**: Permitir que cada propietario personalice su branding

## Contacto y Soporte

Para dudas o problemas durante la implementación, revisar:
- Logs de la aplicación en `target/logs/`
- Tests unitarios en `src/test/java/`
- Documentación adicional en `docs/`

---

**Fecha de creación**: 06/02/2026  
**Versión del sistema**: 2.0  
**Estado**: Implementación en progreso
