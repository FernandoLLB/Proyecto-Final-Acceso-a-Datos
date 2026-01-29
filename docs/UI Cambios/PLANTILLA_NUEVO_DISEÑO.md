# Plantilla para Actualizar Páginas HTML con Nuevo Diseño

## Estructura Base

Todas las páginas deben seguir esta estructura:

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{fragments :: head('Título de la Página')}"></head>
<body>
    <!-- Navbar común -->
    <nav th:replace="~{fragments :: navbar('ROL', ${#authentication.name})}"></nav>
    
    <!-- Sidebar específico del rol -->
    <aside th:replace="~{fragments :: sidebar-ROL('seccion-activa')}"></aside>

    <!-- Contenido principal -->
    <main class="main-content fade-in">
        <!-- Tu contenido aquí -->
    </main>

    <!-- Scripts -->
    <div th:replace="~{fragments :: scripts}"></div>
</body>
</html>
```

## Roles Disponibles para Navbar y Sidebar

- **admin**: Para administradores
- **secretaria**: Para secretarios
- **profesor**: Para profesores
- **propietario**: Para propietarios
- **alumno**: Para alumnos

## Secciones para Sidebar Admin
- 'dashboard'
- 'academias'
- 'profesores'

## Secciones para Sidebar Secretaria
- 'dashboard'
- 'alumnos'
- 'cursos'
- 'aulas'
- 'reservas'

## Componentes Comunes

### Header de Página
```html
<div class="page-header">
    <h1 class="page-title">Título Principal</h1>
    <p class="page-subtitle">Descripción de la página</p>
</div>
```

### Header con Acción
```html
<div class="page-header">
    <div class="d-flex justify-content-between align-items-center">
        <div>
            <h1 class="page-title">Título</h1>
            <p class="page-subtitle">Descripción</p>
        </div>
        <a th:href="@{/ruta/nueva}" class="btn btn-primary">
            <i class="bi bi-plus-lg"></i> Nuevo Item
        </a>
    </div>
</div>
```

### Cards de Estadísticas
```html
<div class="stats-grid">
    <div class="stat-card">
        <div class="stat-card-header">
            <span class="stat-card-title">Título</span>
            <div class="stat-card-icon primary">
                <i class="bi bi-icon"></i>
            </div>
        </div>
        <div class="stat-card-value">123</div>
    </div>
</div>
```

Tipos de iconos: `primary`, `success`, `warning`, `danger`

### Tabla
```html
<div class="card">
    <div class="card-header">
        <h3 class="card-title">Título de la Tabla</h3>
    </div>
    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <th>Columna 1</th>
                    <th>Columna 2</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Dato 1</td>
                    <td>Dato 2</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>
```

### Formulario
```html
<div class="card">
    <div class="card-header">
        <h3 class="card-title">Título del Formulario</h3>
    </div>
    <div class="card-body">
        <form>
            <div class="form-group">
                <label for="campo" class="form-label">Etiqueta</label>
                <input type="text" class="form-control" id="campo" placeholder="Placeholder">
            </div>
            
            <!-- Botones al final -->
            <div style="display: flex; gap: 1rem; justify-content: flex-end; margin-top: 2rem; padding-top: 1.5rem; border-top: 1px solid var(--border-color);">
                <a href="#" class="btn btn-outline">
                    <i class="bi bi-x-lg"></i> Cancelar
                </a>
                <button type="submit" class="btn btn-primary">
                    <i class="bi bi-check-lg"></i> Guardar
                </button>
            </div>
        </form>
    </div>
</div>
```

### Badges (Estado)
```html
<span class="badge badge-success">
    <i class="bi bi-check-circle"></i> Activo
</span>

<span class="badge badge-danger">
    <i class="bi bi-x-circle"></i> Inactivo
</span>

<span class="badge badge-warning">
    <i class="bi bi-exclamation-circle"></i> Pendiente
</span>

<span class="badge badge-primary">
    <i class="bi bi-info-circle"></i> Información
</span>

<span class="badge badge-secondary">
    <i class="bi bi-dash-circle"></i> Neutro
</span>
```

### Alertas
```html
<div class="alert alert-success">
    <i class="bi bi-check-circle"></i> Mensaje de éxito
</div>

<div class="alert alert-danger">
    <i class="bi bi-exclamation-triangle"></i> Mensaje de error
</div>

<div class="alert alert-warning">
    <i class="bi bi-exclamation-triangle"></i> Mensaje de advertencia
