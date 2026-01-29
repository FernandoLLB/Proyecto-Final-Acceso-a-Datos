# Guía: Cómo Eliminar Matrículas para Borrar Cursos

## 🎯 Problema Resuelto

Ya puedes **cancelar matrículas** para poder eliminar cursos. La funcionalidad estaba implementada y ahora la he mejorado con un **enlace directo** desde el mensaje de error.

---

## 📋 Pasos para Eliminar un Curso con Matrículas

### Método 1: Usando el Enlace Directo (⭐ NUEVO - MÁS FÁCIL)

1. **Intentar eliminar el curso:**
   - Ve a **Gestión de Cursos**
   - Click en botón rojo **"Eliminar"** del curso

2. **Ver mensaje con enlace:**
   ```
   ⚠️ No se puede eliminar el curso porque tiene X matrícula(s) registrada(s). 
   Por favor, cancele las matrículas primero.
   
   👉 Click aquí para ver y cancelar las matrículas
   ```

3. **Click en el enlace:**
   - Te llevará directamente a la página de matrículas del curso
   - Verás la tabla con todas las matrículas

4. **Cancelar cada matrícula:**
   - Para cada matrícula **ACTIVA**:
     - Click en botón rojo **"Cancelar"**
     - Se abre un modal
     - (Opcional) Escribe un motivo: "Curso será eliminado"
     - Click en **"Cancelar Matrícula"**
     - ✅ Matrícula cancelada

5. **Volver a eliminar el curso:**
   - Click en **"Volver a Cursos"** (arriba)
   - Click en **"Eliminar"** del curso
   - ✅ **"Curso eliminado exitosamente"**

---

### Método 2: Navegación Manual

1. **Ve a Gestión de Cursos**
   - Menú lateral → "Cursos"

2. **Click en botón azul "Matrículas"**
   - Del curso que deseas eliminar

3. **Ver lista de matrículas**
   - Verás tabla con todos los alumnos matriculados
   - Estado: ACTIVA / COMPLETADA / CANCELADA

4. **Cancelar matrículas activas:**
   - Solo las **ACTIVAS** tienen botón "Cancelar"
   - Click en **"Cancelar"**
   - Escribir motivo (opcional)
   - Confirmar

5. **Volver y eliminar:**
   - Click en "Volver a Cursos"
   - Click en "Eliminar" del curso
   - ✅ Curso eliminado

---

## 🖥️ Ubicación de las Funciones

### Gestión de Cursos:
```
URL: http://localhost:8080/secretaria/cursos
Acceso: ADMIN y SECRETARIA
```

**Acciones disponibles por curso:**
- 🔵 **Matrículas** → Ver lista de alumnos matriculados
- 🟡 **Editar** → Modificar datos del curso
- 🟢 **Activar** / 🔘 **Desactivar** → Cambiar estado
- 🔴 **Eliminar** → Borrar curso (si no tiene matrículas)

### Gestión de Matrículas:
```
URL: http://localhost:8080/secretaria/matriculas/curso/{id}
Acceso: ADMIN y SECRETARIA (⭐ AHORA AMBOS)
```

**Acciones disponibles por matrícula:**
- ✅ **Completar** → Marcar como completada (solo ACTIVAS)
- ❌ **Cancelar** → Cancelar la matrícula (solo ACTIVAS)

---

## 📊 Estados de Matrículas

| Estado | Significado | ¿Puedo cancelarla? | ¿Impide eliminar curso? |
|--------|-------------|-------------------|------------------------|
| 🟢 **ACTIVA** | Alumno cursando actualmente | ✅ Sí | ✅ Sí |
| 🔵 **COMPLETADA** | Alumno terminó el curso | ❌ No | ✅ Sí |
| 🔴 **CANCELADA** | Matrícula cancelada | ❌ No | ✅ Sí |

**IMPORTANTE:** **TODAS** las matrículas (activas, completadas y canceladas) impiden eliminar el curso. Esto es por diseño para mantener la integridad de los datos históricos.

### Solución para Matrículas Completadas/Canceladas:

Si el curso tiene matrículas completadas o canceladas y quieres eliminarlo, tienes dos opciones:

**Opción 1: Desactivar en lugar de Eliminar (RECOMENDADO)**
- Click en "Desactivar" en lugar de "Eliminar"
- El curso no aparecerá como activo
- Se conserva el historial completo
- Puedes reactivarlo después si es necesario

**Opción 2: Eliminación en Base de Datos (Solo Administrador)**
- Requiere acceso directo a la base de datos
- Ejecutar: `DELETE FROM matricula WHERE curso_id = X;`
- Luego eliminar el curso desde la interfaz

---

## 🎓 Ejemplo Completo

