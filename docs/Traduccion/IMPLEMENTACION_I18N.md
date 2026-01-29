# Implementación de Internacionalización (i18n) - Español/Inglés

## Fecha de Implementación
29 de enero de 2026

## Descripción
Se ha implementado la funcionalidad de cambio de idioma entre Español e Inglés en toda la aplicación, permitiendo a los usuarios cambiar el idioma de la interfaz mediante botones dedicados.

## Componentes Implementados

### 1. Configuración de Spring i18n

#### **application.properties**
```properties
# Internationalization (i18n)
spring.messages.basename=i18n/messages
spring.messages.encoding=UTF-8
spring.messages.fallback-to-system-locale=false
spring.web.locale=es
spring.web.locale-resolver=fixed
```

#### **LocaleConfig.java** (Nueva clase)
```java
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(new Locale("es")); // Español por defecto
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang"); // Parámetro: ?lang=en o ?lang=es
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
```

### 2. Archivos de Mensajes

#### Estructura de archivos
```
src/main/resources/i18n/
├── messages.properties (por defecto - español)
├── messages_es.properties (español)
└── messages_en.properties (inglés)
```

#### Categorías de mensajes incluidos
- **Común**: Botones, acciones, estados generales
- **Login**: Página de inicio de sesión
- **Navbar**: Barra de navegación
- **Roles**: Nombres de roles
- **Dashboard**: Panel de control
- **Academias**: Gestión de academias
- **Profesores**: Gestión de profesores
- **Alumnos**: Gestión de estudiantes
- **Cursos**: Gestión de cursos
- **Aulas**: Gestión de aulas
- **Reservas**: Gestión de reservas
- **Filtros**: Filtros de búsqueda
- **Mensajes**: Alertas y notificaciones
- **Validación**: Mensajes de validación
- **Estadísticas**: KPIs y estadísticas

### 3. Botones de Cambio de Idioma

#### **Navbar (fragments.html)**
```html
<!-- Language Switcher -->
<div class="language-switcher" style="display: flex; gap: 0.5rem; margin-right: 1rem;">
    <a th:href="@{''(lang='es')}" class="btn-language" 
       th:classappend="${#locale.toString() == 'es' ? 'active' : ''}" 
       title="Español">
        🇪🇸 ES
    </a>
    <a th:href="@{''(lang='en')}" class="btn-language" 
       th:classappend="${#locale.toString() == 'en' ? 'active' : ''}" 
       title="English">
        🇬🇧 EN
    </a>
</div>
```

#### **Login (posición fija top-right)**
```html
<div style="position: fixed; top: 1.5rem; right: 1.5rem; z-index: 1000; display: flex; gap: 0.5rem;">
    <a th:href="@{/login(lang='es')}" class="btn-language" 
       th:classappend="${#locale.toString() == 'es' ? 'active' : ''}" 
       title="Español">
        🇪🇸 ES
    </a>
    <a th:href="@{/login(lang='en')}" class="btn-language" 
       th:classappend="${#locale.toString() == 'en' ? 'active' : ''}" 
       title="English">
        🇬🇧 EN
    </a>
</div>
```

### 4. Estilos CSS

```css
/* Language Switcher */
.language-switcher {
    display: flex;
    gap: 0.5rem;
}

.btn-language {
    padding: 0.375rem 0.75rem;
    border-radius: var(--radius);
    border: 1px solid var(--border-color);
    background: transparent;
    color: var(--text-secondary);
    font-size: 0.813rem;
    font-weight: 500;
    cursor: pointer;
    transition: all 0.2s;
    text-decoration: none;
    display: inline-flex;
    align-items: center;
    gap: 0.25rem;
}

.btn-language:hover {
    background: var(--bg-tertiary);
    color: var(--text-primary);
    border-color: var(--primary-color);
}

.btn-language.active {
    background: var(--primary-color);
    color: white;
    border-color: var(--primary-color);
}
```

### 5. Uso de Mensajes en Thymeleaf

#### Sintaxis básica
```html
<!-- Texto simple -->
<h1 th:text="#{student.title}">Gestión de Alumnos</h1>

<!-- Atributos -->
<button th:title="#{app.save}">Guardar</button>

<!-- Placeholder -->
<input th:placeholder="#{login.username}" />

<!-- Concatenación -->
<title th:text="#{login.title} + ' - Gestor de Academias'">Login</title>

<!-- Con parámetros -->
<p th:text="#{dashboard.welcome(${username})}">Bienvenido</p>
```

## Páginas Actualizadas con i18n

### ✅ Páginas Completamente Traducidas
1. **login.html** - Página de inicio de sesión
2. **fragments.html** - Navbar y sidebars
3. **secretaria/dashboard.html** - Dashboard de secretaría
4. **secretaria/alumnos-lista.html** - Lista de alumnos (parcial)

### 🔄 Páginas Pendientes de Traducción Completa
- secretaria/cursos-lista.html
- secretaria/aulas-lista.html
- secretaria/reservas-lista.html
- admin/dashboard.html
- admin/academias-lista.html
- admin/profesores-lista.html
- Formularios de edición y creación
- Páginas de otros roles (profesor, propietario, alumno)

## Cómo Funciona

### 1. Cambio de Idioma
El usuario hace clic en el botón de idioma (🇪🇸 ES o 🇬🇧 EN), que agrega el parámetro `?lang=es` o `?lang=en` a la URL actual.

### 2. Interceptor
El `LocaleChangeInterceptor` detecta el parámetro `lang` y cambia el locale de la sesión.

### 3. Persistencia
El idioma se guarda en la sesión del usuario usando `SessionLocaleResolver`, por lo que se mantiene mientras la sesión esté activa.

### 4. Renderizado
Thymeleaf usa el locale de la sesión para seleccionar el archivo de mensajes correcto (`messages_es.properties` o `messages_en.properties`).

