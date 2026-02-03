package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidad que representa un curso impartido en una academia.
 * Un curso está asociado a una academia específica, tiene un profesor asignado,
 * y puede tener múltiples alumnos matriculados.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Entity
@Table(name = "curso", indexes = {
    @Index(name = "idx_curso_academia", columnList = "academia_id"),
    @Index(name = "idx_curso_profesor", columnList = "profesor_id"),
    @Index(name = "idx_curso_fechas", columnList = "fecha_inicio, fecha_fin")
})
public class Curso {

    /** Identificador único del curso */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Academia a la que pertenece el curso */
    @NotNull(message = "La academia es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    /** Nombre del curso */
    @NotBlank(message = "El nombre del curso es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    @Column(nullable = false, length = 200)
    private String nombre;

    /** Descripción detallada del curso */
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    @Column(length = 1000)
    private String descripcion;

    /** Duración del curso en horas */
    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 hora")
    @Column(nullable = false)
    private Integer duracionHoras;

    /** Precio del curso */
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;

    /** Fecha de inicio del curso */
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    /** Fecha de finalización del curso */
    @NotNull(message = "La fecha de fin es obligatoria")
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    /** Categoría o área temática del curso */
    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    @Column(length = 100)
    private String categoria;

    /** Profesor que imparte el curso */
    @NotNull(message = "El profesor es obligatorio")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profesor_id", nullable = false)
    private Profesor profesor;

    /** Número de plazas disponibles para el curso */
    @Min(value = 0, message = "Las plazas no pueden ser negativas")
    @Column(name = "plazas_disponibles")
    private Integer plazasDisponibles;

    /** Indica si el curso está activo */
    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Constructor por defecto.
     * Inicializa el curso como activo.
     */
    public Curso() {
        this.activo = true;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDuracionHoras() {
        return duracionHoras;
    }

    public void setDuracionHoras(Integer duracionHoras) {
        this.duracionHoras = duracionHoras;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Integer getPlazasDisponibles() {
        return plazasDisponibles;
    }

    public void setPlazasDisponibles(Integer plazasDisponibles) {
        this.plazasDisponibles = plazasDisponibles;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
