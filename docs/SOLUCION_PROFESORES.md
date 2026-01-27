# Solución: Profesores no aparecen en el formulario de creación de cursos

## Problema identificado

Al intentar crear un nuevo curso desde el rol SECRETARIA, el desplegable de profesores aparecía vacío. 

**Causa raíz:** No existían profesores creados en la base de datos para esa academia, y además, no había una interfaz para gestionar profesores.

## Solución implementada

### 1. Sistema de Gestión de Profesores

Se ha creado un CRUD completo para gestionar profesores accesible por los roles:
- **ADMIN**: Acceso global
- **PROPIETARIO**: Gestión de su academia
- **SECRETARIA**: Gestión de su academia

#### Controlador creado
- **Archivo:** `GestionProfesorController.java`
- **Rutas:**
  - `GET /admin/profesores` - Lista de profesores
  - `GET /admin/profesores/nuevo` - Formulario nuevo profesor
  - `POST /admin/profesores/crear` - Crear profesor
  - `GET /admin/profesores/{id}/editar` - Formulario editar
  - `POST /admin/profesores/{id}/actualizar` - Actualizar profesor
  - `POST /admin/profesores/{id}/eliminar` - Eliminar profesor

#### Vistas creadas
1. **admin/profesores-lista.html** - Listado de profesores con opciones de editar/eliminar
2. **admin/profesor-nuevo.html** - Formulario para crear nuevo profesor
3. **admin/profesor-editar.html** - Formulario para editar profesor existente

### 2. Mejoras en el formulario de cursos

Se ha mejorado `curso-nuevo.html` para mostrar claramente cuando no hay profesores disponibles:
- Mensaje de advertencia visible si la lista está vacía
- Contador de profesores disponibles
- Indicación de que primero deben crearse profesores

### 3. Logs de debug

Se han añadido logs en el método `nuevoCursoForm` del `CursoController` para facilitar el diagnóstico:
- Muestra el ID de la academia
- Cuenta el número de profesores encontrados
- Lista los profesores disponibles en consola

## Pasos para usar la solución

### 1. Acceder a la gestión de profesores

**Desde el rol ADMIN:**
1. Iniciar sesión con usuario `admin` / `password123`
2. En el dashboard, hacer clic en "Profesores" en el menú lateral
3. O acceder directamente a: http://localhost:8080/admin/profesores

**Desde el rol SECRETARIA:**
1. Iniciar sesión con tu usuario de secretaria
2. En el dashboard, hacer clic en "Gestión de Profesores" en el menú lateral
3. O acceder directamente a: http://localhost:8080/admin/profesores

### 2. Crear un nuevo profesor

1. En la página de profesores, hacer clic en "Nuevo Profesor"
2. Rellenar el formulario:

   **Datos de Usuario:**
   - Usuario: nombre único (ej: profesor1)
   - Email: correo único (ej: profesor1@academia.com)
   - Contraseña: mínimo 6 caracteres

   **Datos Personales:**
   - Nombre: (ej: Juan)
   - Apellidos: (ej: Martínez García)

   **Datos Profesionales:**
   - Especialidad: (ej: Programación Web)
   - Biografía: (opcional)

3. Hacer clic en "Crear Profesor"

### 3. Crear un curso con el profesor

1. Ir a "Gestión de Cursos" desde el dashboard de secretaria
2. Hacer clic en "Nuevo Curso"
3. Ahora el desplegable de "Profesor" mostrará los profesores disponibles
4. Seleccionar el profesor deseado y completar el formulario
5. Hacer clic en "Crear Curso"

## Verificación

Para verificar que todo funciona correctamente:

1. **Ver logs en consola:**
   Al acceder al formulario de nuevo curso, verás en la consola:
   ```
   === DEBUG: Formulario Nuevo Curso ===
   Academia ID: 1
   Número de profesores encontrados: 2
   Lista de profesores:
     - ID: 1, Nombre: Juan Martínez, Academia: 1
     - ID: 2, Nombre: Ana Fernández, Academia: 1
   ```

2. **Ver contador en formulario:**
   En el campo "Profesor" verás:
   ```
   Total de profesores disponibles: 2
   ```

3. **Si no hay profesores:**
   Verás un mensaje de advertencia:
   ```
   ⚠️ No hay profesores disponibles.
   Primero debe crear profesores para la academia antes de poder crear cursos.
   ```

## Estructura de archivos creados/modificados

```
src/main/java/es/fempa/acd/demosecurityproductos/
└── controller/
    ├── GestionProfesorController.java (NUEVO)
    └── CursoController.java (MODIFICADO - añadidos logs)

src/main/resources/templates/
├── admin/
│   ├── dashboard.html (MODIFICADO - añadido enlace profesores)
│   ├── profesores-lista.html (NUEVO)
│   ├── profesor-nuevo.html (NUEVO)
│   └── profesor-editar.html (NUEVO)
└── secretaria/
    ├── dashboard.html (MODIFICADO - añadido enlace profesores)
    └── curso-nuevo.html (MODIFICADO - añadido mensaje y contador)
```

## Notas importantes

1. **Relación Academia-Profesor:** Cada profesor se vincula automáticamente a la academia del usuario que lo crea
2. **Validaciones:** El sistema valida que username y email sean únicos
3. **Seguridad:** Solo usuarios con roles ADMIN, PROPIETARIO o SECRETARIA pueden gestionar profesores
4. **Datos aislados:** Cada academia solo ve sus propios profesores

## Próximos pasos recomendados

1. Crear al menos 2-3 profesores para tu academia
2. Verificar que aparecen en el desplegable de cursos
3. Crear cursos y asignarlos a diferentes profesores
4. Opcionalmente, añadir más funcionalidades como:
   - Filtros en la lista de profesores
   - Vista de cursos asignados por profesor
   - Desactivar profesores en lugar de eliminarlos

## Soporte

Si el problema persiste después de crear profesores:
1. Verificar los logs en consola al acceder al formulario de nuevo curso
2. Comprobar que el usuario secretaria pertenece a una academia válida
3. Verificar en la base de datos que los profesores tienen `academia_id` correcto

---
**Versión:** 1.0  
**Fecha:** 27/01/2026  
**Estado:** Implementado y probado
