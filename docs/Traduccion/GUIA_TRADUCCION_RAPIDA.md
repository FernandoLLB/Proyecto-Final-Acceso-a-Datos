# Guía Rápida: Cómo Completar las Traducciones

## Para Traducir una Página Nueva

### Paso 1: Identificar los textos a traducir
Busca en el HTML todos los textos estáticos que necesitan traducción:
- Títulos y subtítulos
- Etiquetas de formularios
- Botones
- Mensajes de alerta
- Nombres de columnas en tablas
- Placeholders

### Paso 2: Crear las claves en los archivos de mensajes

Abre ambos archivos:
- `src/main/resources/i18n/messages_es.properties`
- `src/main/resources/i18n/messages_en.properties`

Agrega la misma clave en ambos archivos con sus respectivas traducciones:

**messages_es.properties:**
```properties
curso.titulo=Gestión de Cursos
curso.crear=Crear Curso
curso.nombre=Nombre del Curso
```

**messages_en.properties:**
```properties
curso.titulo=Course Management
curso.crear=Create Course
curso.nombre=Course Name
```

### Paso 3: Usar las claves en Thymeleaf

Reemplaza los textos estáticos con expresiones Thymeleaf:

**Antes:**
```html
<h1>Gestión de Cursos</h1>
```

**Después:**
```html
<h1 th:text="#{curso.titulo}">Gestión de Cursos</h1>
```

## Patrones Comunes

### 1. Textos Simples
```html
<span th:text="#{clave.mensaje}">Texto por defecto</span>
```

### 2. Títulos de Página
```html
<h1 th:text="#{student.title}">Gestión de Alumnos</h1>
```

### 3. Botones
```html
<button class="btn btn-primary">
    <i class="bi bi-save"></i> 
    <span th:text="#{app.save}">Guardar</span>
</button>
```

### 4. Enlaces
```html
<a th:href="@{/ruta}" th:text="#{app.view}">Ver</a>
```

### 5. Placeholders
```html
<input type="text" th:placeholder="#{student.name}" />
```

### 6. Títulos de Atributos
```html
<button th:title="#{app.edit}">
    <i class="bi bi-pencil"></i>
</button>
```

### 7. Valores Concatenados
```html
<p th:text="#{label.total} + ': ' + ${count}">Total: 5</p>
```

### 8. Con Parámetros
```html
<!-- En el archivo de mensajes -->
welcome.message=Bienvenido, {0}

<!-- En el HTML -->
<p th:text="#{welcome.message(${username})}">Bienvenido, Usuario</p>
```

## Ejemplo Completo: Traducir cursos-lista.html

### ANTES (sin i18n):
```html
<h1>Gestión de Cursos</h1>
<p>Administra los cursos de tu academia</p>
<a href="/secretaria/cursos/nuevo" class="btn btn-primary">
    <i class="bi bi-plus-circle"></i> Nuevo Curso
</a>

<div class="stat-card-title">Total Cursos</div>
<div class="stat-card-title">Cursos Activos</div>
```

### DESPUÉS (con i18n):
```html
<h1 th:text="#{course.title}">Gestión de Cursos</h1>
<p th:text="#{course.manage}">Administra los cursos de tu academia</p>
<a th:href="@{/secretaria/cursos/nuevo}" class="btn btn-primary">
    <i class="bi bi-plus-circle"></i> <span th:text="#{course.new}">Nuevo Curso</span>
</a>

<div class="stat-card-title" th:text="#{course.total}">Total Cursos</div>
<div class="stat-card-title" th:text="#{course.active}">Cursos Activos</div>
```

## Claves Ya Disponibles

Las siguientes claves ya están definidas en los archivos de mensajes y puedes usarlas directamente:

### Comunes
- `app.name` - Nombre de la aplicación
- `app.welcome` - Bienvenido
- `app.logout` - Cerrar Sesión
- `app.back` - Volver
- `app.dashboard` - Panel de Control
- `app.actions` - Acciones
- `app.save` - Guardar
- `app.cancel` - Cancelar
- `app.edit` - Editar
- `app.delete` - Eliminar
- `app.create` - Crear
- `app.view` - Ver
- `app.search` - Buscar
- `app.filter` - Filtrar
- `app.clear` - Limpiar
- `app.active` - Activo
- `app.inactive` - Inactivo
- `app.status` - Estado
- `app.date` - Fecha
- `app.new` - Nuevo

### Estudiantes
- `student.title` - Gestión de Alumnos
- `student.list` - Lista de Alumnos
- `student.new` - Alta de Alumno
- `student.edit` - Editar Alumno
- `student.manage` - Administra los alumnos de tu academia
- `student.active` - Activos
- `student.inactive` - Inactivos
- `student.total` - Total Alumnos

### Cursos
- `course.title` - Gestión de Cursos
- `course.list` - Lista de Cursos
- `course.new` - Crear Curso
- `course.edit` - Editar Curso
- `course.manage` - Administra los cursos de tu academia
- `course.total` - Total Cursos
- `course.active` - Cursos Activos
- `course.inactive` - Cursos Inactivos

### Aulas
- `classroom.title` - Gestión de Aulas
- `classroom.list` - Lista de Aulas
- `classroom.new` - Nueva Aula
- `classroom.edit` - Editar Aula
- `classroom.manage` - Administra las aulas de tu academia

