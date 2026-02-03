# Guía de JavaDoc - Gestor de Academias

## ¿Qué es JavaDoc?

JavaDoc es una herramienta de documentación automática que:
- Lee comentarios especiales en tu código Java
- Genera páginas HTML navegables
- Crea un "manual de usuario" de tus clases y métodos
- Es el estándar de la industria para documentar código Java

---

## 📂 Ubicación del JavaDoc Generado

El JavaDoc de tu proyecto está en:
```
target/site/apidocs/index.html
```

**Ruta completa:**
```
C:\Users\USUARIO\Desktop\Gestor de Academias AD\target\site\apidocs\index.html
```

---

## 🚀 Cómo Ver el JavaDoc

### Método 1: Abrir desde Windows Explorer
1. Abre el Explorador de Windows
2. Navega a: `Gestor de Academias AD\target\site\apidocs\`
3. Haz doble clic en `index.html`
4. Se abrirá en tu navegador predeterminado

### Método 2: Desde PowerShell
```powershell
# Abrir el JavaDoc
start target/site/apidocs/index.html
```

### Método 3: Desde IntelliJ IDEA
1. En el panel de proyectos, navega a: `target/site/apidocs/`
2. Clic derecho en `index.html`
3. Selecciona "Open in Browser"

---

## 🔄 Cómo Regenerar el JavaDoc

Si haces cambios en el código y quieres actualizar la documentación:

### Comando Maven:
```powershell
./mvnw.cmd javadoc:javadoc
```

O si tienes Maven instalado globalmente:
```powershell
mvn javadoc:javadoc
```

**Proceso completo:**
```powershell
# 1. Navegar al directorio del proyecto
cd "C:\Users\USUARIO\Desktop\Gestor de Academias AD"

# 2. Generar JavaDoc
./mvnw.cmd javadoc:javadoc

# 3. Abrir en navegador
start target/site/apidocs/index.html
```

---

## 📖 Navegación en el JavaDoc

Una vez abierto, verás una interfaz HTML con:

### Panel Izquierdo (Packages)
- Lista de todos los paquetes del proyecto
- Ejemplo: `es.fempa.acd.demosecurityproductos.model`

### Panel Central (Clases)
- Lista de clases del paquete seleccionado
- Ejemplo: `Usuario`, `Academia`, `Curso`

### Panel Principal (Detalles)
- Documentación completa de la clase seleccionada
- Descripción general
- Lista de campos (atributos)
- Lista de constructores
- Lista de métodos con:
  - Descripción
  - Parámetros (@param)
  - Valor de retorno (@return)
  - Excepciones (@throws)

### Barra Superior
- **Overview** - Vista general de todo el proyecto
- **Package** - Vista del paquete actual
- **Class** - Vista de la clase actual
- **Tree** - Árbol de herencia
- **Index** - Índice alfabético

---

## 🔍 Ejemplo: Buscar la Clase Usuario

1. **Abrir el JavaDoc:** `target/site/apidocs/index.html`

2. **Navegar:**
   - En el panel izquierdo, clic en `es.fempa.acd.demosecurityproductos.model`
   - En el panel central, clic en `Usuario`

3. **Ver documentación:**
   ```
   Class Usuario
   
   Entidad que representa un usuario del sistema de gestión de academias.
   
   Un usuario puede tener diferentes roles (ADMIN, PROPIETARIO, SECRETARIA, 
   PROFESOR, ALUMNO) y está asociado a una academia.
   
   Field Summary
   - id: Long - Identificador único del usuario
   - username: String - Nombre de usuario único
   - email: String - Correo electrónico único
   ...
   
   Constructor Summary
   - Usuario() - Constructor por defecto
   
   Method Summary
   - getId(): Long - Obtiene el identificador único del usuario
   - getUsername(): String - Obtiene el nombre de usuario
   ...
   ```

---

## 💡 Cómo Funciona JavaDoc

### 1. Comentarios Especiales
JavaDoc lee comentarios que empiezan con `/**`:

```java
/**
 * Entidad que representa un usuario del sistema.
 * 
 * <p>Descripción detallada aquí.</p>
 * 
 * @author Sistema Gestor de Academias
 * @version 1.0
 * @since 1.0
 */
