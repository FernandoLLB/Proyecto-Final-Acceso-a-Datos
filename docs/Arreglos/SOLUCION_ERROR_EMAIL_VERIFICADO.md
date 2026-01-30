# Solución al Error: Field 'email_verificado' doesn't have a default value

## Problema
Al intentar registrar un nuevo alumno, se producía el siguiente error:
```
SQL Error: 1364, SQLState: HY000
Field 'email_verificado' doesn't have a default value
```

## Causa
La tabla `usuario` en la base de datos MySQL contiene un campo `email_verificado` de tipo BOOLEAN que es NOT NULL y no tiene valor por defecto. Sin embargo, la entidad JPA `Usuario` no tenía mapeado este campo, por lo que al intentar insertar un nuevo usuario, Hibernate no incluía este campo en el INSERT y MySQL rechazaba la operación.

## Solución Implementada

### 1. Modificación de la Entidad Usuario
Se agregó el campo `emailVerificado` a la clase `Usuario.java`:

```java
@Column(name = "email_verificado", nullable = false)
private Boolean emailVerificado = false;
```

### 2. Inicialización en Constructor
Se modificó el constructor para inicializar el campo:

```java
public Usuario() {
    this.activo = true;
    this.emailVerificado = false;
}
```

### 3. Getters y Setters
Se agregaron los métodos de acceso:

```java
public Boolean getEmailVerificado() {
    return emailVerificado;
}

public void setEmailVerificado(Boolean emailVerificado) {
    this.emailVerificado = emailVerificado;
}
```

## Archivos Modificados
- `src/main/java/es/fempa/acd/demosecurityproductos/model/Usuario.java`
- `docs/IMPLEMENTACION_REGISTRO_ALUMNOS.md` (documentación actualizada)

## Archivos Creados
- `docs/SQL_AGREGAR_EMAIL_VERIFICADO.sql` (script para actualizar base de datos existentes)

## Verificación
Después de aplicar estos cambios:
1. La compilación fue exitosa (`mvn clean compile`)
2. El campo `emailVerificado` ahora se incluye en todos los INSERT de usuarios
3. Todos los nuevos usuarios se crean con `email_verificado = FALSE`

## Para Bases de Datos Existentes
Si ya tienes una base de datos con la tabla `usuario` pero sin el campo `email_verificado`, ejecuta el script:
```sql
ALTER TABLE usuario 
ADD COLUMN IF NOT EXISTS email_verificado BOOLEAN NOT NULL DEFAULT FALSE;
```

## Comportamiento Actual
- **Registro de nuevos alumnos**: El campo se establece automáticamente en `FALSE`
- **Usuarios existentes**: Deben tener el campo configurado (usar script SQL)
- **Futura funcionalidad**: Este campo está preparado para implementar verificación de email por correo electrónico

## Fecha de Corrección
30 de Enero de 2026
