# Implementación: Gestión de Profesores para Secretarias

## Resumen de Cambios

Se ha implementado la funcionalidad completa para que las secretarias puedan gestionar profesores de su academia, similar a como lo hacen los propietarios con sus academias.

## Archivos Creados

### 1. Controlador: SecretariaGestionProfesorController.java
**Ruta:** `src/main/java/es/fempa/acd/demosecurityproductos/controller/SecretariaGestionProfesorController.java`

**Funcionalidades implementadas:**
- ✅ **Listar profesores**: Muestra todos los profesores de la academia de la secretaria con filtro de activos/inactivos
- ✅ **Crear profesor**: Permite crear un nuevo profesor asignado a la academia
- ✅ **Editar profesor**: Permite modificar los datos de un profesor existente
- ✅ **Desactivar profesor**: Desactiva temporalmente un profesor sin eliminar su historial
- ✅ **Reactivar profesor**: Reactiva un profesor previamente desactivado

**Características de seguridad:**
- Solo puede gestionar profesores de su propia academia
- Valida permisos en cada operación
- Rol requerido: `SECRETARIA`

### 2. Plantillas HTML

#### a) profesores-lista.html
**Ruta:** `src/main/resources/templates/secretaria/profesores-lista.html`

**Características:**
- Tabla con listado completo de profesores
- Filtros: Solo activos / Todos
- Información mostrada: ID, nombre completo, email, usuario, especialidad, fecha contratación, estado
- Acciones: Editar, Desactivar/Reactivar
- Estado visual (badges) para identificar profesores activos/inactivos
- Mensaje cuando no hay profesores registrados

#### b) profesor-nuevo.html
**Ruta:** `src/main/resources/templates/secretaria/profesor-nuevo.html`

**Campos del formulario:**
- **Datos de Usuario:**
  - Usuario (username) *
  - Email *
  - Contraseña *
  
- **Datos Personales:**
  - Nombre *
  - Apellidos *
  
- **Datos Profesionales:**
  - Especialidad (opcional)
  - Biografía (opcional)

**Características:**
- Validación de campos obligatorios
- Email verificado automáticamente
- El profesor se asigna automáticamente a la academia de la secretaria

#### c) profesor-editar.html
**Ruta:** `src/main/resources/templates/secretaria/profesor-editar.html`

**Campos editables:**
- Nombre
- Apellidos
- Email
- Especialidad
- Biografía

**Campos de solo lectura:**
- Usuario (username)
- Fecha de contratación
- Academia (fija, no se puede cambiar)

### 3. Navegación Actualizada

**Archivo:** `src/main/resources/templates/fragments.html`

**Cambio realizado:**
- Añadido enlace "Profesores" en el sidebar de secretaria entre "Alumnos" y "Cursos"
- Icono: `bi-person-badge-fill`
- URL: `/secretaria/profesores`

