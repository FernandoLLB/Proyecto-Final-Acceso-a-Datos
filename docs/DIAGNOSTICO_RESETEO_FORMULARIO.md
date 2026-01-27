# 🔍 DIAGNÓSTICO: Formulario se Resetea sin Crear

## ✅ Cambios Aplicados

He agregado **logging detallado** en el controlador y servicio para ver exactamente qué está pasando.

### Archivos Modificados:
1. ✅ `CursoController.java` - Logging extensivo agregado
2. ✅ `CursoService.java` - Logging en el método crear()
3. ✅ `curso-nuevo.html` - Eliminado campo hidden innecesario

### ¿Qué se Eliminó?
El campo `<input type="hidden" name="academiaId">` que NO era necesario y podía causar problemas.

## 🚀 INSTRUCCIONES PARA DIAGNÓSTICO

### PASO 1: Ejecutar la Aplicación

Abre PowerShell/CMD y ejecuta:
```bash
cd "C:\Users\USUARIO\Desktop\Gestor de Academias AD"
mvn spring-boot:run
```

### PASO 2: Esperar que Inicie

Espera a ver este mensaje en consola:
```
Started DemoSecurityProductosApplication in X.XXX seconds
```

### PASO 3: Ir al Formulario

1. Abrir navegador: `http://localhost:8080`
2. Login como SECRETARIA
3. Ir a: **Cursos** → **Nuevo Curso**

### PASO 4: Completar el Formulario

Rellena TODOS los campos obligatorios:
- **Nombre:** Curso de Prueba Debug
- **Profesor:** (selecciona uno)
- **Duración:** 40
- **Fecha Inicio:** 2026-02-01
- **Fecha Fin:** 2026-03-30

### PASO 5: Enviar y Observar la Consola

**ANTES de hacer clic en "Crear Curso"**, asegúrate de tener la ventana de PowerShell/CMD visible.

Luego haz clic en **"Crear Curso"** y observa INMEDIATAMENTE la consola.

## 📋 QUÉ BUSCAR EN LA CONSOLA

Deberías ver mensajes como estos:

### ✅ SI TODO VA BIEN:
```
=== DEBUG: Crear Curso ===
Nombre: Curso de Prueba Debug
Profesor ID: 1
Duración: 40
Fecha Inicio: 2026-02-01
Fecha Fin: 2026-03-30
...
Academia obtenida: 1
Profesor obtenido: 1
Curso creado en memoria, intentando guardar...
=== CursoService.crear() iniciado ===
Academia ID del usuario actual: 1
Academia del curso: 1
Validando fechas: inicio=2026-02-01, fin=2026-03-30
Academia del profesor: 1
Guardando curso en BD...
✅ Curso guardado con ID: X
✅ Curso guardado exitosamente con ID: X
```

### ❌ SI HAY ERROR:
```
=== DEBUG: Crear Curso ===
...
❌ ERROR al crear curso: [MENSAJE DE ERROR]
    [STACK TRACE]
```

O:

```
ERROR: Usuario sin academia asignada
```

O:

```
ERROR: Academia no coincide o es null
```

O:

```
ERROR: Profesor no pertenece a la academia
```

## 🎯 INFORMACIÓN CRÍTICA QUE NECESITO

Por favor, copia y pégame **TODA LA SALIDA** de la consola que aparece desde el momento en que haces clic en "Crear Curso" hasta que la página se recarga.

### Específicamente necesito saber:

1. **¿Aparece "=== DEBUG: Crear Curso ===" ?**
   - SI → El controlador está recibiendo la petición
   - NO → El formulario no se está enviando correctamente

2. **¿Qué valores se imprimen?**
   - Copia los valores de Nombre, Profesor ID, Duración, Fechas, etc.

3. **¿Aparece "=== CursoService.crear() iniciado ===" ?**
   - SI → El servicio se está ejecutando
   - NO → Hay un error antes de llegar al servicio

4. **¿Aparece algún "ERROR:" o "Exception" ?**
   - Copia el mensaje completo

5. **¿Aparece "✅ Curso guardado con ID: X" ?**
   - SI → El curso SÍ se está guardando (problema de redirección)
   - NO → El curso NO se está guardando (problema en el servicio)

## 🔍 CASOS POSIBLES

### Caso A: No aparece ningún log
**Problema:** El formulario no se envía al servidor
**Solución:** Hay un problema con el formulario HTML o JavaScript

### Caso B: Aparece log pero con error de academia
**Problema:** El usuario SECRETARIA no tiene academia asignada
**Solución:** Asignar academia al usuario en BD

### Caso C: Aparece log pero error de profesor
**Problema:** El profesor no pertenece a la misma academia
**Solución:** Verificar datos en BD

### Caso D: Aparece "✅ Curso guardado" pero no redirige
**Problema:** Problema con la redirección
**Solución:** Revisar la configuración de Spring Security

### Caso E: Aparece "✅ Curso guardado" y redirige pero no se ve en lista
**Problema:** El curso se guarda pero la consulta no lo trae
**Solución:** Revisar el método listarPorAcademia()

## 📝 TEMPLATE PARA TU RESPUESTA

Por favor responde con este formato:

```
CONSOLA:
[pega aquí toda la salida de la consola]

COMPORTAMIENTO:
1. Hice clic en "Crear Curso"
2. La página [se recargó / mostró error / redirigió a lista / etc]
3. [Cualquier otro detalle que notes]

NAVEGADOR:
- URL después de enviar: [URL que aparece en la barra]
- Mensaje visible: [cualquier mensaje que aparezca]
```

---

## 🎯 Una vez que me des esta información, podré:

1. Identificar EXACTAMENTE dónde falla
2. Aplicar la solución específica
3. Resolver el problema definitivamente

La aplicación está compilada y corriendo con logging extensivo. 
**Ahora solo necesito que ejecutes el test y me copies la salida.**
