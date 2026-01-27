# 🔍 DIAGNÓSTICO COMPLETO - VERSIÓN FINAL

## ✅ LOGGING ACTIVADO EN 3 CAPAS

### 1. **JavaScript (Navegador)**
- Se imprimirán datos en la **Consola del Navegador** (F12)
- Mensaje: "🚀 FORMULARIO ENVIÁNDOSE..."

### 2. **Controlador (Servidor)**
- Se imprimirán logs en la **Consola de PowerShell/CMD**
- Mensaje: "=== DEBUG: Crear Curso ==="

### 3. **Servicio (Base de Datos)**
- Se imprimirán logs en la **Consola de PowerShell/CMD**  
- Mensaje: "=== CursoService.crear() iniciado ==="

---

## 🚀 INSTRUCCIONES PASO A PASO

### PASO 1: Iniciar Aplicación
```bash
cd "C:\Users\USUARIO\Desktop\Gestor de Academias AD"
mvn spring-boot:run
```

Espera ver: `Started DemoSecurityProductosApplication`

### PASO 2: Abrir Navegador con Herramientas de Desarrollo

1. Abre `http://localhost:8080`
2. **Presiona F12** (abre herramientas de desarrollo)
3. Ve a la pestaña **"Console"** (Consola)
4. Deja esa pestaña abierta

### PASO 3: Login y Navegar
- Login como SECRETARIA
- Ir a: **Cursos** → **Nuevo Curso**

### PASO 4: Llenar Formulario

**Campos obligatorios (tienen asterisco rojo):**
- Nombre: `Curso Debug Test`
- Profesor: `(selecciona cualquiera)`
- Duración: `40`
- Fecha Inicio: `2026-02-01`
- Fecha Fin: `2026-03-30`

**Campos opcionales:**
- Categoría: `Programación`
- Descripción: `Prueba de debugging`
- Precio: `150`
- Plazas: `20`

### PASO 5: Hacer Clic en "Crear Curso"

**OBSERVA AMBAS CONSOLAS:**

#### A) CONSOLA DEL NAVEGADOR (F12)
Deberías ver:
```javascript
🚀 FORMULARIO ENVIÁNDOSE...
Datos del formulario:
nombre: Curso Debug Test
categoria: Programación
descripcion: Prueba de debugging
profesorId: 1
duracionHoras: 40
plazasDisponibles: 20
precio: 150
fechaInicio: 2026-02-01
fechaFin: 2026-03-30
_csrf: [algún token largo]
```

#### B) CONSOLA DE POWERSHELL/CMD
Deberías ver:
```
=== DEBUG: Crear Curso ===
Nombre: Curso Debug Test
Profesor ID: 1
Duración: 40
Fecha Inicio: 2026-02-01
Fecha Fin: 2026-03-30
Precio: 150
Categoría: Programación
Plazas: 20
Academia obtenida: 1
Profesor obtenido: 1
Curso creado en memoria, intentando guardar...
=== CursoService.crear() iniciado ===
Academia ID del usuario actual: 1
Academia del curso: 1
Validando fechas: inicio=2026-02-01, fin=2026-03-30
Academia del profesor: 1
Guardando curso en BD...
✅ Curso guardado con ID: 1
✅ Curso guardado exitosamente con ID: 1
```

---

## 📋 INFORMACIÓN QUE NECESITO

### Por favor, cópiame:

#### 1. **CONSOLA DEL NAVEGADOR (F12)**
```
[Pega aquí todo lo que aparece en la consola del navegador]
```

#### 2. **CONSOLA DE POWERSHELL/CMD**
```
[Pega aquí todo lo que aparece en PowerShell desde que haces clic]
```

#### 3. **COMPORTAMIENTO**
- URL antes de enviar: `_______________________`
- URL después de enviar: `_______________________`
- ¿Aparece algún mensaje en la página? `_______________________`
- ¿El formulario se vacía? SÍ / NO
- ¿Ves el curso en la lista si vas a /secretaria/cursos? SÍ / NO

---

## 🎯 DIAGNÓSTICO SEGÚN LOS LOGS

### ESCENARIO 1: No aparece nada en consola del navegador
❌ **Problema:** JavaScript no se carga o hay error en la página
✅ **Solución:** Verificar errores en consola del navegador (pestaña roja)

### ESCENARIO 2: Aparece en navegador pero NO en servidor
❌ **Problema:** Formulario no llega al servidor (posible error de red/CSRF)
✅ **Solución:** Verificar pestaña "Network" en F12, buscar POST a /crear

### ESCENARIO 3: Aparece en servidor con "Usuario sin academia"
❌ **Problema:** Usuario SECRETARIA no tiene academia asignada
✅ **Solución:** Ejecutar SQL:
```sql
UPDATE usuario SET academia_id = 1 WHERE username = 'tu_usuario_secretaria';
```

### ESCENARIO 4: Aparece en servidor con "Profesor no pertenece"
❌ **Problema:** Profesor y usuario de academias diferentes
✅ **Solución:** Verificar que profesor y usuario tengan mismo academia_id

### ESCENARIO 5: Aparece "✅ Curso guardado con ID: X"
✅ **¡FUNCIONA!** El curso SÍ se está guardando
❓ **Pero:** ¿Por qué no se ve? 
   - Verifica URL después de enviar
   - Verifica si redirige a /secretaria/cursos
   - Verifica si el curso aparece en la lista

### ESCENARIO 6: Aparece error de fechas
❌ **Problema:** Fechas mal formateadas o incorrectas
✅ **Solución:** Usar formato YYYY-MM-DD en los inputs

---

## 💡 TIPS ADICIONALES

### Ver datos enviados en detalle:
1. F12 → Pestaña **"Network"**
2. Haz clic en "Crear Curso"
3. Busca la petición POST a `crear`
4. Clic en ella → Pestaña **"Payload"**
5. Verás todos los datos enviados

### Ver respuesta del servidor:
1. En la misma petición POST
2. Pestaña **"Response"**
3. Verás el HTML devuelto o el redirect

---

## 🎯 ARCHIVO COMPILADO

✅ **Todo está compilado y listo**

La aplicación tiene 3 capas de logging:
- ✅ Cliente (JavaScript)
- ✅ Controlador (Java)
- ✅ Servicio (Java)

**Ejecuta `mvn spring-boot:run` y copia ambas consolas.**

---

## 📞 PRÓXIMO PASO

Una vez que me des los logs de **AMBAS consolas**, sabré:
1. Si el formulario se envía correctamente
2. Si llega al servidor
3. Si hay error en el servicio
4. Si se guarda pero no redirige
5. Cualquier otro problema específico

Y podré dar la solución exacta y definitiva.
