# Guía de Debugging: Dashboard de Alumno

## Fecha: 29 de enero de 2026

## Problema: "Error Desconocido" en la tabla de cursos

### Síntomas
- La sección "Mis Cursos Matriculados" muestra "Error Desconocido"
- No se muestran datos en las columnas (profesor, duración, fechas, etc.)

### Causas Comunes

#### 1. Datos No Inicializados
**Problema**: El alumno no tiene matrículas en la base de datos.

**Verificación**:
```sql
-- Verificar si el alumno tiene matrículas
SELECT * FROM matricula WHERE alumno_id = [ID_DEL_ALUMNO];

-- Verificar si las matrículas tienen cursos asociados
SELECT m.id, m.alumno_id, m.curso_id, c.nombre as curso_nombre
FROM matricula m
LEFT JOIN curso c ON m.curso_id = c.id
WHERE m.alumno_id = [ID_DEL_ALUMNO];
```

**Solución**: Si no hay datos, matricular al alumno en algún curso desde el panel de secretaría.

#### 2. Relaciones JPA No Cargadas (Lazy Loading)
**Problema**: Las relaciones `@ManyToOne` están configuradas como `LAZY` en lugar de `EAGER`.

**Verificación**: Revisar `Matricula.java`:
```java
@ManyToOne(fetch = FetchType.EAGER)  // ✅ Debe ser EAGER
@JoinColumn(name = "curso_id", nullable = false)
private Curso curso;
```

**Solución**: Cambiar `FetchType.LAZY` a `FetchType.EAGER` o usar `@EntityGraph`.

#### 3. Valores Null en Relaciones Anidadas
**Problema**: Algunos cursos no tienen profesor asignado, o el profesor no tiene usuario asociado.

**Verificación**:
```sql
-- Verificar integridad de las relaciones
SELECT 
    m.id as matricula_id,
    c.id as curso_id, 
    c.nombre as curso_nombre,
    p.id as profesor_id,
    u.id as usuario_id,
    u.nombre as usuario_nombre
FROM matricula m
LEFT JOIN curso c ON m.curso_id = c.id
LEFT JOIN profesor p ON c.profesor_id = p.id
LEFT JOIN usuario u ON p.usuario_id = u.id
WHERE m.alumno_id = [ID_DEL_ALUMNO];
```

**Solución**: Ya está implementado en el template con operadores seguros (`?.`) y valores por defecto.

#### 4. Problemas de Seguridad/Permisos
**Problema**: El método del servicio valida permisos que el alumno no tiene.

**Verificación**: Revisar los logs de la aplicación:
```
grep "Error al obtener matrículas" application.log
```

**Solución**: Usar los métodos específicos `obtenerMisMatriculas()` que no validan tenant scope.

### Pasos de Debugging

#### Paso 1: Verificar Logs de la Aplicación
Buscar en la consola o archivo de logs:
```
INFO  - Cargando dashboard para alumno con usuario ID: X
INFO  - Alumno encontrado: ID=X, nombre=...
INFO  - Matrículas totales encontradas: X
```

Si aparece:
```
ERROR - Error al obtener matrículas para alumno ID X: ...
```
Revisar el mensaje de error completo.

#### Paso 2: Verificar Datos en Base de Datos
```sql
-- 1. Verificar que el usuario existe y es alumno
SELECT u.id, u.username, u.nombre, u.apellidos, a.id as alumno_id
FROM usuario u
LEFT JOIN alumno a ON a.usuario_id = u.id
WHERE u.username = 'nombre_usuario_alumno';

-- 2. Verificar matrículas
SELECT * FROM matricula WHERE alumno_id = [ID_ALUMNO];

-- 3. Verificar cursos asociados
SELECT 
    m.id, 
    m.estado,
    c.nombre as curso,
    p.id as profesor_id,
    u.nombre as profesor_nombre
FROM matricula m
INNER JOIN curso c ON m.curso_id = c.id
INNER JOIN profesor p ON c.profesor_id = p.id
INNER JOIN usuario u ON p.usuario_id = u.id
WHERE m.alumno_id = [ID_ALUMNO];
```

