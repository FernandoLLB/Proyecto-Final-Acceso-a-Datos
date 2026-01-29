# 🧪 Instrucciones de Prueba - Internacionalización (i18n)

## 📋 Checklist de Pruebas

### ✅ Pruebas Básicas

#### 1. Login Page
- [ ] Abrir http://localhost:8090/login
- [ ] Verificar que los botones 🇪🇸 ES y 🇬🇧 EN están visibles en la esquina superior derecha
- [ ] Hacer clic en 🇬🇧 EN
- [ ] Verificar que el texto cambia a inglés:
  - "Iniciar Sesión" → "Sign In"
  - "Usuario" → "Username"
  - "Contraseña" → "Password"
- [ ] Hacer clic en 🇪🇸 ES
- [ ] Verificar que el texto vuelve a español
- [ ] Verificar que el botón activo está resaltado en azul

#### 2. Navbar (Después del Login)
- [ ] Iniciar sesión como secretaria
- [ ] Verificar que los botones de idioma están en el navbar superior
- [ ] Hacer clic en 🇬🇧 EN
- [ ] Verificar que "Cerrar Sesión" cambia a "Logout"
- [ ] Hacer clic en 🇪🇸 ES
- [ ] Verificar que vuelve a "Cerrar Sesión"

#### 3. Sidebar
- [ ] Estando en el dashboard de secretaría
- [ ] Cambiar a inglés (🇬🇧 EN)
- [ ] Verificar que el menú lateral cambia:
  - "Dashboard" → "Dashboard" (igual)
  - "Lista de Alumnos" → "Student List"
  - "Lista de Cursos" → "Course List"
  - "Lista de Aulas" → "Classroom List"
  - "Lista de Reservas" → "Reservation List"
- [ ] Cambiar a español (🇪🇸 ES)
- [ ] Verificar que vuelve a español

#### 4. Dashboard de Secretaría
- [ ] Ir a http://localhost:8090/secretaria/dashboard
- [ ] Cambiar a inglés (🇬🇧 EN)
- [ ] Verificar las traducciones de las tarjetas de estadísticas:
  - "Alumnos Activos" → "Active"
  - "Alumnos Inactivos" → "Inactive"
  - "Aulas Activas" → "Active Classrooms"
  - "Reservas Activas" → "ACTIVE"
- [ ] Verificar las acciones rápidas:
  - "Alta de Alumno" → "Register Student"
  - "Crear Curso" → "Create Course"
  - "Crear Aula" → "New Classroom"
  - "Nueva Reserva" → "New Reservation"
- [ ] Verificar el título:
  - "Panel de Secretaría" → "Secretary Panel"
- [ ] Cambiar a español y verificar que todo vuelve a español

#### 5. Lista de Alumnos
- [ ] Ir a http://localhost:8090/secretaria/alumnos
- [ ] Cambiar a inglés (🇬🇧 EN)
- [ ] Verificar:
  - Título: "Gestión de Alumnos" → "Student Management"
  - Subtítulo: "Administra los alumnos de tu academia" → "Manage your academy students"
  - Botón: "Nuevo Alumno" → "Register Student"
- [ ] Cambiar a español y verificar

### ✅ Pruebas de Persistencia

#### 6. Persistencia del Idioma en la Sesión
- [ ] Iniciar sesión en español
- [ ] Cambiar a inglés (🇬🇧 EN)
- [ ] Navegar a diferentes páginas (dashboard, alumnos, cursos)
- [ ] Verificar que el idioma se mantiene en inglés en todas las páginas
- [ ] Cerrar sesión
- [ ] Iniciar sesión nuevamente
- [ ] Verificar que vuelve al idioma por defecto (español)

#### 7. Cambio de Idioma y Navegación
- [ ] Estar en /secretaria/dashboard en español
- [ ] Cambiar a inglés
- [ ] Hacer clic en "Student List" en el sidebar
- [ ] Verificar que la página de alumnos se muestra en inglés
- [ ] Hacer clic en "Dashboard" en el sidebar
- [ ] Verificar que sigue en inglés
- [ ] Cambiar a español
- [ ] Verificar que todas las páginas vuelven a español

### ✅ Pruebas de UI/UX

#### 8. Botones de Idioma
- [ ] Verificar que los botones son claramente visibles
- [ ] Verificar el hover effect (cambio de color al pasar el mouse)
- [ ] Verificar que el botón activo tiene fondo azul
- [ ] Verificar que el botón inactivo tiene fondo transparente
- [ ] Verificar que las banderas emoji se muestran correctamente

