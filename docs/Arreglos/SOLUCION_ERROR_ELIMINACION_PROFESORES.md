# Solución: Error al Eliminar Profesores con Cursos Asignados

## Problema Identificado

Al intentar eliminar un profesor que tiene cursos asignados, se producía el siguiente error:

```
Error al eliminar el profesor: could not execute statement [Cannot delete or update a parent row: 
a foreign key constraint fails (`acd_gestion_academias`.`curso`, CONSTRAINT `FK2w3lfylwy3y81e5kubeyu4t3k` 
FOREIGN KEY (`profesor_id`) REFERENCES `profesor` (`id`))] [delete from profesor where id=?]; 
SQL [delete from profesor where id=?]; constraint [null]
```

### Causa del Error

El error se producía porque:
1. La tabla `curso` tiene una clave foránea `profesor_id` que referencia a la tabla `profesor`
2. La restricción de integridad referencial impide eliminar un profesor que tiene cursos asignados
3. No se validaba esta condición antes de intentar la eliminación

## Solución Implementada

Se implementó una validación previa a la eliminación que verifica si el profesor tiene cursos asignados.

### Cambios Realizados

#### 1. ProfesorService.java

Se agregó un nuevo método `eliminarProfesor` que:
- Verifica si el profesor tiene cursos asignados
- Si tiene cursos, lanza una excepción con un mensaje descriptivo
- Si no tiene cursos, procede a eliminar el profesor y su usuario asociado

```java
@Transactional
public void eliminarProfesor(Long id) {
    Profesor profesor = obtenerPorId(id);
    
    // Verificar si el profesor tiene cursos asignados
    List<Curso> cursosAsignados = cursoRepository.findByProfesorId(id);
    
    if (!cursosAsignados.isEmpty()) {
        throw new IllegalStateException(
            "No se puede eliminar el profesor porque tiene " + cursosAsignados.size() + 
            " curso(s) asignado(s). Por favor, reasigne o elimine los cursos primero."
        );
    }
    
    // Guardar referencia al usuario para eliminarlo después
    Usuario usuario = profesor.getUsuario();
    
    // Eliminar el profesor
    profesorRepository.delete(profesor);
    
    // Eliminar el usuario asociado
    if (usuario != null) {
        usuarioRepository.delete(usuario);
    }
}
```

**Dependencias agregadas:**
- `CursoRepository`: Para verificar los cursos asignados
- `UsuarioRepository`: Para eliminar el usuario asociado

#### 2. GestionProfesorController.java

Se modificó el método `eliminarProfesor` para:
- Usar el nuevo método del servicio
- Capturar específicamente `IllegalStateException` para mostrar el mensaje de validación
- Mantener el manejo general de excepciones para otros errores

```java
@PostMapping("/{id}/eliminar")
public String eliminarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        profesorService.eliminarProfesor(id);
        redirectAttributes.addFlashAttribute("success", "Profesor eliminado exitosamente");
    } catch (IllegalStateException e) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al eliminar el profesor: " + e.getMessage());
    }
    return "redirect:/profesores";
}
```

## Comportamiento Actual

### Caso 1: Profesor sin Cursos Asignados
- ✅ El profesor se elimina exitosamente
- ✅ El usuario asociado también se elimina
- ✅ Se muestra mensaje: "Profesor eliminado exitosamente"

### Caso 2: Profesor con Cursos Asignados
- ❌ La eliminación se cancela
- ℹ️ Se muestra mensaje: "No se puede eliminar el profesor porque tiene X curso(s) asignado(s). Por favor, reasigne o elimine los cursos primero."
- ✅ Los datos permanecen intactos

## Próximos Pasos (Opcional)

Para mejorar aún más la funcionalidad, se podrían implementar:

1. **Interfaz de Reasignación**: Permitir reasignar los cursos de un profesor a otro antes de eliminarlo
2. **Soft Delete**: Marcar el profesor como inactivo en lugar de eliminarlo físicamente
3. **Vista de Dependencias**: Mostrar la lista de cursos asignados antes de intentar eliminar
4. **Eliminación en Cascada Controlada**: Dar opción de desactivar los cursos al eliminar el profesor

## Pruebas Realizadas

- [x] Compilación exitosa del proyecto
- [ ] Prueba funcional: Intentar eliminar profesor con cursos asignados
- [ ] Prueba funcional: Eliminar profesor sin cursos asignados
- [ ] Verificación de mensajes de error descriptivos

## Archivos Modificados

1. `src/main/java/es/fempa/acd/demosecurityproductos/service/ProfesorService.java`
   - Agregadas dependencias: CursoRepository, UsuarioRepository
   - Agregado método: `eliminarProfesor(Long id)`

2. `src/main/java/es/fempa/acd/demosecurityproductos/controller/GestionProfesorController.java`
   - Modificado método: `eliminarProfesor(@PathVariable Long id, RedirectAttributes redirectAttributes)`

---

**Fecha de implementación:** 29 de enero de 2026
**Estado:** ✅ Implementado y compilado exitosamente
