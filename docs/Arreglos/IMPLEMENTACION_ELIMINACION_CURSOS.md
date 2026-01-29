# Implementación: Funcionalidad de Eliminación de Cursos

## Problema Identificado

El sistema no tenía una forma de eliminar cursos de la base de datos. Solo existía la funcionalidad de desactivar cursos, pero no de eliminarlos físicamente. Esto era necesario para poder eliminar profesores que tienen cursos asignados.

## Solución Implementada

Se implementó la funcionalidad completa de eliminación de cursos con validación de integridad referencial.

### Cambios Realizados

#### 1. CursoService.java

**Inyección de dependencia agregada:**
- `MatriculaRepository`: Para verificar si el curso tiene matrículas antes de eliminarlo

**Nuevo método `eliminar`:**
```java
@Transactional
public void eliminar(Long id) {
    Curso curso = obtenerPorId(id);
    
    // Verificar si el curso tiene matrículas
    List<Matricula> matriculas = matriculaRepository.findByCursoId(id);
    
    if (!matriculas.isEmpty()) {
        throw new IllegalStateException(
            "No se puede eliminar el curso porque tiene " + matriculas.size() + 
            " matrícula(s) registrada(s). Por favor, cancele las matrículas primero."
        );
    }
    
    // Eliminar el curso
    cursoRepository.delete(curso);
}
```

**Características:**
- ✅ Valida que el curso pertenezca a la academia del usuario
- ✅ Verifica que no haya matrículas asociadas
- ✅ Muestra mensaje descriptivo con el número de matrículas si existen
- ✅ Eliminación transaccional segura

#### 2. CursoController.java

**Permiso de acceso actualizado:**
```java
@PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")
```
- Ahora ADMIN también puede gestionar cursos (antes solo SECRETARIA)

**Nuevo endpoint:**
```java
@PostMapping("/{id}/eliminar")
public String eliminarCurso(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        cursoService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Curso eliminado exitosamente");
    } catch (IllegalStateException e) {
        // Error de validación (curso con matrículas)
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (Exception e) {
        // Otros errores
        redirectAttributes.addFlashAttribute("error", "Error al eliminar el curso: " + e.getMessage());
    }
    return "redirect:/secretaria/cursos";
}
```

#### 3. cursos-lista.html

**Botón de eliminar agregado:**
```html
<form th:action="@{/secretaria/cursos/{id}/eliminar(id=${curso.id})}"
      method="post"
      style="margin: 0;">
    <button type="submit" class="btn btn-danger btn-sm"
            onclick="return confirm('¿Está seguro de eliminar este curso? Esta acción no se puede deshacer y solo es posible si no tiene matrículas.');">
        <i class="bi bi-trash"></i> Eliminar
    </button>
</form>
```

**Ubicación:** En la columna de acciones, junto a los botones de Matrículas, Editar, Activar/Desactivar

## Comportamiento del Sistema

### Caso 1: Curso sin Matrículas
1. Usuario hace clic en "Eliminar"
2. Aparece confirmación: "¿Está seguro de eliminar este curso?..."
3. El sistema verifica que no hay matrículas
4. ✅ El curso se elimina de la base de datos
5. Se muestra mensaje: **"Curso eliminado exitosamente"**

### Caso 2: Curso con Matrículas
1. Usuario hace clic en "Eliminar"
2. Aparece confirmación
3. El sistema detecta matrículas asociadas
4. ❌ La eliminación se cancela
5. Se muestra mensaje: **"No se puede eliminar el curso porque tiene X matrícula(s) registrada(s). Por favor, cancele las matrículas primero."**

## Flujo para Eliminar un Profesor con Cursos

Ahora el usuario puede seguir este proceso:

### Paso 1: Identificar cursos del profesor
1. Ir a **Gestión de Profesores**
2. Intentar eliminar el profesor
3. Ver mensaje: "No se puede eliminar el profesor porque tiene X curso(s) asignado(s)"

### Paso 2: Acceder a los cursos
1. Ir a **Gestión de Cursos** (menú Secretaría o Admin)
2. Identificar los cursos del profesor que se desea eliminar

### Paso 3: Eliminar cada curso
Para cada curso del profesor:

