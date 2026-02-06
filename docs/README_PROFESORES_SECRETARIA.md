# 🎓 Gestión de Profesores - Módulo Secretaria

## 📖 Descripción

Este módulo permite a las **secretarias** gestionar completamente a los **profesores** de su academia, incluyendo crear, editar, desactivar y reactivar profesores.

---

## ✨ Características Principales

- ✅ **CRUD Completo**: Crear, Leer, Actualizar, Desactivar/Reactivar
- 🔒 **Seguro**: Solo acceso a profesores de su propia academia
- 🎯 **Filtros**: Ver solo activos o todos los profesores
- 📊 **Vista de tabla**: Información completa y organizada
- ♻️ **No destructivo**: Desactivación en lugar de eliminación
- 🎨 **UI Moderna**: Diseño consistente con el resto de la aplicación

---

## 🗂️ Estructura de Archivos

```
📁 Gestor de Academias AD/
├─ 📁 src/main/java/es/fempa/acd/demosecurityproductos/
│  └─ 📁 controller/
│     └─ 📄 SecretariaGestionProfesorController.java  ⭐ NUEVO
│
├─ 📁 src/main/resources/templates/
│  ├─ 📄 fragments.html  ✏️ MODIFICADO (sidebar)
│  └─ 📁 secretaria/
│     ├─ 📄 profesores-lista.html  ⭐ NUEVO
│     ├─ 📄 profesor-nuevo.html    ⭐ NUEVO
│     └─ 📄 profesor-editar.html   ⭐ NUEVO
│
└─ 📁 docs/
   ├─ 📄 IMPLEMENTACION_PROFESORES_SECRETARIA.md  ⭐ NUEVO
   └─ 📄 PRUEBAS_PROFESORES_SECRETARIA.md         ⭐ NUEVO
```

---

## 🚀 Cómo Usar

### 1. Acceso al Módulo

1. Iniciar sesión como **secretaria**
2. En el menú lateral, hacer clic en **"Profesores"**
3. Se abrirá la lista de profesores de la academia

### 2. Crear un Profesor

1. Click en **"Nuevo Profesor"**
2. Completar formulario:
   - Datos de usuario (username, email, contraseña)
   - Datos personales (nombre, apellidos)
   - Datos profesionales (especialidad, biografía) - opcional
3. Click en **"Crear Profesor"**

### 3. Editar un Profesor

1. En la lista, click en **"Editar"** del profesor deseado
2. Modificar los campos necesarios
3. Click en **"Actualizar Profesor"**

### 4. Desactivar un Profesor

1. En la lista, click en **"Desactivar"**
2. Confirmar la acción
3. El profesor no podrá iniciar sesión

**Nota:** No se puede desactivar si tiene cursos asignados.

### 5. Reactivar un Profesor

1. Cambiar filtro a **"Todos"**
2. Click en **"Reactivar"** en un profesor inactivo
3. El profesor podrá volver a iniciar sesión

---

## 🔐 Seguridad y Permisos

### Requisitos de Acceso
- ✅ Usuario autenticado
- ✅ Rol: `SECRETARIA`
- ✅ Academia asignada

### Restricciones
- ❌ No puede ver profesores de otras academias
- ❌ No puede editar profesores de otras academias
- ❌ No puede cambiar el profesor a otra academia
- ❌ No puede eliminar profesores (solo desactivar)
- ❌ No puede desactivar si tiene cursos asignados

---

## 📋 Validaciones

### Al Crear Profesor
| Campo | Validación | Mensaje de Error |
|-------|-----------|------------------|
| Username | Obligatorio, único | "Usuario ya existe" |
| Email | Obligatorio, único, formato email | "Email ya existe" / "Email inválido" |
| Contraseña | Obligatorio, mín. 6 caracteres | "Mínimo 6 caracteres" |
| Nombre | Obligatorio | "Campo obligatorio" |
| Apellidos | Obligatorio | "Campo obligatorio" |
| Especialidad | Opcional | - |
| Biografía | Opcional | - |

### Al Editar Profesor
- ✅ Email debe ser único (si se cambia)
- ✅ No se puede modificar username
- ✅ No se puede modificar fecha de contratación
- ✅ No se puede cambiar de academia

### Al Desactivar Profesor
- ✅ No debe tener cursos asignados
- ✅ Si tiene cursos, mostrar error con cantidad

---

