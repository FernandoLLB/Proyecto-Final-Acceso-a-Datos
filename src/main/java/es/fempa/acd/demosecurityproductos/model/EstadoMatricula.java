package es.fempa.acd.demosecurityproductos.model;

/**
 * Enumeración que define los posibles estados de una matrícula.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
public enum EstadoMatricula {
    /** La matrícula está activa y el alumno está cursando */
    ACTIVA,

    /** El alumno ha completado el curso */
    COMPLETADA,

    /** La matrícula ha sido cancelada */
    CANCELADA
}