**Escenario:** Quieres eliminar el curso "Java Básico" que tiene 3 matrículas

```
1. Vas a Gestión de Cursos
   📋 Ves: "Java Básico" - Juan (Profesor)

2. Click en "Eliminar"
   ⚠️ "No se puede eliminar... tiene 3 matrícula(s)"
   👉 "Click aquí para ver y cancelar las matrículas" (NUEVO)

3. Click en el enlace
   📋 Vas a: /secretaria/matriculas/curso/5
   Ves tabla:
   - Pedro García (ACTIVA) → Botón "Cancelar" ✅
   - María López (ACTIVA) → Botón "Cancelar" ✅
   - Juan Pérez (COMPLETADA) → Sin botón ❌

4. Cancelas Pedro:
   - Click "Cancelar"
   - Motivo: "Curso será eliminado"
   - Confirmar
   ✅ "Matrícula cancelada"

5. Cancelas María:
   - Click "Cancelar"
   - Motivo: "Curso será eliminado"
   - Confirmar
   ✅ "Matrícula cancelada"

6. Juan tiene matrícula COMPLETADA
   ⚠️ No puedes cancelarla desde la interfaz
   
   OPCIÓN A: Desactivar el curso en lugar de eliminarlo
   OPCIÓN B: Contactar administrador de BD

7. Si solo hubiera ACTIVAS (las cancelaste todas):
   - Click "Volver a Cursos"
   - Click "Eliminar" en "Java Básico"
   ✅ "Curso eliminado exitosamente"
```

---

## 🆕 Mejoras Implementadas

### 1. **Enlace Directo en Mensaje de Error** ⭐
- Cuando intentas eliminar un curso con matrículas
- El mensaje ahora incluye un **enlace clickeable**
- Te lleva directo a la página de matrículas del curso

### 2. **Acceso para ADMIN**
- Antes: Solo SECRETARIA podía gestionar matrículas
- Ahora: ADMIN también puede cancelar matrículas
- Facilita el proceso de limpieza de datos

### 3. **Excepción Personalizada**
- Nueva clase: `CursoConMatriculasException`
- Incluye ID del curso en la excepción
- Permite crear enlaces dinámicos

---

## 🔧 Cambios Técnicos Realizados

### Backend:
1. ✅ **CursoConMatriculasException.java** (NUEVO)
   - Excepción personalizada con ID de curso
   
2. ✅ **CursoService.java**
   - Usa nueva excepción en método `eliminar()`
   
3. ✅ **CursoController.java**
   - Captura excepción y crea mensaje con enlace HTML
   - Nuevo atributo: `errorHtml`
   
4. ✅ **MatriculaController.java**
   - Cambiado a: `@PreAuthorize("hasAnyRole('ADMIN', 'SECRETARIA')")`

### Frontend:
5. ✅ **cursos-lista.html**
   - Soporte para mensajes con HTML (`th:utext`)
   - Muestra enlaces clickeables en errores

---

## 🚀 Cómo Usar Ahora

1. **Iniciar aplicación:**
   ```bash
   mvn spring-boot:run
   ```

2. **Acceder:**
   ```
   http://localhost:8080/login
   ```

3. **Probar el nuevo flujo:**
   - Ir a Gestión de Cursos
   - Intentar eliminar curso con matrículas
   - **Ver el nuevo enlace clickeable** en el mensaje
   - Seguir el enlace para cancelar matrículas

---

## ⚠️ Notas Importantes

### ¿Por qué no puedo eliminar un curso con matrículas?
- **Integridad de datos:** Las matrículas registran información histórica importante
- **Auditoría:** Se debe mantener el registro de quién estuvo matriculado
- **Mejores prácticas:** En sistemas educativos, nunca se eliminan registros académicos

### ¿Debo eliminar o desactivar?
**Recomendación:** **Desactivar** en lugar de eliminar

| Acción | Cuándo usarla | Ventajas |
|--------|---------------|----------|
| **Desactivar** | Curso que ya tuvo actividad | Conserva historial, reversible |
| **Eliminar** | Curso creado por error, sin matrículas | Limpia la base de datos |

---

## 📚 Documentación Relacionada

- `IMPLEMENTACION_ELIMINACION_CURSOS.md` - Funcionalidad de eliminar cursos
- `SOLUCION_ERROR_ELIMINACION_PROFESORES.md` - Eliminar profesores con cursos

---

## ✅ Estado Final

- ✅ **Funcionalidad implementada y funcionando**
- ✅ **Enlace directo desde mensaje de error**
- ✅ **Acceso para ADMIN agregado**
- ✅ **Compilación exitosa**
- ✅ **Listo para usar**

---

**Fecha:** 29 de enero de 2026  
**Estado:** ✅ Completado y mejorado
