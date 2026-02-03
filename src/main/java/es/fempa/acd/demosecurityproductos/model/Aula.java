package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Entidad que representa un aula en una academia.
 * Las aulas pueden ser reservadas por profesores para impartir sus cursos.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Entity
@Table(name = "aula", indexes = {
    @Index(name = "idx_aula_academia", columnList = "academia_id"),
    @Index(name = "idx_aula_academia_activa", columnList = "academia_id, activa")
})
public class Aula {

    /** Identificador único del aula */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Academia a la que pertenece el aula */
    @NotNull(message = "La academia es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = false)
    private Academia academia;

    /** Nombre o identificador del aula */
    @NotBlank(message = "El nombre del aula es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    /** Capacidad máxima de personas en el aula */
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    @Column(nullable = false)
    private Integer capacidad;

    /** Indica si el aula está activa y disponible para reservas */
    @Column(nullable = false)
    private Boolean activa = true;

    /** Descripción de los recursos disponibles en el aula (proyector, pizarra, etc.) */
    @Size(max = 500, message = "Los recursos no pueden exceder 500 caracteres")
    @Column(length = 500)
    private String recursos;

    /**
     * Constructor por defecto.
     * Inicializa el aula como activa.
     */
    public Aula() {
        this.activa = true;
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

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public Boolean getActiva() {
        return activa;
    }

    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    public String getRecursos() {
        return recursos;
    }

    public void setRecursos(String recursos) {
        this.recursos = recursos;
    }
}