**Si el curso NO tiene matrículas:**
- Click en "Eliminar"
- Confirmar
- ✅ Curso eliminado

**Si el curso tiene matrículas:**
1. Click en "Matrículas" del curso
2. Cancelar cada matrícula activa:
   - Click en "Cancelar" en cada matrícula
   - Proporcionar un motivo (opcional)
3. Volver a la lista de cursos
4. Click en "Eliminar" el curso
5. ✅ Curso eliminado

### Paso 4: Eliminar el profesor
1. Volver a **Gestión de Profesores**
2. Click en "Eliminar" del profesor
3. ✅ Profesor eliminado exitosamente (junto con su usuario)

## Alternativas a la Eliminación

### Opción 1: Desactivar en lugar de Eliminar
- Los cursos pueden **desactivarse** sin necesidad de cancelar matrículas
- Los datos históricos se conservan
- Recomendado para cursos que ya tuvieron actividad

### Opción 2: Reasignar Cursos
- Editar cada curso y cambiar el profesor asignado
- Permite eliminar al profesor sin perder los cursos
- Útil cuando otro profesor puede asumir las responsabilidades

## Restricciones y Validaciones

### Validaciones Implementadas

1. **Acceso por Academia:**
   - Solo se pueden eliminar cursos de la academia del usuario
   - Los ADMIN tienen acceso a todas las academias

2. **Validación de Matrículas:**
   - No se permite eliminar cursos con matrículas (activas, completadas o canceladas)
   - Se debe cancelar primero todas las matrículas

3. **Confirmación del Usuario:**
   - Mensaje de confirmación antes de eliminar
   - Advierte que la acción no se puede deshacer

### Restricciones de Base de Datos

El sistema mantiene la integridad referencial:
- `Curso` → `Profesor` (FK: profesor_id)
- `Matricula` → `Curso` (FK: curso_id)

## Mensajes de Error Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| "No se puede eliminar el curso porque tiene X matrícula(s)..." | El curso tiene matrículas registradas | Cancelar todas las matrículas primero |
| "Curso no encontrado o no pertenece a su academia" | Intentando eliminar curso de otra academia | Verificar que es el curso correcto |
| "No tiene acceso a esta academia" | Usuario sin permisos | Contactar administrador |

## Archivos Modificados

### Backend
1. ✅ `src/main/java/es/fempa/acd/demosecurityproductos/service/CursoService.java`
   - Agregado MatriculaRepository
   - Agregado método `eliminar(Long id)`

2. ✅ `src/main/java/es/fempa/acd/demosecurityproductos/controller/CursoController.java`
   - Cambiado `@PreAuthorize("hasRole('SECRETARIA')")` a `@PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")`
   - Agregado endpoint `@PostMapping("/{id}/eliminar")`

### Frontend
3. ✅ `src/main/resources/templates/secretaria/cursos-lista.html`
   - Agregado botón "Eliminar" en acciones de cada curso

## Pruebas Realizadas

- [x] Compilación exitosa del proyecto
- [ ] Prueba funcional: Eliminar curso sin matrículas
- [ ] Prueba funcional: Intentar eliminar curso con matrículas
- [ ] Verificación de mensajes de error descriptivos
- [ ] Prueba de flujo completo: Cancelar matrículas → Eliminar curso → Eliminar profesor

## Mejoras Futuras (Opcional)

1. **Eliminación en Cascada Controlada:**
   - Opción para eliminar curso y todas sus matrículas de una vez (con confirmación especial)

2. **Soft Delete:**
   - Marcar curso como eliminado en lugar de borrado físico
   - Mantener historial completo

3. **Vista Previa de Dependencias:**
   - Mostrar lista de matrículas antes de intentar eliminar
   - Botón directo para ir a gestión de matrículas

4. **Reasignación Masiva:**
   - Interfaz para reasignar todos los cursos de un profesor a otro
   - Facilitar el proceso de eliminación de profesores

---

**Fecha de implementación:** 29 de enero de 2026  
**Estado:** ✅ Implementado y compilado exitosamente  
**Relacionado con:** `SOLUCION_ERROR_ELIMINACION_PROFESORES.md`