## 🎯 URLs y Endpoints

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/secretaria/profesores` | Lista de profesores |
| GET | `/secretaria/profesores?soloActivos=true` | Solo activos |
| GET | `/secretaria/profesores?soloActivos=false` | Todos |
| GET | `/secretaria/profesores/nuevo` | Formulario crear |
| POST | `/secretaria/profesores/crear` | Crear profesor |
| GET | `/secretaria/profesores/{id}/editar` | Formulario editar |
| POST | `/secretaria/profesores/{id}/actualizar` | Actualizar |
| POST | `/secretaria/profesores/{id}/eliminar` | Desactivar |
| POST | `/secretaria/profesores/{id}/reactivar` | Reactivar |

---

## 📊 Datos Automáticos

Al crear un profesor, se establecen automáticamente:

- **Academia**: La academia de la secretaria
- **Fecha de contratación**: Fecha actual
- **Email verificado**: `true` (verificado automáticamente)
- **Usuario activo**: `true`
- **Rol**: `PROFESOR`

---

## 🎨 Elementos de la Interfaz

### Lista de Profesores
- **Tabla**: ID, Nombre, Email, Usuario, Especialidad, Fecha, Estado
- **Filtros**: Solo Activos / Todos
- **Badges**: Verde (Activo), Rojo (Inactivo)
- **Botones**: Editar (azul), Desactivar (amarillo), Reactivar (verde)

### Formulario Crear/Editar
- **Secciones**: Datos de Usuario, Personales, Profesionales
- **Validación**: Campos obligatorios marcados con *
- **Ayuda**: Tooltips y mensajes informativos
- **Botones**: Cancelar, Guardar

---

## 📱 Responsive Design

- ✅ Desktop: Vista completa de tabla
- ✅ Tablet: Tabla adaptada
- ✅ Móvil: Tarjetas en lugar de tabla (si implementado)

---

## 🔧 Dependencias

### Backend
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf

### Frontend
- Bootstrap 5
- Bootstrap Icons
- CSS personalizado

---

## 📝 Documentación Adicional

1. **Implementación Completa**
   - Archivo: `docs/IMPLEMENTACION_PROFESORES_SECRETARIA.md`
   - Contenido: Detalles técnicos, estructura, flujos

2. **Guía de Pruebas**
   - Archivo: `docs/PRUEBAS_PROFESORES_SECRETARIA.md`
   - Contenido: Casos de prueba, checklist, validaciones

---

## 🐛 Solución de Problemas

### Problema: No aparece el enlace "Profesores" en el menú
**Solución:** 
- Verificar que el usuario tiene rol `SECRETARIA`
- Limpiar caché del navegador
- Reiniciar la aplicación

### Problema: Error al crear profesor - "Usuario ya existe"
**Solución:**
- El username ya está en uso por otro usuario
- Elegir un username diferente

### Problema: Error al crear profesor - "Email ya existe"
**Solución:**
- El email ya está registrado en el sistema
- Usar un email diferente

### Problema: No puedo desactivar un profesor
**Solución:**
- Verificar que no tenga cursos asignados
- Primero reasignar o eliminar los cursos
- Luego intentar desactivar nuevamente

### Problema: No veo todos los profesores
**Solución:**
- Solo se muestran profesores de tu academia
- Cambiar filtro a "Todos" para ver inactivos

---

## 🔄 Comparación: Propietario vs Secretaria

| Característica | Propietario | Secretaria |
|----------------|-------------|------------|
| Ver profesores | Múltiples academias | Solo su academia |
| Crear profesores | ✅ Sí | ✅ Sí |
| Editar profesores | ✅ Sí | ✅ Sí |
| Desactivar profesores | ✅ Sí | ✅ Sí |
| Cambiar de academia | ✅ Sí | ❌ No |
| Selector academia | ✅ Sí | ❌ No |
| Filtrar por academia | ✅ Sí | ❌ N/A |

---

## 📈 Próximas Mejoras Sugeridas

- [ ] Vista de detalle del profesor (read-only)
- [ ] Búsqueda por nombre/email/especialidad
- [ ] Exportar lista a PDF/Excel
- [ ] Estadísticas de profesores en dashboard
- [ ] Historial de cambios
- [ ] Asignación masiva de profesores a cursos
- [ ] Notificaciones por email al crear profesor
- [ ] Foto de perfil del profesor
- [ ] CV adjunto del profesor

---

## 👥 Roles y Permisos

### SECRETARIA
- ✅ Ver profesores de su academia
- ✅ Crear nuevos profesores
- ✅ Editar profesores existentes
- ✅ Desactivar profesores (sin cursos)
- ✅ Reactivar profesores

### PROPIETARIO
- ✅ Todo lo anterior
- ✅ Cambiar profesor entre sus academias
- ✅ Ver profesores de todas sus academias

### ADMIN
- ✅ Todo sin restricciones

---

## 📞 Soporte

Para más información o soporte:

1. **Documentación**: Revisar carpeta `/docs`
2. **Logs**: Archivo `logs/application.log`
3. **Consola**: Logs en tiempo real al ejecutar la app
4. **Issues**: Crear issue en el repositorio

---

## 📄 Licencia

Este módulo es parte del **Sistema de Gestión de Academias**.  
Ver archivo `LICENSE` en la raíz del proyecto.

---

## 🙏 Créditos

**Desarrollado por:** Sistema de Gestión de Academias  
**Fecha:** 6 de febrero de 2026  
**Versión:** 1.0  

---

## ✅ Estado del Módulo

```
🟢 COMPLETADO Y FUNCIONAL
```

**Última actualización:** 6 de febrero de 2026
