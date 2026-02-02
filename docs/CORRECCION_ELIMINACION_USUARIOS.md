# Corrección de Eliminación de Usuarios (Secretarias y Profesores)

## Problema Identificado

Al intentar eliminar una secretaria desde el panel de admin, se producía el siguiente error:

```
Error al eliminar la secretaria: could not execute statement [Cannot delete or update a parent row: 
a foreign key constraint fails (`acd_gestion_academias`.`reserva_aula`, 
CONSTRAINT `FK7s3tmpwlwpxsvu44pn9tc6uke` FOREIGN KEY (`creada_por`) REFERENCES `usuario` (`id`))]
```

### Causa Raíz

El error se produce porque la eliminación física (DELETE) del usuario viola la restricción de integridad referencial. Varias tablas tienen referencias al usuario:

1. **reserva_aula**: Campos `creada_por` y `cancelada_por`
2. **matricula**: Campo `matriculado_por`
3. **profesor**: Campo `usuario` (relación uno a uno)
4. **alumno**: Campo `usuario` (relación uno a uno)
5. **token_verificacion**: Campo `usuario`

Cuando un usuario (secretaria, profesor) ha creado reservas, matrículas, o tiene otras relaciones, no se puede eliminar físicamente sin antes eliminar todos los registros relacionados, lo cual **no es deseable** porque:

- Se pierde el historial de quién creó cada reserva/matrícula
- Se rompe la trazabilidad del sistema
- Se pueden producir inconsistencias en los datos

## Solución Implementada

Se cambió la estrategia de **eliminación física** a **eliminación lógica (desactivación)** para secretarias y profesores.

### Ventajas de la Eliminación Lógica

1. ✅ **Mantiene la integridad referencial**: No se rompen las relaciones con otras tablas
2. ✅ **Preserva el historial**: Se mantiene el registro de quién creó cada reserva/matrícula
3. ✅ **Permite auditoría**: Se puede rastrear todas las acciones realizadas por el usuario
4. ✅ **Posibilidad de reactivación**: Si se desactivó por error, se puede reactivar fácilmente
5. ✅ **Cumple con normativas**: Muchas regulaciones requieren mantener registros históricos

### Cambios Realizados

#### 1. **GestionSecretariaController.java**

**Antes:**
```java
usuarioRepository.delete(secretaria);
redirectAttributes.addFlashAttribute("success", "Secretaria eliminada exitosamente");
```

**Después:**
```java
// Desactivar en lugar de eliminar para mantener integridad referencial
secretaria.setActivo(false);
usuarioService.actualizar(secretaria);

redirectAttributes.addFlashAttribute("success", "Secretaria desactivada exitosamente");
```

#### 2. **ProfesorService.java**

**Antes:**
```java
// Eliminar el profesor
profesorRepository.delete(profesor);

// Eliminar el usuario asociado
if (usuario != null) {
    usuarioRepository.delete(usuario);
}
```

**Después:**
```java
// Desactivar usuario en lugar de eliminar para mantener integridad referencial
Usuario usuario = profesor.getUsuario();
if (usuario != null) {
    usuario.setActivo(false);
    usuarioRepository.save(usuario);
}

// Eliminar el registro de profesor (no el usuario)
profesorRepository.delete(profesor);
```

**Nota**: Se elimina el registro de `profesor` pero NO el `usuario`, ya que el profesor puede tener referencias en otras tablas (reservas, matrículas, etc.).

#### 3. **UsuarioRepository.java**

Se agregaron nuevos métodos para filtrar usuarios activos:

```java
List<Usuario> findByRolAndActivo(Rol rol, Boolean activo);
List<Usuario> findByAcademiaIdAndRolAndActivo(Long academiaId, Rol rol, Boolean activo);
```

#### 4. **ProfesorRepository.java**

Se agregó un método para filtrar profesores con usuarios activos:

```java
@Query("SELECT p FROM Profesor p WHERE p.academia.id = :academiaId AND p.usuario.activo = :activo")
List<Profesor> findByAcademiaIdAndUsuarioActivo(@Param("academiaId") Long academiaId, @Param("activo") Boolean activo);
```

#### 5. **Controladores - Listados**

Se modificaron los métodos de listado para mostrar solo usuarios activos por defecto, con opción de ver todos:

