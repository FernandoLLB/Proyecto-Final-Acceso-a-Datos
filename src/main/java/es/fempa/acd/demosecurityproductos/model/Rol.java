package es.fempa.acd.demosecurityproductos.model;

/**
 * Enumeración que define los diferentes roles de usuario en el sistema.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public enum Rol {
    /** Administrador del sistema con acceso completo a todas las academias */
    ADMIN,

    /** Propietario de una academia con permisos de gestión completos en su academia */
    PROPIETARIO,

    /** Secretaria de una academia con permisos de gestión de matrículas y alumnos */
    SECRETARIA,

    /** Profesor de una academia con permisos para gestionar cursos y reservas de aulas */
    PROFESOR,

    /** Alumno registrado en una academia con permisos para ver sus cursos y matrículas */
    ALUMNO
}