## Estructura de URLs

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/secretaria/profesores` | Lista todos los profesores |
| GET | `/secretaria/profesores?soloActivos=true` | Lista solo profesores activos |
| GET | `/secretaria/profesores?soloActivos=false` | Lista todos los profesores |
| GET | `/secretaria/profesores/nuevo` | Formulario para crear profesor |
| POST | `/secretaria/profesores/crear` | Procesa la creación del profesor |
| GET | `/secretaria/profesores/{id}/editar` | Formulario para editar profesor |
| POST | `/secretaria/profesores/{id}/actualizar` | Procesa la actualización del profesor |
| POST | `/secretaria/profesores/{id}/eliminar` | Desactiva un profesor |
| POST | `/secretaria/profesores/{id}/reactivar` | Reactiva un profesor |

## Validaciones y Restricciones

### Seguridad
- ✅ Solo usuarios con rol `SECRETARIA` pueden acceder
- ✅ Solo puede ver y gestionar profesores de su propia academia
- ✅ No puede cambiar el profesor a otra academia (no tiene selector de academia)

### Creación de Profesores
- ✅ El username debe ser único en todo el sistema
- ✅ El email debe ser único en todo el sistema
- ✅ La contraseña debe tener mínimo 6 caracteres
- ✅ Nombre, apellidos, email y usuario son obligatorios
- ✅ Especialidad y biografía son opcionales

### Eliminación de Profesores
- ✅ No se puede eliminar si tiene cursos asignados
- ✅ En lugar de eliminar, se desactiva el usuario
- ✅ Se mantiene el historial completo del profesor
- ✅ Se puede reactivar posteriormente

## Diferencias con la Gestión del Propietario

| Característica | Propietario | Secretaria |
|----------------|-------------|------------|
| Puede cambiar de academia | ✅ Sí (entre sus academias) | ❌ No |
| Selector de academia | ✅ Sí | ❌ No |
| Columna "Academia" en tabla | ✅ Visible | ❌ No visible (solo hay una) |
| Filtro de academia | ✅ Sí (múltiples) | ❌ No necesario |
| Permisos | Todas sus academias | Solo su academia |

## Flujo de Trabajo

### 1. Crear un Profesor
1. La secretaria accede a `/secretaria/profesores`
2. Hace clic en "Nuevo Profesor"
3. Completa el formulario con los datos
4. Al enviar, se crea:
   - Un usuario con rol `PROFESOR`
   - Un registro de profesor asociado
   - Se asigna automáticamente a la academia de la secretaria
   - El email se marca como verificado automáticamente
5. Redirige a la lista con mensaje de éxito

### 2. Editar un Profesor
1. Desde la lista, hace clic en "Editar"
2. Modifica los campos necesarios
3. Al guardar, actualiza tanto el usuario como el profesor
4. Redirige a la lista con mensaje de éxito

### 3. Desactivar un Profesor
1. Desde la lista, hace clic en "Desactivar"
2. Confirma la acción
3. El sistema:
   - Verifica que no tenga cursos asignados
   - Desactiva el usuario (no puede iniciar sesión)
   - Mantiene todos los datos históricos
4. Redirige con mensaje de éxito

### 4. Reactivar un Profesor
1. Desde la lista (con filtro "Todos"), hace clic en "Reactivar"
2. Confirma la acción
3. El profesor puede volver a iniciar sesión
4. Redirige con mensaje de éxito

## Mensajes de Error Comunes

- **"No se pudo identificar la academia"**: La secretaria no tiene una academia asignada
- **"Usuario ya existe"**: El username ya está registrado en el sistema
- **"Email ya existe"**: El email ya está registrado en el sistema
- **"No tienes permisos para editar este profesor"**: El profesor pertenece a otra academia
- **"No se puede eliminar el profesor porque tiene X curso(s) asignado(s)"**: Debe reasignar o eliminar los cursos primero
- **"Profesor no encontrado"**: El ID del profesor no existe

## Próximos Pasos Sugeridos

1. ✅ **Implementado**: Gestión completa de profesores para secretarias
2. 🔄 **Opcional**: Añadir vista de detalle del profesor (solo lectura)
3. 🔄 **Opcional**: Exportar listado de profesores a PDF/Excel
4. 🔄 **Opcional**: Búsqueda y filtros avanzados (por especialidad, nombre, etc.)
5. 🔄 **Opcional**: Historial de cambios del profesor
6. 🔄 **Opcional**: Estadísticas de profesores en el dashboard

## Pruebas Recomendadas

### Funcionales
- [ ] Crear un profesor con todos los datos
- [ ] Crear un profesor solo con datos obligatorios
- [ ] Editar un profesor existente
- [ ] Desactivar un profesor sin cursos
- [ ] Intentar desactivar un profesor con cursos asignados
- [ ] Reactivar un profesor desactivado
- [ ] Filtrar por activos/todos

### Seguridad
- [ ] Intentar editar un profesor de otra academia (debe fallar)
- [ ] Intentar acceder sin rol SECRETARIA (debe redirigir)
- [ ] Verificar que solo ve profesores de su academia
- [ ] Intentar crear profesor con username duplicado
- [ ] Intentar crear profesor con email duplicado

### UI/UX
- [ ] Verificar responsive design en móviles
- [ ] Verificar que los mensajes de éxito/error se muestran correctamente
- [ ] Verificar navegación entre páginas
- [ ] Verificar que el sidebar marca correctamente la opción activa
- [ ] Verificar que los badges de estado se muestran correctamente

## Notas Técnicas

- **Fecha de contratación**: Se establece automáticamente con `LocalDate.now()` al crear
- **Email verificado**: Se marca como `true` automáticamente (creado por secretaria)
- **Academia asignada**: Se obtiene del usuario autenticado de la secretaria
- **Eliminación lógica**: Se desactiva el usuario en lugar de eliminar el registro
- **Transacciones**: Las operaciones de creación/actualización son transaccionales
- **Validaciones**: Se validan tanto en cliente (HTML5) como en servidor (Spring)

---

**Fecha de implementación**: 6 de febrero de 2026
**Autor**: Sistema de Gestión de Academias
**Versión**: 1.0