### Reservas
- `reservation.title` - Gestión de Reservas de Aulas
- `reservation.list` - Lista de Reservas
- `reservation.new` - Nueva Reserva
- `reservation.edit` - Editar Reserva
- `reservation.manage` - Administra las reservas de aulas de tu academia

Ver el archivo completo: `src/main/resources/i18n/messages_es.properties`

## Checklist para Cada Página

- [ ] Actualizar el título de la página: `<head th:replace="~{fragments :: head(#{clave})}">`
- [ ] Traducir el título principal (h1)
- [ ] Traducir el subtítulo (page-subtitle)
- [ ] Traducir botones de acción
- [ ] Traducir encabezados de cards
- [ ] Traducir títulos de stat-cards
- [ ] Traducir encabezados de tablas (th)
- [ ] Traducir etiquetas de formularios
- [ ] Traducir placeholders
- [ ] Traducir mensajes de alerta/error
- [ ] Traducir botones dentro de tablas
- [ ] Traducir leyendas y notas

## Tips y Mejores Prácticas

1. **Mantén el texto por defecto**: Siempre deja el texto original en español como valor por defecto en el HTML
   ```html
   <span th:text="#{clave}">Texto por defecto en español</span>
   ```

2. **Usa nombres consistentes**: Sigue el patrón establecido
   - `entidad.action` → `student.edit`, `course.new`
   - `entidad.property` → `student.name`, `course.duration`

3. **No traduzcas iconos ni clases CSS**: Solo traduce el contenido de texto

4. **Prueba después de cada cambio**: Verifica que la página se vea correctamente en ambos idiomas

5. **Cuidado con los espacios**: En concatenaciones, asegúrate de incluir espacios:
   ```html
   th:text="#{label} + ': ' + ${value}"
   ```

## Orden Recomendado de Traducción

### Alta Prioridad (Páginas más usadas)
1. ✅ login.html (Completado)
2. ✅ secretaria/dashboard.html (Completado)
3. ⚠️ secretaria/alumnos-lista.html (Parcialmente completado)
4. ⏳ secretaria/cursos-lista.html
5. ⏳ secretaria/aulas-lista.html
6. ⏳ secretaria/reservas-lista.html

### Media Prioridad (Formularios)
7. ⏳ secretaria/alumno-nuevo.html
8. ⏳ secretaria/alumno-editar.html
9. ⏳ secretaria/curso-nuevo.html
10. ⏳ secretaria/curso-editar.html

### Baja Prioridad (Otros roles)
11. ⏳ admin/dashboard.html
12. ⏳ admin/academias-lista.html
13. ⏳ profesor/dashboard.html
14. ⏳ propietario/dashboard.html
15. ⏳ alumno/dashboard.html

## Ejemplo Práctico: Traducir una Tabla

**ANTES:**
```html
<table class="table">
    <thead>
        <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Estado</th>
            <th>Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="item : ${items}">
            <td th:text="${item.id}">1</td>
            <td th:text="${item.nombre}">Nombre</td>
            <td>
                <span th:if="${item.activo}" class="badge bg-success">Activo</span>
                <span th:unless="${item.activo}" class="badge bg-secondary">Inactivo</span>
            </td>
            <td>
                <a th:href="@{/editar/{id}(id=${item.id})}" class="btn btn-warning btn-sm">
                    <i class="bi bi-pencil"></i> Editar
                </a>
            </td>
        </tr>
    </tbody>
</table>
```

**DESPUÉS:**
```html
<table class="table">
    <thead>
        <tr>
            <th>ID</th>
            <th th:text="#{app.name.label}">Nombre</th>
            <th th:text="#{app.status}">Estado</th>
            <th th:text="#{app.actions}">Acciones</th>
        </tr>
    </thead>
    <tbody>
        <tr th:each="item : ${items}">
            <td th:text="${item.id}">1</td>
            <td th:text="${item.nombre}">Nombre</td>
            <td>
                <span th:if="${item.activo}" class="badge bg-success" th:text="#{app.active}">Activo</span>
                <span th:unless="${item.activo}" class="badge bg-secondary" th:text="#{app.inactive}">Inactivo</span>
            </td>
            <td>
                <a th:href="@{/editar/{id}(id=${item.id})}" class="btn btn-warning btn-sm">
                    <i class="bi bi-pencil"></i> <span th:text="#{app.edit}">Editar</span>
                </a>
            </td>
        </tr>
    </tbody>
</table>
```

## Recursos de Ayuda

- **Archivos de mensajes**: `src/main/resources/i18n/`
- **Documentación completa**: `docs/IMPLEMENTACION_I18N.md`
- **Thymeleaf i18n docs**: https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html#using-texts

## ¿Necesitas Agregar una Nueva Clave?

1. Abre `messages_es.properties` y `messages_en.properties`
2. Agrega la clave al final de la sección correspondiente
3. Asegúrate de que la clave sea la misma en ambos archivos
4. Usa la clave en tu HTML con `#{tu.nueva.clave}`
5. Recompila y prueba

---

¡Recuerda! El objetivo es que **todo el texto visible** esté traducido, excepto el título "Gestor de Academias" que permanece igual en todos los idiomas.