## Ejemplos de Uso

### Traducción de Textos Estáticos
```html
<!-- Español: "Gestión de Alumnos" -->
<!-- Inglés: "Student Management" -->
<h1 th:text="#{student.title}">Gestión de Alumnos</h1>
```

### Traducción de Botones
```html
<!-- Español: "Nuevo Alumno" -->
<!-- Inglés: "Register Student" -->
<button>
    <i class="bi bi-person-plus"></i> 
    <span th:text="#{student.new}">Nuevo Alumno</span>
</button>
```

### Traducción de Mensajes con Parámetros
```html
<!-- Español: "Bienvenido, Juan" -->
<!-- Inglés: "Welcome, Juan" -->
<p th:text="#{dashboard.welcome(${username})}">Bienvenido, Usuario</p>
```

### Traducción de Placeholders
```html
<!-- Español: "Usuario" -->
<!-- Inglés: "Username" -->
<input type="text" th:placeholder="#{login.username}" />
```

## Características Implementadas

### ✅ Completado
- [x] Configuración de Spring i18n
- [x] Archivos de mensajes en español e inglés
- [x] Botones de cambio de idioma en navbar
- [x] Botones de cambio de idioma en login
- [x] Estilos CSS para botones de idioma
- [x] Indicador visual de idioma activo
- [x] Persistencia del idioma en sesión
- [x] Traducción de navbar y sidebars
- [x] Traducción de página de login
- [x] Traducción parcial de dashboards
- [x] Compilación exitosa

### 🔄 Pendiente
- [ ] Traducción completa de todas las páginas de listado
- [ ] Traducción de formularios de edición/creación
- [ ] Traducción de mensajes de error del servidor
- [ ] Traducción de validaciones de formularios
- [ ] Pruebas de usuario completas

## Notas Técnicas

### Idioma por Defecto
El idioma por defecto es **Español (ES)**, configurado en:
- `LocaleConfig.java`: `slr.setDefaultLocale(new Locale("es"));`
- `application.properties`: `spring.web.locale=es`

### Codificación
Todos los archivos de mensajes usan **UTF-8** para soportar caracteres especiales:
```properties
spring.messages.encoding=UTF-8
```

### Fallback
Si un mensaje no se encuentra en el idioma seleccionado, **no** se usa el idioma del sistema:
```properties
spring.messages.fallback-to-system-locale=false
```

### Título de la Aplicación
El título "Gestor de Academias" **NO** se traduce, como solicitado en los requisitos.

## Ventajas de la Implementación

1. **Fácil Mantenimiento**: Todos los textos están centralizados en archivos de propiedades
2. **Escalable**: Fácil agregar nuevos idiomas (crear `messages_fr.properties`, etc.)
3. **Consistente**: Los mismos términos se usan en toda la aplicación
4. **Profesional**: Mejora la experiencia de usuarios internacionales
5. **Estándar**: Usa las mejores prácticas de Spring Boot

## Cómo Agregar un Nuevo Idioma

1. Crear archivo: `src/main/resources/i18n/messages_[codigo].properties`
   - Ejemplo: `messages_fr.properties` para francés
   
2. Copiar el contenido de `messages_en.properties`

3. Traducir todos los valores

4. Agregar botón en navbar:
   ```html
   <a th:href="@{''(lang='fr')}" class="btn-language">
       🇫🇷 FR
   </a>
   ```

## Cómo Agregar Nuevas Traducciones

1. Agregar la clave en ambos archivos (`messages_es.properties` y `messages_en.properties`):
   ```properties
   # messages_es.properties
   nuevo.mensaje=Hola Mundo
   
   # messages_en.properties
   nuevo.mensaje=Hello World
   ```

2. Usar en Thymeleaf:
   ```html
   <span th:text="#{nuevo.mensaje}">Texto por defecto</span>
   ```

## Testing

### Pruebas Manuales Realizadas
- [x] Cambio de idioma en login
- [x] Cambio de idioma en dashboard
- [x] Persistencia del idioma al navegar entre páginas
- [x] Indicador visual de idioma activo
- [x] Renderizado correcto de caracteres especiales

### Pruebas Pendientes
- [ ] Cambio de idioma en todas las páginas
- [ ] Mensajes de error traducidos
- [ ] Validaciones en formularios traducidas
- [ ] Pruebas con diferentes navegadores

## Recursos

### Archivos Creados
- `src/main/java/es/fempa/acd/demosecurityproductos/config/LocaleConfig.java`
- `src/main/resources/i18n/messages.properties`
- `src/main/resources/i18n/messages_es.properties`
- `src/main/resources/i18n/messages_en.properties`

### Archivos Modificados
- `src/main/resources/application.properties`
- `src/main/resources/templates/fragments.html`
- `src/main/resources/templates/login.html`
- `src/main/resources/templates/secretaria/dashboard.html`
- `src/main/resources/templates/secretaria/alumnos-lista.html`
- `src/main/resources/static/css/style.css`

## Conclusión

La funcionalidad de internacionalización ha sido implementada exitosamente con:
- ✅ Configuración completa de Spring i18n
- ✅ Archivos de mensajes en 2 idiomas (ES/EN)
- ✅ Botones de cambio de idioma visibles y funcionales
- ✅ Persistencia del idioma en sesión
- ✅ Compilación exitosa sin errores

El sistema está listo para ser extendido con traducciones completas de todas las páginas y la adición de nuevos idiomas si es necesario.

---

**Próximos Pasos Recomendados:**
1. Completar la traducción de todas las páginas restantes
2. Traducir mensajes de error y validaciones del backend
3. Realizar pruebas exhaustivas de usuario
4. Considerar agregar más idiomas (francés, alemán, etc.)
