# Solución al Error de Creación de Cursos

## Problema Identificado

Al intentar crear un nuevo curso desde el formulario en `/secretaria/cursos/nuevo`, la aplicación generaba un error.

### Causa del Error

El formulario HTML utiliza binding de Thymeleaf para enviar los datos:
- `th:field="*{profesor.id}"` - Envía solo el ID del profesor
- `th:field="*{academia.id}"` - Envía solo el ID de la academia

Sin embargo, el controlador `CursoController` espera recibir un objeto `Curso` completo con objetos `Profesor` y `Academia` anidados mediante `@ModelAttribute Curso curso`.

Spring Framework no sabía cómo convertir automáticamente los IDs (String) en objetos de entidad (`Profesor` y `Academia`).

## Solución Implementada

Se crearon tres conversores personalizados para que Spring pueda convertir automáticamente los IDs en entidades:

### 1. StringToProfesorConverter
**Archivo:** `src/main/java/es/fempa/acd/demosecurityproductos/config/StringToProfesorConverter.java`

```java
@Component
public class StringToProfesorConverter implements Converter<String, Profesor> {
    // Convierte el ID del profesor (String) en un objeto Profesor
    // buscándolo en la base de datos mediante ProfesorRepository
}
```

### 2. StringToAcademiaConverter
**Archivo:** `src/main/java/es/fempa/acd/demosecurityproductos/config/StringToAcademiaConverter.java`

```java
@Component
public class StringToAcademiaConverter implements Converter<String, Academia> {
    // Convierte el ID de la academia (String) en un objeto Academia
    // buscándolo en la base de datos mediante AcademiaRepository
}
```

### 3. StringToAulaConverter
**Archivo:** `src/main/java/es/fempa/acd/demosecurityproductos/config/StringToAulaConverter.java`

```java
@Component
public class StringToAulaConverter implements Converter<String, Aula> {
    // Convierte el ID del aula (String) en un objeto Aula
    // buscándolo en la base de datos mediante AulaRepository
    // Previene errores similares en formularios de reservas
}
```

## Cómo Funciona

1. Cuando el formulario se envía, Spring recibe:
   - `profesor.id=5` (String)
   - `academia.id=2` (String)

2. Spring detecta que necesita convertir estos valores en objetos `Profesor` y `Academia`

3. Busca conversores registrados (`@Component`) que implementen `Converter<String, Profesor>` y `Converter<String, Academia>`

4. Los conversores consultan las respectivas tablas en la base de datos y retornan los objetos completos

5. El controlador recibe un objeto `Curso` con las referencias correctas a `Profesor` y `Academia`

## Ventajas de esta Solución

- ✅ **Reutilizable**: Los conversores funcionan automáticamente en todos los formularios que usen binding de entidades
- ✅ **Limpio**: No requiere cambiar la lógica del controlador
- ✅ **Automático**: Spring registra los conversores automáticamente al usar `@Component`
- ✅ **Consistente**: Funciona tanto para crear como para editar cursos

## Archivos Afectados

### Creados:
- `StringToProfesorConverter.java`
- `StringToAcademiaConverter.java`
- `StringToAulaConverter.java`

### Funcionan correctamente ahora:
- `CursoController.java` - Método `crearCurso()` y `actualizarCurso()`
- `curso-nuevo.html` - Formulario de creación de cursos
- `curso-editar.html` - Formulario de edición de cursos
- `aula-nueva.html` - Formulario de creación de aulas
- `aula-editar.html` - Formulario de edición de aulas
- `reserva-nueva.html` - Formulario de creación de reservas
- `reserva-editar.html` - Formulario de edición de reservas

## Prueba de la Solución

Para verificar que el error está resuelto:

1. Compilar el proyecto: `mvn clean compile`
2. Ejecutar la aplicación: `mvn spring-boot:run`
3. Iniciar sesión como usuario con rol SECRETARIA
4. Navegar a "Cursos" → "Nuevo Curso"
5. Completar el formulario con los datos requeridos
6. Enviar el formulario
7. El curso debería crearse exitosamente sin errores

## Fecha de Resolución
27 de enero de 2026
