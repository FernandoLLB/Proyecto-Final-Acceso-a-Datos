# 🧪 Guía de Pruebas: Gestión de Profesores por Secretarias

## 📋 Lista de Verificación

### ✅ Prerrequisitos
- [ ] Aplicación Spring Boot en ejecución
- [ ] Usuario con rol `SECRETARIA` creado
- [ ] Secretaria asignada a una academia
- [ ] Acceso al navegador web

---

## 🚀 Inicio de Sesión

1. Acceder a: `http://localhost:8080/login`
2. Ingresar credenciales de una secretaria
3. Verificar que redirige al dashboard de secretaria

---

## 📝 Prueba 1: Acceso al Módulo de Profesores

### Pasos:
1. Desde el dashboard de secretaria
2. Mirar el menú lateral (sidebar)
3. Buscar el enlace "Profesores" con icono de persona con credencial
4. Hacer clic en "Profesores"

### Resultado Esperado:
✅ Redirige a `/secretaria/profesores`  
✅ Muestra el título "Gestión de Profesores"  
✅ Muestra botón "Nuevo Profesor"  
✅ Si no hay profesores, muestra mensaje informativo  
✅ Si hay profesores, muestra tabla con datos  

---

## 📝 Prueba 2: Crear un Nuevo Profesor

### Pasos:
1. En la lista de profesores, hacer clic en "Nuevo Profesor"
2. Completar el formulario:
   ```
   Usuario: profesor_prueba_001
   Contraseña: test123
   Email: profesor001@test.com
   Nombre: Juan
   Apellidos: Martínez García
   Especialidad: Programación Web
   Biografía: Experto en desarrollo full-stack
   ```
3. Hacer clic en "Crear Profesor"

### Resultado Esperado:
✅ Redirige a `/secretaria/profesores`  
✅ Muestra mensaje: "Profesor creado exitosamente"  
✅ El profesor aparece en la tabla  
✅ Estado muestra "Activo" con badge verde  
✅ Email verificado automáticamente  

### Validaciones a Probar:
- [ ] Intentar crear con username duplicado → Error
- [ ] Intentar crear con email duplicado → Error
- [ ] Intentar crear sin campos obligatorios → Error
- [ ] Crear solo con campos obligatorios → OK

---

## 📝 Prueba 3: Filtrar Profesores

### Pasos:
1. En la lista de profesores
2. Verificar que el filtro "Solo Activos" está activo por defecto
3. Hacer clic en "Todos"
4. Verificar que muestra también los desactivados

### Resultado Esperado:
✅ "Solo Activos" muestra solo profesores activos  
✅ "Todos" muestra activos e inactivos  
✅ El filtro activo se marca visualmente  
✅ La URL cambia: `?soloActivos=true` o `?soloActivos=false`  

---

## 📝 Prueba 4: Editar un Profesor

### Pasos:
1. En la lista, hacer clic en "Editar" de un profesor
2. Modificar datos:
   ```
   Nombre: Juan Carlos (cambiar)
   Especialidad: Desarrollo Full-Stack (cambiar)
   ```
3. Hacer clic en "Actualizar Profesor"

### Resultado Esperado:
✅ Redirige a `/secretaria/profesores`  
✅ Muestra mensaje: "Profesor actualizado exitosamente"  
✅ Los cambios se reflejan en la tabla  
✅ El username no se puede cambiar (solo lectura)  
✅ La fecha de contratación no se puede cambiar  

---

## 📝 Prueba 5: Desactivar un Profesor

### Pasos:
1. En la lista, buscar un profesor activo SIN cursos asignados
2. Hacer clic en "Desactivar"
3. Confirmar en el diálogo

### Resultado Esperado:
✅ Muestra mensaje: "Profesor desactivado exitosamente"  
✅ El estado cambia a "Inactivo" con badge rojo  
✅ El botón cambia a "Reactivar"  
✅ Con filtro "Solo Activos" ya no aparece  

### Caso de Error (Con Cursos):
1. Intentar desactivar un profesor con cursos asignados
2. Debe mostrar error: "No se puede eliminar el profesor porque tiene X curso(s) asignado(s)"

---

## 📝 Prueba 6: Reactivar un Profesor

### Pasos:
1. Cambiar filtro a "Todos"
2. Buscar un profesor inactivo
3. Hacer clic en "Reactivar"
4. Confirmar en el diálogo

### Resultado Esperado:
✅ Muestra mensaje: "Profesor reactivado exitosamente"  
✅ El estado cambia a "Activo" con badge verde  
✅ El botón cambia a "Desactivar"  
✅ Aparece en el filtro "Solo Activos"  

---

## 📝 Prueba 7: Validaciones de Seguridad

### Intentos Maliciosos (Deben FALLAR):

#### A) Acceder a profesor de otra academia:
```
URL directa: /secretaria/profesores/999/editar
(donde 999 es un profesor de otra academia)
```
**Esperado:** ❌ Error "No tienes permisos para editar este profesor"

