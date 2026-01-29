# 🌍 Internacionalización (i18n) - Gestor de Academias

## 🎯 ¿Qué es?

La internacionalización (i18n) permite que la aplicación se muestre en diferentes idiomas. Los usuarios pueden cambiar entre **Español** e **Inglés** con un simple clic.

## 🚀 Inicio Rápido

### Para Usuarios
1. Busca los botones de idioma en la esquina superior derecha:
   - **🇪🇸 ES** para Español
   - **🇬🇧 EN** para English

2. Haz clic en el idioma deseado

3. La página se recargará en el idioma seleccionado

4. El idioma se mantendrá durante toda tu sesión

### Para Desarrolladores

#### 1. Agregar una nueva traducción
Edita ambos archivos:
- `src/main/resources/i18n/messages_es.properties`
- `src/main/resources/i18n/messages_en.properties`

```properties
# messages_es.properties
mi.nueva.clave=Mi texto en español

# messages_en.properties
mi.nueva.clave=My text in English
```

#### 2. Usar la traducción en HTML
```html
<span th:text="#{mi.nueva.clave}">Texto por defecto</span>
```

#### 3. Verificar que funciona
- Inicia la aplicación
- Cambia el idioma con los botones
- Verifica que tu texto cambie correctamente

## 📚 Documentación Completa

- **[IMPLEMENTACION_I18N.md](IMPLEMENTACION_I18N.md)** - Documentación técnica completa
- **[GUIA_TRADUCCION_RAPIDA.md](GUIA_TRADUCCION_RAPIDA.md)** - Guía paso a paso para traducir páginas

## 🎨 Ejemplos

### Traducir un título
```html
<h1 th:text="#{student.title}">Gestión de Alumnos</h1>
```

### Traducir un botón
```html
<button>
    <i class="bi bi-save"></i>
    <span th:text="#{app.save}">Guardar</span>
</button>
```

### Traducir con parámetros
```html
<!-- En messages_es.properties -->
welcome.message=Bienvenido, {0}

<!-- En HTML -->
<p th:text="#{welcome.message(${username})}">Bienvenido, Usuario</p>
```

## 🔑 Claves Más Usadas

| Clave | Español | Inglés |
|-------|---------|--------|
| `app.save` | Guardar | Save |
| `app.cancel` | Cancelar | Cancel |
| `app.edit` | Editar | Edit |
| `app.delete` | Eliminar | Delete |
| `app.active` | Activo | Active |
| `app.inactive` | Inactivo | Inactive |
| `app.logout` | Cerrar Sesión | Logout |
| `app.dashboard` | Panel de Control | Dashboard |

Ver todas las claves en: `src/main/resources/i18n/messages_es.properties`

## 🌐 Agregar un Nuevo Idioma

### 1. Crear archivo de mensajes
```bash
cp messages_es.properties messages_fr.properties
```

### 2. Traducir el contenido
Abre `messages_fr.properties` y traduce todos los valores al francés

### 3. Agregar botón en navbar
Edita `templates/fragments.html`:
```html
<a th:href="@{''(lang='fr')}" class="btn-language">
    🇫🇷 FR
</a>
```

## ⚙️ Configuración

### Cambiar idioma por defecto
Edita `config/LocaleConfig.java`:
```java
slr.setDefaultLocale(new Locale("en")); // Cambiar a inglés
```

### Cambiar parámetro de idioma
Edita `config/LocaleConfig.java`:
```java
lci.setParamName("language"); // Usar ?language=es en lugar de ?lang=es
```

## 🐛 Solución de Problemas

### El idioma no cambia
1. Verifica que el parámetro `?lang=es` o `?lang=en` esté en la URL
2. Limpia la caché del navegador
3. Cierra sesión y vuelve a iniciar

### Un texto no se traduce
1. Verifica que la clave existe en ambos archivos (`messages_es.properties` y `messages_en.properties`)
2. Verifica que la sintaxis Thymeleaf sea correcta: `th:text="#{clave}"`
3. Recompila el proyecto: `mvn clean compile`

### Caracteres especiales no se muestran
Los archivos de mensajes usan UTF-8. Verifica que tu editor esté configurado para UTF-8.

## 📊 Estado Actual

### ✅ Completado
- Configuración de Spring i18n
- Archivos de mensajes (ES/EN)
- Botones de cambio de idioma
- Login traducido
- Navbar y sidebars traducidos
- Dashboard de secretaría traducido

### ⏳ Pendiente
- Páginas de listado restantes
- Formularios de creación/edición
- Mensajes de error del backend
- Validaciones de formularios

## 🎯 Mejores Prácticas

1. **Siempre incluye texto por defecto**
   ```html
   <span th:text="#{clave}">Texto por defecto</span>
   ```

2. **Usa nombres consistentes**
   - `entidad.accion` → `student.edit`, `course.new`
   - `entidad.propiedad` → `student.name`, `course.duration`

3. **No traduzcas el título de la app**
   - "Gestor de Academias" permanece igual en todos los idiomas

4. **Prueba en ambos idiomas**
   - Verifica que la página se vea bien en español e inglés

5. **Documenta las nuevas claves**
   - Agrega comentarios en los archivos de mensajes si es necesario

## 🤝 Contribuir

Para agregar más traducciones:

1. Identifica los textos a traducir en el HTML
2. Crea las claves en `messages_es.properties` y `messages_en.properties`
3. Reemplaza el texto en HTML con `th:text="#{clave}"`
4. Prueba que funcione en ambos idiomas
5. Actualiza la documentación si es necesario

## 📞 Soporte

Si tienes preguntas o problemas:
1. Consulta la documentación completa en `docs/IMPLEMENTACION_I18N.md`
2. Revisa la guía rápida en `docs/GUIA_TRADUCCION_RAPIDA.md`
3. Verifica los ejemplos en este README

## 📝 Notas

- El idioma se guarda en la **sesión** del usuario
- El idioma por defecto es **Español**
- Los archivos de mensajes usan codificación **UTF-8**
- Se puede agregar soporte para más idiomas fácilmente

---

**Última actualización:** 29 de enero de 2026
**Versión:** 1.0
**Idiomas disponibles:** Español (ES), English (EN)