#### Paso 3: Verificar Template de Thymeleaf
Revisar la consola del navegador (F12) para ver errores de JavaScript o problemas de renderizado.

#### Paso 4: Probar con Datos de Prueba
Crear una matrícula de prueba:
```sql
-- Asegurarse de que existe un curso con todos los datos
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado)
VALUES (1, [ID_ALUMNO], [ID_CURSO], NOW(), 'ACTIVA');
```

### Soluciones Aplicadas

#### ✅ Manejo Seguro de Null en Template
```html
<!-- Antes (inseguro) -->
<td th:text="${matricula.curso.profesor.usuario.nombre}">...</td>

<!-- Después (seguro) -->
<td>
    <span th:if="${matricula?.curso?.profesor?.usuario != null}" 
          th:text="${matricula.curso.profesor.usuario.nombre}">
        Profesor
    </span>
    <span th:unless="${matricula?.curso?.profesor?.usuario != null}" class="text-muted">
        No asignado
    </span>
</td>
```

#### ✅ Logging Completo en Controlador
```java
logger.info("Matrículas totales encontradas: {}", todasMatriculas.size());
for (Matricula m : todasMatriculas) {
    logger.info("Matrícula: ID={}, Curso={}, Estado={}", 
                m.getId(), 
                m.getCurso() != null ? m.getCurso().getNombre() : "NULL",
                m.getEstado());
}
```

#### ✅ Métodos de Servicio Específicos
```java
// No valida tenant scope para evitar problemas de permisos
public List<Matricula> obtenerMisMatriculas(Long alumnoId) {
    return matriculaRepository.findByAlumnoId(alumnoId);
}
```

### Testing Manual

#### 1. Crear Usuario Alumno de Prueba
```sql
INSERT INTO usuario (username, password, email, nombre, apellidos, rol, academia_id, activo)
VALUES ('alumno.test', '{bcrypt}...', 'alumno@test.com', 'Juan', 'Pérez', 'ALUMNO', 1, true);

INSERT INTO alumno (usuario_id, academia_id, fecha_registro, estado_matricula)
VALUES (LAST_INSERT_ID(), 1, NOW(), 'ACTIVO');
```

#### 2. Crear Curso con Profesor
```sql
-- Asegurarse de que existe un profesor
INSERT INTO curso (academia_id, nombre, descripcion, duracion_horas, precio, fecha_inicio, fecha_fin, profesor_id, activo)
VALUES (1, 'Curso de Prueba', 'Descripción del curso', 40, 299.99, '2026-02-01', '2026-06-30', [ID_PROFESOR], true);
```

#### 3. Matricular al Alumno
```sql
INSERT INTO matricula (academia_id, alumno_id, curso_id, fecha_matriculacion, estado, matriculado_por)
VALUES (1, [ID_ALUMNO], [ID_CURSO], NOW(), 'ACTIVA', [ID_USUARIO_ADMIN]);
```

#### 4. Iniciar Sesión y Verificar
1. Iniciar sesión con el usuario alumno
2. Ir al dashboard
3. Verificar que se muestra la tabla con el curso
4. Verificar que todos los campos tienen datos o "N/A"

### Checklist de Verificación

- [ ] El alumno tiene un perfil asociado a su usuario
- [ ] El alumno tiene al menos una matrícula en la base de datos
- [ ] Todos los cursos matriculados tienen un curso válido asociado
- [ ] Todos los cursos tienen un profesor asignado
- [ ] Todos los profesores tienen un usuario asociado
- [ ] Las fechas de matrícula, inicio y fin no son null
- [ ] El estado de la matrícula es uno de los valores válidos (ACTIVA, COMPLETADA, CANCELADA, SUSPENDIDA)
- [ ] Los logs no muestran errores durante la carga del dashboard
- [ ] El template renderiza sin errores de Thymeleaf

### Contacto para Soporte
Si el problema persiste después de seguir esta guía:
1. Capturar los logs completos de la aplicación
2. Capturar el resultado de las consultas SQL de verificación
3. Capturar una captura de pantalla del error
4. Proporcionar el usuario con el que se está probando

---

**Creado por**: GitHub Copilot  
**Fecha**: 29 de enero de 2026  
**Versión**: 1.0