#### B) Acceder sin autenticación:
```
Cerrar sesión e intentar: /secretaria/profesores
```
**Esperado:** ❌ Redirige a login

#### C) Acceder con rol incorrecto:
```
Iniciar sesión como ALUMNO o PROFESOR
Intentar: /secretaria/profesores
```
**Esperado:** ❌ Acceso denegado (403)

---

## 📝 Prueba 8: Navegación y UI

### Verificar:
- [ ] El menú lateral marca "Profesores" como activo
- [ ] Los breadcrumbs funcionan correctamente
- [ ] Los botones tienen iconos apropiados
- [ ] Los colores de badges son correctos (verde=activo, rojo=inactivo)
- [ ] Las tablas son responsive
- [ ] Los formularios tienen placeholders
- [ ] Los mensajes de ayuda se muestran
- [ ] Los tooltips funcionan (si existen)

---

## 📝 Prueba 9: Datos en la Tabla

### Verificar columnas:
- [ ] ID - Número del profesor
- [ ] Nombre Completo - Con badge si está desactivado
- [ ] Email - Email del usuario
- [ ] Usuario - Username del login
- [ ] Especialidad - O "Sin especialidad" si está vacío
- [ ] Fecha Contratación - Formato dd/MM/yyyy
- [ ] Estado - Badge verde (Activo) o rojo (Inactivo)
- [ ] Acciones - Botones Editar y Desactivar/Reactivar

---

## 📝 Prueba 10: Mensajes y Feedback

### Tipos de mensajes a verificar:

#### ✅ Éxito (Verde):
- "Profesor creado exitosamente"
- "Profesor actualizado exitosamente"
- "Profesor desactivado exitosamente"
- "Profesor reactivado exitosamente"

#### ❌ Error (Rojo):
- "No se pudo identificar la academia"
- "Usuario ya existe"
- "Email ya existe"
- "No tienes permisos para editar este profesor"
- "Profesor no encontrado"
- "No se puede eliminar el profesor porque tiene X curso(s) asignado(s)"

---

## 🎯 Checklist Final

### Funcionalidades Core:
- [ ] Listar profesores
- [ ] Crear profesor nuevo
- [ ] Editar profesor existente
- [ ] Desactivar profesor
- [ ] Reactivar profesor
- [ ] Filtrar por estado

### Seguridad:
- [ ] Solo secretarias tienen acceso
- [ ] Solo ve profesores de su academia
- [ ] No puede acceder a profesores de otras academias
- [ ] Validación de permisos en cada acción

### Validaciones:
- [ ] Username único
- [ ] Email único
- [ ] Campos obligatorios
- [ ] No eliminar si tiene cursos
- [ ] Contraseña mínimo 6 caracteres

### UI/UX:
- [ ] Sidebar actualizado
- [ ] Navegación fluida
- [ ] Mensajes claros
- [ ] Responsive design
- [ ] Iconos apropiados
- [ ] Colores consistentes

---

## 📊 Tabla de Resultados

| # | Prueba | Estado | Notas |
|---|--------|--------|-------|
| 1 | Acceso al módulo | ⬜ | |
| 2 | Crear profesor | ⬜ | |
| 3 | Filtrar profesores | ⬜ | |
| 4 | Editar profesor | ⬜ | |
| 5 | Desactivar profesor | ⬜ | |
| 6 | Reactivar profesor | ⬜ | |
| 7 | Validaciones seguridad | ⬜ | |
| 8 | Navegación y UI | ⬜ | |
| 9 | Datos en tabla | ⬜ | |
| 10 | Mensajes y feedback | ⬜ | |

**Leyenda:**  
⬜ Pendiente | ✅ Pasó | ❌ Falló | ⚠️ Con observaciones

---

## 🐛 Registro de Bugs (Si se encuentran)

### Bug Template:
```
**ID:** BUG-001
**Título:** [Descripción breve]
**Pasos para reproducir:**
1. 
2. 
3. 

**Resultado esperado:**

**Resultado actual:**

**Severidad:** Alta/Media/Baja
**Estado:** Abierto/En progreso/Resuelto
```

---

## 💡 Consejos para las Pruebas

1. **Probar casos límite**: Username muy largo, caracteres especiales, etc.
2. **Probar con diferentes navegadores**: Chrome, Firefox, Edge
3. **Probar en móvil**: Responsive design
4. **Revisar la consola del navegador**: Errores JavaScript
5. **Revisar logs del servidor**: Errores backend
6. **Probar con datos reales**: Acentos, ñ, caracteres especiales
7. **Verificar transacciones**: Si falla algo, no debe quedar a medias

---

## 📞 Contacto para Reportar Issues

- Revisar logs en: `logs/application.log`
- Consola del navegador (F12)
- Documentación: `docs/IMPLEMENTACION_PROFESORES_SECRETARIA.md`

---

**Fecha de creación**: 6 de febrero de 2026  
**Versión**: 1.0  
**Responsable de pruebas**: [Tu nombre aquí]