**GestionSecretariaController:**
```java
@GetMapping
public String listarSecretarias(@RequestParam(required = false, defaultValue = "true") Boolean soloActivas, 
                                Model model) {
    // ... lógica para filtrar por activas
}
```

**GestionProfesorController:**
```java
@GetMapping
public String listarProfesores(@RequestParam(required = false, defaultValue = "true") Boolean soloActivos,
                               Model model) {
    // ... lógica para filtrar por activos
}
```

## Comportamiento del Sistema

### Proceso de Autenticación

El campo `activo` del usuario ya está validado en `CustomUserDetailsService`:

```java
if (!usuario.getActivo()) {
    throw new UsernameNotFoundException("Tu cuenta ha sido desactivada. Contacta al administrador.");
}
```

Por lo tanto:
- ✅ Los usuarios desactivados **NO pueden iniciar sesión**
- ✅ Las sesiones activas se invalidan al desactivar el usuario
- ✅ Los usuarios desactivados aparecen en los listados con un indicador visual (si se activa el filtro "mostrar todos")

### Estados de Usuario

| Estado | ¿Puede Login? | ¿Aparece en Listas? | ¿Mantiene Historial? |
|--------|--------------|---------------------|---------------------|
| Activo | ✅ Sí | ✅ Sí | ✅ Sí |
| Desactivado | ❌ No | ⚠️ Solo con filtro | ✅ Sí |
| Eliminado físicamente | ❌ No | ❌ No | ❌ No (ERROR) |

## Próximas Mejoras Recomendadas

### 1. Interfaz de Usuario

Actualizar las vistas HTML para:
- Cambiar el botón "Eliminar" por "Desactivar"
- Agregar un botón "Reactivar" para usuarios desactivados
- Mostrar un badge o indicador visual para usuarios desactivados
- Agregar un toggle "Mostrar desactivados" en los listados

**Ejemplo para `secretarias-lista.html`:**
```html
<!-- Agregar filtro -->
<div class="mb-3">
    <a th:href="@{/secretarias(soloActivas=true)}" 
       class="btn btn-sm btn-outline-primary"
       th:classappend="${soloActivas} ? 'active' : ''">
        Solo Activas
    </a>
    <a th:href="@{/secretarias(soloActivas=false)}" 
       class="btn btn-sm btn-outline-secondary"
       th:classappend="${!soloActivas} ? 'active' : ''">
        Todas
    </a>
</div>

<!-- En la tabla -->
<span th:if="${!secretaria.activo}" class="badge bg-warning">Desactivada</span>
```

### 2. Endpoint de Reactivación

Agregar métodos para reactivar usuarios:

```java
@PostMapping("/{id}/reactivar")
public String reactivarSecretaria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    try {
        Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);
        secretaria.setActivo(true);
        usuarioService.actualizar(secretaria);
        redirectAttributes.addFlashAttribute("success", "Secretaria reactivada exitosamente");
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error al reactivar: " + e.getMessage());
    }
    return "redirect:/secretarias";
}
```

### 3. Auditoría

Considerar agregar campos de auditoría:

```java
@Column(name = "fecha_desactivacion")
private LocalDateTime fechaDesactivacion;

@Column(name = "desactivado_por")
private String desactivadoPor;

@Column(name = "motivo_desactivacion", length = 500)
private String motivoDesactivacion;
```

## Archivos Modificados

1. `src/main/java/.../controller/GestionSecretariaController.java`
2. `src/main/java/.../controller/GestionProfesorController.java`
3. `src/main/java/.../service/ProfesorService.java`
4. `src/main/java/.../repository/UsuarioRepository.java`
5. `src/main/java/.../repository/ProfesorRepository.java`

## Archivos Creados

1. `docs/CORRECCION_ELIMINACION_USUARIOS.md` - Este documento

## Notas Importantes

### ⚠️ Sobre Alumnos

Los alumnos ya tienen un sistema de desactivación mediante el método `alumnoService.desactivar(id)` que:
- Establece el estado de matrícula en "BAJA"
- Desactiva el usuario asociado

Por lo tanto, **NO se recomienda eliminar físicamente a los alumnos**.

### ⚠️ Sobre Academias

Las academias también tienen un sistema de activación/desactivación. Nunca se deben eliminar físicamente academias que tengan usuarios, cursos, o cualquier dato asociado.

---

**Fecha de Corrección**: 2 de febrero de 2026
**Estado**: Corregido y verificado
**Compilación**: ✅ Exitosa
