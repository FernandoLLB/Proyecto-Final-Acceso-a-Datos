package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa un alumno en una academia.
 * Un alumno está asociado a un usuario y puede matricularse en múltiples cursos.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Entity
@Table(name = "alumno")
public class Alumno {

    /** Identificador único del alumno */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario asociado al alumno (relación uno a uno) */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    /** Academia a la que pertenece el alumno */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    /** Fecha en que se registró el alumno */
    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    /** Estado general del alumno: ACTIVO, INACTIVO, COMPLETADO, SUSPENDIDO */
    @Column(name = "estado_matricula", length = 50)
    private String estadoMatricula;

    /** Observaciones o notas sobre el alumno */
    @Column(length = 1000)
    private String observaciones;

    /**
     * Constructor por defecto.
     * Inicializa la fecha de registro con la fecha actual y el estado como ACTIVO.
     */
    public Alumno() {
        this.fechaRegistro = LocalDate.now();
        this.estadoMatricula = "ACTIVO";
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Academia getAcademia() {
        return academia;
    }

    public void setAcademia(Academia academia) {
        this.academia = academia;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getEstadoMatricula() {
        return estadoMatricula;
    }

    public void setEstadoMatricula(String estadoMatricula) {
        this.estadoMatricula = estadoMatricula;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