#### 9. Responsive Design
- [ ] Reducir el tamaño de la ventana del navegador
- [ ] Verificar que los botones de idioma siguen visibles
- [ ] Probar en diferentes resoluciones
- [ ] Verificar en móvil (si es posible)

### ✅ Pruebas de Compatibilidad

#### 10. Diferentes Navegadores
- [ ] Probar en Chrome
- [ ] Probar en Firefox
- [ ] Probar en Edge
- [ ] Probar en Safari (si está disponible)

#### 11. Caracteres Especiales
- [ ] Verificar que los caracteres con tildes se muestran correctamente en español
  - "Gestión" (í)
  - "Matrícula" (í)
  - "Acción" (ó)
- [ ] Verificar que no hay caracteres extraños o símbolos raros

## 🐛 Problemas Conocidos y Soluciones

### Problema 1: El idioma no cambia
**Solución:**
1. Verificar que la URL tiene el parámetro correcto: `?lang=en` o `?lang=es`
2. Limpiar la caché del navegador (Ctrl+Shift+Del)
3. Cerrar y abrir el navegador
4. Verificar que la sesión está activa

### Problema 2: Algunos textos no están traducidos
**Solución:**
1. Verificar si ese texto está incluido en los archivos de mensajes
2. Las páginas no completamente traducidas seguirán mostrando algunos textos en español
3. Consultar la lista de páginas traducidas en `README_I18N.md`

### Problema 3: Los botones de idioma no son visibles
**Solución:**
1. Verificar que estás en una página con navbar (no en páginas de error)
2. Verificar el tamaño de la ventana del navegador
3. Comprobar que el archivo CSS se cargó correctamente

## 📊 Resultados Esperados

### Página de Login (Español → Inglés)

| Elemento | Español | Inglés |
|----------|---------|--------|
| Título | Iniciar Sesión | Sign In |
| Campo Usuario | Usuario | Username |
| Campo Contraseña | Contraseña | Password |
| Botón | Iniciar Sesión | Sign In |
| Mensaje | Sistema Multi-Academia | Welcome to the academy management system |
| Error | Usuario o contraseña incorrectos | Incorrect username or password |

### Dashboard de Secretaría (Español → Inglés)

| Elemento | Español | Inglés |
|----------|---------|--------|
| Título | Panel de Secretaría | Secretary Panel |
| Subtítulo | Academia: [nombre] | Academy: [name] |
| Card 1 | Alumnos Activos | Active |
| Card 2 | Alumnos Inactivos | Inactive |
| Card 3 | Aulas Activas | Active Classrooms |
| Card 4 | Reservas Activas | ACTIVE |
| Acción 1 | Alta de Alumno | Register Student |
| Acción 2 | Crear Curso | Create Course |
| Acción 3 | Crear Aula | New Classroom |
| Acción 4 | Nueva Reserva | New Reservation |

### Sidebar (Español → Inglés)

| Elemento | Español | Inglés |
|----------|---------|--------|
| Dashboard | Dashboard | Dashboard |
| Alumnos | Lista de Alumnos | Student List |
| Cursos | Lista de Cursos | Course List |
| Aulas | Lista de Aulas | Classroom List |
| Reservas | Lista de Reservas | Reservation List |

## ✅ Criterios de Aceptación

Para considerar la implementación exitosa, todas las siguientes condiciones deben cumplirse:

- [ ] Los botones de idioma son visibles en todas las páginas (login y navbar)
- [ ] El cambio de idioma funciona con un solo clic
- [ ] El idioma activo está claramente indicado visualmente
- [ ] El idioma se mantiene al navegar entre páginas
- [ ] El idioma se resetea al cerrar sesión
- [ ] Todos los textos traducidos cambian correctamente
- [ ] No hay errores en la consola del navegador
- [ ] No hay errores en los logs del servidor
- [ ] Los caracteres especiales se muestran correctamente
- [ ] El hover effect funciona en los botones
- [ ] La aplicación compila sin errores

## 📝 Reporte de Pruebas

### Fecha: ___________
### Probador: ___________

#### Resumen de Resultados:
- Total de pruebas realizadas: ____
- Pruebas exitosas: ____
- Pruebas fallidas: ____
- Problemas encontrados: ____

#### Problemas Encontrados:
1. ________________________________________________
2. ________________________________________________
3. ________________________________________________

#### Observaciones:
_____________________________________________________
_____________________________________________________
_____________________________________________________

#### Conclusión:
[ ] ✅ La funcionalidad i18n funciona correctamente
[ ] ⚠️ Funciona con problemas menores
[ ] ❌ Requiere correcciones

---

**Última actualización:** 29 de enero de 2026
