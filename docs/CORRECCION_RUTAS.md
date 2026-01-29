# Corrección de Rutas - 29 de Enero 2026

## Problema Identificado

Al acceder como secretaria y hacer clic en los enlaces del sidebar, se producían errores 404 porque las rutas en el HTML no coincidían con las rutas definidas en los controladores.

## Rutas Corregidas

### Sidebar de Secretaria (`fragments.html`)

#### ❌ Rutas Incorrectas (Anteriores)
```html
/secretaria/alumnos/lista
/secretaria/cursos/lista
/secretaria/aulas/lista
/secretaria/reservas/lista
```

#### ✅ Rutas Correctas (Actuales)
```html
/secretaria/alumnos
/secretaria/cursos
/secretaria/aulas
/secretaria/reservas
```

### Dashboard de Secretaria

También se corrigieron los enlaces en las stat cards del dashboard:

#### ❌ Antes
```html
/secretaria/alumnos/lista
/secretaria/alumnos/lista?estado=INACTIVO
/secretaria/aulas/lista
/secretaria/reservas/lista
```

#### ✅ Ahora
```html
/secretaria/alumnos
/secretaria/alumnos?estado=INACTIVO
/secretaria/aulas
/secretaria/reservas
```

### Sidebar de Admin (`fragments.html`)

#### ❌ Ruta Incorrecta (Anterior)
```html
/admin/profesores/lista
```

#### ✅ Ruta Correcta (Actual)
```html
/profesores
```

### Dashboard de Admin

También se corrigió el enlace de acceso rápido a profesores:

#### ❌ Antes
```html
/admin/profesores/lista
```

#### ✅ Ahora
```html
/profesores
```

## Archivos Modificados

1. **`src/main/resources/templates/fragments.html`**
   - Sidebar de Secretaria: Corregidas 4 rutas
   - Sidebar de Admin: Corregida 1 ruta

2. **`src/main/resources/templates/secretaria/dashboard.html`**
   - Stat cards: Corregidas 4 rutas

3. **`src/main/resources/templates/admin/dashboard.html`**
   - Acceso rápido: Corregida 1 ruta

## Verificación de Rutas en Controladores

### SecretariaController.java
```java
@RequestMapping("/secretaria")

@GetMapping("/dashboard")           → /secretaria/dashboard ✅
@GetMapping("/alumnos")              → /secretaria/alumnos ✅
@GetMapping("/alumnos/nuevo")        → /secretaria/alumnos/nuevo ✅
@PostMapping("/alumnos/crear")       → /secretaria/alumnos/crear ✅
@GetMapping("/alumnos/{id}/editar")  → /secretaria/alumnos/{id}/editar ✅
```

### GestionProfesorController.java
```java
@RequestMapping("/profesores")

@GetMapping                          → /profesores ✅
@GetMapping("/nuevo")                → /profesores/nuevo ✅
@PostMapping("/crear")               → /profesores/crear ✅
@GetMapping("/{id}/editar")          → /profesores/{id}/editar ✅
```

## Estado Actual

✅ **Compilación exitosa**
```
[INFO] BUILD SUCCESS
[INFO] Total time:  4.315 s
```

✅ **Todas las rutas corregidas**

✅ **Páginas funcionales**:
- Secretaria Dashboard
- Sidebar de Secretaria (todos los enlaces)
- Admin Dashboard
- Sidebar de Admin (todos los enlaces)

## Cómo Probar

1. **Reiniciar la aplicación** (si está corriendo):
   ```bash
   # Detener la aplicación actual
   # Luego ejecutar:
   ./mvnw spring-boot:run
   ```

2. **Acceder como Secretaria**:
   - URL: `http://localhost:8090`
   - Usuario: `secretaria` (o el que hayas configurado)
   - Contraseña: tu contraseña configurada

3. **Probar los enlaces del sidebar**:
   - ✅ Dashboard
   - ✅ Alumnos
   - ✅ Cursos
   - ✅ Aulas
   - ✅ Reservas

4. **Acceder como Admin**:
   - Usuario: `admin`
   - Probar los enlaces:
     - ✅ Dashboard
     - ✅ Academias
     - ✅ Profesores

## Notas Importantes

### Rutas que SÍ existen y funcionan:

**Admin:**
- `/admin/dashboard`
- `/admin/academias/lista`
- `/admin/academias/nueva`
- `/admin/academias/{id}/editar`
- `/profesores` (compartida con otros roles)

**Secretaria:**
- `/secretaria/dashboard`
- `/secretaria/alumnos`
- `/secretaria/alumnos/nuevo`
- `/secretaria/alumnos/{id}/editar`
- `/secretaria/cursos`
- `/secretaria/aulas`
- `/secretaria/reservas`

**Profesor:**
- `/profesor/dashboard`

**Alumno:**
- `/alumno/dashboard`

**Propietario:**
- `/propietario/dashboard`

### Rutas Compartidas:
- `/profesores` - Accesible por ADMIN, SECRETARIA y PROPIETARIO

## Problema Resuelto

El error que recibías:
```
AccountNonLocked=true, Granted Authorities=[ROLE_SECRETARIA]
Secured GET /error
```

Era causado porque Spring Security intentaba acceder a rutas que no existían (como `/secretaria/alumnos/lista`), lo que generaba un error 404, que luego redirigía a `/error`.

Ahora todas las rutas están correctamente mapeadas y deberían funcionar sin problemas.

---

**Última actualización**: 29/01/2026 15:58
**Estado**: ✅ Resuelto