</div>

<div class="alert alert-info">
    <i class="bi bi-info-circle"></i> Mensaje informativo
</div>
```

### Botones
```html
<!-- Botones primarios -->
<button class="btn btn-primary">Primario</button>
<button class="btn btn-success">Éxito</button>
<button class="btn btn-danger">Peligro</button>
<button class="btn btn-warning">Advertencia</button>
<button class="btn btn-secondary">Secundario</button>
<button class="btn btn-outline">Outline</button>

<!-- Tamaños -->
<button class="btn btn-primary btn-sm">Pequeño</button>
<button class="btn btn-primary">Normal</button>
<button class="btn btn-primary btn-lg">Grande</button>

<!-- Con iconos -->
<button class="btn btn-primary">
    <i class="bi bi-plus-lg"></i> Con Icono
</button>
```

### Estado Vacío
```html
<div class="empty-state">
    <div class="empty-state-icon">
        <i class="bi bi-inbox"></i>
    </div>
    <h3 class="empty-state-title">No hay datos</h3>
    <p class="empty-state-description">Descripción del estado vacío</p>
    <a href="#" class="btn btn-primary">
        <i class="bi bi-plus-lg"></i> Crear Primero
    </a>
</div>
```

### Grupo de Botones de Acción
```html
<div class="action-buttons">
    <a href="#" class="btn btn-sm btn-primary">
        <i class="bi bi-pencil"></i> Editar
    </a>
    <button class="btn btn-sm btn-danger">
        <i class="bi bi-trash"></i> Eliminar
    </button>
</div>
```

## Clases de Utilidad

### Espaciado
- `mb-0`, `mb-1`, `mb-2`, `mb-3`, `mb-4`: Margin bottom
- `mt-0`, `mt-1`, `mt-2`, `mt-3`, `mt-4`: Margin top

### Layout
- `d-flex`: Display flex
- `justify-content-between`: Justify content space-between
- `align-items-center`: Align items center
- `gap-1`, `gap-2`: Gap entre elementos

### Texto
- `text-muted`: Texto gris claro
- `text-secondary`: Texto secundario

## Iconos Bootstrap (Bootstrap Icons)

Usa iconos de Bootstrap Icons con la clase `bi bi-nombre-icono`:
- `bi-plus-lg`: Más
- `bi-pencil`: Editar
- `bi-trash`: Eliminar
- `bi-check-circle`: Check
- `bi-x-circle`: X
- `bi-exclamation-triangle`: Advertencia
- `bi-info-circle`: Información
- `bi-people-fill`: Personas
- `bi-person-badge`: Persona con badge
- `bi-building`: Edificio
- `bi-book-fill`: Libro
- `bi-door-closed-fill`: Puerta
- `bi-calendar-check-fill`: Calendario

## Colores CSS Variables

Los colores se definen en el archivo `style.css`:
- `var(--primary-color)`: Color primario (#6366f1)
- `var(--success-color)`: Color de éxito (#10b981)
- `var(--warning-color)`: Color de advertencia (#f59e0b)
- `var(--danger-color)`: Color de peligro (#ef4444)
- `var(--text-primary)`: Texto principal
- `var(--text-secondary)`: Texto secundario
- `var(--text-muted)`: Texto apagado
- `var(--bg-primary)`: Fondo primario (blanco)
- `var(--bg-secondary)`: Fondo secundario (#f8fafc)
- `var(--border-color)`: Color de borde

## Páginas ya Actualizadas

✅ Login
✅ Admin Dashboard
✅ Admin Academias Lista
✅ Admin Academia Nueva
✅ Admin Academia Editar
✅ Admin Profesores Lista
✅ Secretaria Dashboard
✅ Profesor Dashboard
✅ Alumno Dashboard
✅ Propietario Dashboard

## Páginas Pendientes de Actualizar

❌ Admin Profesor Nuevo
❌ Admin Profesor Editar
❌ Secretaria Alumnos Lista
❌ Secretaria Alumno Nuevo
❌ Secretaria Alumno Editar
❌ Secretaria Cursos Lista
❌ Secretaria Curso Nuevo
❌ Secretaria Curso Editar
❌ Secretaria Aulas Lista
❌ Secretaria Aula Nueva
❌ Secretaria Aula Editar
❌ Secretaria Reservas Lista
❌ Secretaria Reserva Nueva
❌ Secretaria Reserva Editar
❌ Secretaria Matriculas
❌ Páginas de error
