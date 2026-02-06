# 🚀 Inicio Rápido - Gestión de Profesores para Secretarias

## ⚡ 3 Pasos para Empezar

### 1️⃣ Compilar y Ejecutar
```powershell
cd "C:\Users\USUARIO\Desktop\Gestor de Academias AD"
.\mvnw.cmd clean spring-boot:run
```

### 2️⃣ Acceder a la Aplicación
- **URL:** http://localhost:8080
- **Usuario:** [Tu usuario SECRETARIA]
- **Contraseña:** [Tu contraseña]

### 3️⃣ Gestionar Profesores
- En el menú lateral → Click en **"Profesores"**
- Ya puedes crear, editar, desactivar y reactivar profesores

---

## 📍 Ubicación de Archivos Clave

### Backend
```
src/main/java/es/fempa/acd/demosecurityproductos/controller/
└── SecretariaGestionProfesorController.java
```

### Frontend
```
src/main/resources/templates/secretaria/
├── profesores-lista.html
├── profesor-nuevo.html
└── profesor-editar.html
```

### Navegación
```
src/main/resources/templates/
└── fragments.html (línea 97)
```

---

## ✅ Funcionalidades Disponibles

| Acción | URL | Descripción |
|--------|-----|-------------|
| 📋 Ver lista | `/secretaria/profesores` | Todos los profesores |
| ➕ Crear | `/secretaria/profesores/nuevo` | Nuevo profesor |
| ✏️ Editar | `/secretaria/profesores/{id}/editar` | Modificar datos |
| ⚠️ Desactivar | POST `/secretaria/profesores/{id}/eliminar` | Deshabilitar |
| ✅ Reactivar | POST `/secretaria/profesores/{id}/reactivar` | Habilitar |

---

## 🎯 Prueba Rápida

### Test 1: Crear un Profesor
1. Click en "Nuevo Profesor"
2. Rellenar:
   - Usuario: `prof_test_001`
   - Email: `profesor@test.com`
   - Contraseña: `123456`
   - Nombre: `Juan`
   - Apellidos: `Pérez`
3. Click "Crear Profesor"
4. ✅ Debe aparecer en la lista

### Test 2: Editar el Profesor
1. Click en "Editar" del profesor creado
2. Cambiar especialidad: `Matemáticas`
3. Click "Actualizar Profesor"
4. ✅ Cambio reflejado en la lista

### Test 3: Desactivar
1. Click en "Desactivar"
2. Confirmar
3. ✅ Estado cambia a "Inactivo"

### Test 4: Reactivar
1. Cambiar filtro a "Todos"
2. Click en "Reactivar"
3. ✅ Estado cambia a "Activo"

---

## 🔧 Solución Rápida de Problemas

### No compila
```powershell
.\mvnw.cmd clean compile -U
```

### Puerto 8080 ocupado
```powershell
# Cambiar en application.properties:
server.port=8081
```

### No aparece el menú
- Verificar rol SECRETARIA
- Limpiar caché del navegador (Ctrl+Shift+Del)

### Error "Academia no encontrada"
- Verificar que la secretaria tiene academia asignada
- Revisar tabla `usuario` → columna `academia_id`

---

## 📚 Documentación Completa

Para más detalles, consulta:

- `docs/IMPLEMENTACION_PROFESORES_SECRETARIA.md` - Técnica
- `docs/PRUEBAS_PROFESORES_SECRETARIA.md` - Testing
- `docs/README_PROFESORES_SECRETARIA.md` - Manual

---

## 🎉 ¡Listo para Usar!

Todo está implementado y funcionando.  
Solo compila, ejecuta y prueba.

**Última actualización:** 6 de febrero de 2026
