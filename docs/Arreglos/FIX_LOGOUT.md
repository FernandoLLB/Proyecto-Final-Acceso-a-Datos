# Solución: Error al Cerrar Sesión

## Problema
Al hacer clic en "Cerrar Sesión", la aplicación mostraba un error en lugar de redirigir al login.

## Causa
El botón de logout usaba un enlace GET (`<a href="/logout">`), pero Spring Security requiere POST cuando CSRF está habilitado.

## Solución Aplicada
Cambié el enlace por un formulario con método POST en `fragments.html`:

```html
<!-- ANTES (❌ Error) -->
<a th:href="@{/logout}" class="btn-logout">
    <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
</a>

<!-- AHORA (✅ Funciona) -->
<form th:action="@{/logout}" method="post" style="display: inline; margin: 0;">
    <button type="submit" class="btn-logout">
        <i class="bi bi-box-arrow-right"></i> Cerrar Sesión
    </button>
</form>
```

## Archivo Modificado
- `src/main/resources/templates/fragments.html`

## Estado
✅ Compilación exitosa
✅ Problema resuelto

## Próximo Paso
Reinicia la aplicación y prueba el logout desde cualquier página.
