package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Entidad que representa la matrícula de un alumno en un curso.
 * Registra la relación entre un alumno y un curso, incluyendo
 * el estado de la matrícula y observaciones.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Entity
@Table(name = "matricula", indexes = {
    @Index(name = "idx_matricula_alumno", columnList = "alumno_id"),
    @Index(name = "idx_matricula_curso", columnList = "curso_id"),
    @Index(name = "idx_matricula_academia", columnList = "academia_id"),
    @Index(name = "idx_matricula_estado", columnList = "estado")
})
public class Matricula {

    /** Identificador único de la matrícula */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Academia a la que pertenece la matrícula */
    @NotNull(message = "La academia es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    /** Alumno que se matricula en el curso */
    @NotNull(message = "El alumno es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "alumno_id", nullable = false)
    private Alumno alumno;

    /** Curso en el que se matricula el alumno */
    @NotNull(message = "El curso es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    /** Fecha y hora en que se realizó la matrícula */
    @NotNull(message = "La fecha de matriculación es obligatoria")
    @Column(name = "fecha_matriculacion", nullable = false)
    private LocalDateTime fechaMatriculacion;

    /** Estado actual de la matrícula (ACTIVA, CANCELADA, COMPLETADA) */
    @NotNull(message = "El estado es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoMatricula estado;

    /** Observaciones o notas sobre la matrícula */
    @Column(length = 500)
    private String observaciones;

    /** Usuario que registró la matrícula (secretaria o propietario) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "matriculado_por")
    private Usuario matriculadoPor;

    /**
     * Constructor por defecto.
     * Inicializa la fecha de matrícula con la fecha actual y el estado como ACTIVA.
     */
    public Matricula() {
        this.fechaMatriculacion = LocalDateTime.now();
        this.estado = EstadoMatricula.ACTIVA;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public LocalDateTime getFechaMatriculacion() {
        return fechaMatriculacion;
    }

    public void setFechaMatriculacion(LocalDateTime fechaMatriculacion) {
        this.fechaMatriculacion = fechaMatriculacion;
    }

    public EstadoMatricula getEstado() {
        return estado;
    }

    public void setEstado(EstadoMatricula estado) {
        this.estado = estado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Usuario getMatriculadoPor() {
        return matriculadoPor;
    }

    public void setMatriculadoPor(Usuario matriculadoPor) {
        this.matriculadoPor = matriculadoPor;
    }
}
