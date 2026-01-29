# Solución: Error al Crear Profesores desde Admin

## Problema Identificado

Cuando el administrador intentaba crear un profesor, se producía un error (aunque el profesor sí se creaba en la base de datos). Sin embargo, estos profesores no aparecían correctamente en la lista porque:

1. **No se asignaba una academia al profesor**: El código anterior usaba `usuario.getAcademia()` para asignar la academia al nuevo profesor, pero el usuario admin no tiene academia asignada (lo cual es correcto para un ADMIN).

2. **Falta de selección de academia**: El formulario no permitía seleccionar a qué academia pertenecería el nuevo profesor.

## Cambios Realizados

### 1. Controller: `GestionProfesorController.java`

#### Imports agregados:
```java
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import java.util.List;
```

#### Dependencia agregada:
```java
private final AcademiaService academiaService;
```

#### Método `nuevoProfesorForm` modificado:
Ahora carga la lista de academias activas cuando el usuario es ADMIN:
```java
@GetMapping("/nuevo")
public String nuevoProfesorForm(Model model) {
    Usuario usuario = securityUtils.getUsuarioAutenticado();
    
    // Si es ADMIN, cargar todas las academias activas para que pueda elegir
    if (usuario.getRol() == Rol.ADMIN) {
        List<Academia> academias = academiaService.listarActivas();
        model.addAttribute("academias", academias);
    }
    
    model.addAttribute("profesor", new Profesor());
    model.addAttribute("usuario", new Usuario());
    return "admin/profesor-nuevo";
}
```

#### Método `crearProfesor` modificado:
Ahora acepta el parámetro `academiaId` y asigna la academia correctamente:
```java
@PostMapping("/crear")
public String crearProfesor(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String nombre,
                           @RequestParam String apellidos,
                           @RequestParam(required = false) String especialidad,
                           @RequestParam(required = false) String biografia,
                           @RequestParam(required = false) Long academiaId,  // ← NUEVO
                           RedirectAttributes redirectAttributes,
                           Model model) {
    try {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Academia academiaAsignar = null;

        // Determinar la academia a asignar
        if (usuario.getRol() == Rol.ADMIN) {
            // Si es ADMIN, debe proporcionar academiaId o puede ser null
            if (academiaId != null) {
                academiaAsignar = academiaService.obtenerPorId(academiaId);
            }
        } else {
            // Si no es ADMIN, usar su propia academia
            if (usuario.getAcademia() == null) {
                redirectAttributes.addFlashAttribute("error", "No se pudo identificar la academia");
                return "redirect:/profesores";
            }
            academiaAsignar = usuario.getAcademia();
        }

        // Crear usuario con rol PROFESOR
        Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.PROFESOR);
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setApellidos(apellidos);
        nuevoUsuario.setAcademia(academiaAsignar);  // ← ACADEMIA ASIGNADA CORRECTAMENTE
        usuarioService.actualizar(nuevoUsuario);

        // Crear profesor
        Profesor profesor = new Profesor();
        profesor.setUsuario(nuevoUsuario);
        profesor.setAcademia(academiaAsignar);  // ← ACADEMIA ASIGNADA CORRECTAMENTE
        profesor.setEspecialidad(especialidad);
        profesor.setBiografia(biografia);
        profesor.setFechaContratacion(LocalDate.now());

        profesorRepository.save(profesor);

        redirectAttributes.addFlashAttribute("success", "Profesor creado exitosamente");
        return "redirect:/profesores";
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
        
        // Recargar academias si es ADMIN
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        if (usuario.getRol() == Rol.ADMIN) {
            List<Academia> academias = academiaService.listarActivas();
            model.addAttribute("academias", academias);
        }
        
        model.addAttribute("profesor", new Profesor());
        model.addAttribute("usuario", new Usuario());
        return "admin/profesor-nuevo";
    }
}
```

### 2. Vista: `profesor-nuevo.html`

Se agregó un campo de selección de academia que solo se muestra cuando el usuario es ADMIN:

```html
<!-- Campo Academia - Solo visible para ADMIN -->
<div th:if="${academias != null}" class="row">
    <div class="col-md-12 form-group">
        <label for="academiaId" class="form-label" th:text="#{academy.name}">Academia</label>
        <select class="form-control" id="academiaId" name="academiaId">
            <option value="" th:text="#{teacher.no.academy}">Sin academia</option>
            <option th:each="academia : ${academias}"
                    th:value="${academia.id}"
                    th:text="${academia.nombre}">
                Academia Ejemplo
            </option>
        </select>
        <small class="form-text text-muted" th:text="#{teacher.academy.optional}">
            Opcional: Selecciona una academia para asignar el profesor
        </small>
    </div>
</div>
```

### 3. Traducciones

Se agregaron las siguientes traducciones en los archivos de i18n:

**messages_es.properties:**
```properties
teacher.academy.optional=Opcional: Selecciona una academia para asignar el profesor
```

**messages_en.properties:**
```properties
teacher.academy.optional=Optional: Select an academy to assign the teacher
```

**messages.properties:**
```properties
teacher.no.academy=Sin academia
teacher.academy.optional=Opcional: Selecciona una academia para asignar el profesor
```

## Cómo Funciona Ahora

### Para Usuarios ADMIN:
1. Al acceder a "Nuevo Profesor", verá un selector de academia con todas las academias activas del sistema
2. Puede elegir una academia específica o dejar el campo vacío (el profesor se creará sin academia)
3. Al crear el profesor, se asigna correctamente la academia seleccionada

### Para Usuarios No-ADMIN (Propietario, Secretaria):
1. No ven el selector de academia
2. Los profesores se crean automáticamente con la academia del usuario que los crea
3. Funciona exactamente igual que antes

## Verificación

Para verificar que todo funciona correctamente:

1. **Reiniciar la aplicación:**
   ```powershell
   mvn clean spring-boot:run
   ```

2. **Iniciar sesión como admin** (usuario: admin, password: admin123)

3. **Ir a "Gestión de Profesores" → "Nuevo Profesor"**

4. **Verificar que aparece el selector de academias**

5. **Crear un profesor:**
   - Rellenar todos los campos requeridos
   - Seleccionar una academia del dropdown
   - Clic en "Crear Profesor"

6. **Verificar que:**
   - No aparece ningún error
   - Se muestra mensaje de éxito
   - El profesor aparece en la lista
   - La academia está correctamente asignada

7. **Verificar en la base de datos (opcional):**
   ```sql
   SELECT p.id, u.username, u.nombre, u.apellidos, a.nombre as academia
   FROM profesor p
   JOIN usuario u ON p.usuario_id = u.id
   LEFT JOIN academia a ON p.academia_id = a.id
   ORDER BY p.id DESC;
   ```

## Notas Importantes

- Los profesores ahora pueden crearse con o sin academia asignada (útil para admins que gestionan múltiples academias)
- Los usuarios no-ADMIN siguen creando profesores solo para su propia academia
- La lista de profesores muestra correctamente la academia asignada o "Sin academia" si no tiene
- Este cambio no afecta a profesores ya creados, solo mejora la creación de nuevos profesores

## Archivos Modificados

1. `src/main/java/es/fempa/acd/demosecurityproductos/controller/GestionProfesorController.java`
2. `src/main/resources/templates/admin/profesor-nuevo.html`
3. `src/main/resources/i18n/messages_es.properties`
4. `src/main/resources/i18n/messages_en.properties`
5. `src/main/resources/i18n/messages.properties`