public class Usuario {
    
    /**
     * Identificador único del usuario.
     */
    private Long id;
    
    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return ID del usuario
     */
    public Long getId() {
        return id;
    }
    
    /**
     * Establece el identificador único del usuario.
     * 
     * @param id ID del usuario a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }
}
```

### 2. Tags Especiales

| Tag | Descripción | Ejemplo |
|-----|-------------|---------|
| `@author` | Autor de la clase | `@author Juan Pérez` |
| `@version` | Versión | `@version 1.0` |
| `@since` | Desde qué versión existe | `@since 1.0` |
| `@param` | Descripción de parámetro | `@param id ID del usuario` |
| `@return` | Descripción del valor retornado | `@return ID del usuario` |
| `@throws` | Excepciones lanzadas | `@throws IllegalArgumentException si id es null` |
| `@see` | Referencias a otras clases | `@see Academia` |
| `@deprecated` | Marca algo como obsoleto | `@deprecated Usar nuevoMetodo() en su lugar` |

### 3. HTML en JavaDoc
Puedes usar etiquetas HTML:

```java
/**
 * <p>Este es un párrafo.</p>
 * 
 * <ul>
 * <li>Item 1</li>
 * <li>Item 2</li>
 * </ul>
 * 
 * <strong>Importante:</strong> No puede ser null.
 */
```

---

## 🎨 Estructura del JavaDoc Generado

```
target/site/apidocs/
├── index.html                          # Página principal
├── allclasses-index.html               # Todas las clases
├── overview-tree.html                  # Árbol de herencia
├── index-all.html                      # Índice alfabético
├── help-doc.html                       # Ayuda
└── es/
    └── fempa/
        └── acd/
            └── demosecurityproductos/
                ├── model/
                │   ├── Usuario.html        # Documentación de Usuario
                │   ├── Academia.html
                │   ├── Curso.html
                │   └── package-summary.html
                ├── repository/
                │   └── ...
                ├── service/
                │   └── ...
                └── controller/
                    └── ...
```

---

## 🔧 Configuración del Plugin Maven

En el `pom.xml` está configurado así:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-javadoc-plugin</artifactId>
    <version>3.6.3</version>
    <configuration>
        <charset>UTF-8</charset>
        <encoding>UTF-8</encoding>
        <docencoding>UTF-8</docencoding>
        <show>private</show>              <!-- Muestra todo, incluso private -->
        <nohelp>true</nohelp>
        <additionalOptions>-Xdoclint:none</additionalOptions>  <!-- Ignora warnings -->
    </configuration>
</plugin>
```

---

## 📝 Buenas Prácticas para JavaDoc

### 1. Documenta las Clases Públicas
```java
/**
 * Servicio para gestionar usuarios del sistema.
 * 
 * <p>Proporciona operaciones CRUD y lógica de negocio
 * relacionada con usuarios.</p>
 * 
 * @author Equipo de Desarrollo
 * @version 1.0
 */
public class UsuarioService {
    // ...
}
```

### 2. Documenta los Métodos Públicos
```java
/**
 * Busca un usuario por su nombre de usuario.
 * 
 * @param username nombre de usuario a buscar
 * @return Optional con el usuario si existe, vacío si no
 * @throws IllegalArgumentException si username es null o vacío
 */
public Optional<Usuario> findByUsername(String username) {
    // ...
}
```

### 3. Documenta los Parámetros Complejos
```java
/**
 * Crea un nuevo curso en el sistema.
 * 
 * @param curso objeto con los datos del curso. No puede ser null.
 *              Debe tener nombre, fechas válidas y profesor asignado
 * @return el curso guardado con su ID asignado
 */
public Curso crearCurso(Curso curso) {
    // ...
}
```

### 4. Indica Precondiciones y Postcondiciones
```java
/**
 * Matricula un alumno en un curso.
 * 
 * <p><strong>Precondiciones:</strong></p>
 * <ul>
 * <li>El alumno debe existir y estar activo</li>
 * <li>El curso debe existir y tener plazas disponibles</li>
 * <li>El alumno no puede estar ya matriculado en el curso</li>
 * </ul>
 * 
 * <p><strong>Postcondiciones:</strong></p>
 * <ul>
 * <li>Se crea una nueva matrícula con estado ACTIVA</li>
 * <li>Se decrementa el número de plazas disponibles</li>
 * </ul>
 * 
 * @param alumnoId ID del alumno
 * @param cursoId ID del curso
 * @return la matrícula creada
 * @throws RecursoNoEncontradoException si alumno o curso no existen
 * @throws ConflictoException si el alumno ya está matriculado
 */
public Matricula matricular(Long alumnoId, Long cursoId) {
    // ...
}
```

---

## 🎯 Ventajas del JavaDoc

### Para Desarrolladores
- ✓ Documentación siempre actualizada con el código
- ✓ Fácil de leer y navegar
- ✓ Integración con IDEs (IntelliJ muestra JavaDoc al hacer hover)

### Para el Equipo
- ✓ Onboarding más rápido de nuevos miembros
- ✓ Menos preguntas sobre "¿qué hace este método?"
- ✓ Estándar de la industria (todos lo conocen)

### Para el Proyecto
- ✓ Referencia técnica oficial
- ✓ Facilita el mantenimiento a largo plazo
- ✓ Cumple requisitos de documentación profesional

---

## 🔍 Búsqueda en el JavaDoc

### Barra de Búsqueda
En la parte superior del JavaDoc hay una barra de búsqueda donde puedes:
- Buscar clases: escribe "Usuario" → Enter
- Buscar métodos: escribe "findByUsername" → Enter
- Buscar paquetes: escribe "model" → Enter

### Índice Alfabético
Clic en **"Index"** en la barra superior para ver todo ordenado alfabéticamente.

### Árbol de Herencia
Clic en **"Tree"** para ver las relaciones de herencia entre clases.

---

## 🛠️ Troubleshooting

### Problema: "No se encuentra target/site/apidocs"
**Solución:** Ejecuta primero:
```powershell
./mvnw.cmd javadoc:javadoc
```

### Problema: "El JavaDoc se ve en blanco"
**Solución:** 
1. Verifica que el archivo existe
2. Prueba con otro navegador
3. Verifica permisos de la carpeta

### Problema: "Warnings al generar JavaDoc"
**Solución:** Son normales. El plugin está configurado con `-Xdoclint:none` para ignorarlos.

### Problema: "Quiero documentar más clases"
**Solución:** Añade comentarios JavaDoc y regenera:
```powershell
# Añadir comentarios /** */ a tus clases
# Luego regenerar:
./mvnw.cmd javadoc:javadoc
```

---

## 📚 Recursos Adicionales

### Documentación Oficial
- [Oracle JavaDoc Guide](https://docs.oracle.com/javase/8/docs/technotes/tools/windows/javadoc.html)
- [Maven JavaDoc Plugin](https://maven.apache.org/plugins/maven-javadoc-plugin/)

### Ejemplos en el Proyecto
- **Usuario.java** - Completamente documentado
- Ver: `src/main/java/es/fempa/acd/demosecurityproductos/model/Usuario.java`

---

## 📋 Checklist de Uso

Para verificar que todo funciona:

- [ ] Puedo abrir `target/site/apidocs/index.html` en mi navegador
- [ ] Veo el listado de paquetes en el panel izquierdo
- [ ] Puedo navegar a `es.fempa.acd.demosecurityproductos.model`
- [ ] Puedo ver la clase `Usuario` con su documentación
- [ ] Puedo buscar clases usando la barra de búsqueda
- [ ] Puedo regenerar el JavaDoc con el comando Maven

---

## 🎓 Resumen

**JavaDoc es:**
- Una herramienta de documentación automática
- Genera HTML a partir de comentarios en el código
- Ya está configurado y generado en tu proyecto

**Para verlo:**
```powershell
start target/site/apidocs/index.html
```

**Para regenerarlo:**
```powershell
./mvnw.cmd javadoc:javadoc
```

**Ubicación:**
```
C:\Users\USUARIO\Desktop\Gestor de Academias AD\target\site\apidocs\index.html
```

---

**¡Ya tienes tu documentación técnica profesional lista para usar!** 🎉
