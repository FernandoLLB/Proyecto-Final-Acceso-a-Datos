package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una academia en el sistema SaaS.
 * Una academia pertenece a un propietario y puede tener múltiples usuarios, cursos, aulas y alumnos asociados.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Entity
@Table(name = "academia")
public class Academia {

    /** Identificador único de la academia */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre de la academia */
    @Column(nullable = false, length = 200)
    private String nombre;

    /** Indica si la academia está activa */
    @Column(nullable = false)
    private Boolean activa = true;

    /** Fecha y hora de alta de la academia en el sistema */
    @Column(nullable = false)
    private LocalDateTime fechaAlta;

    /** NIF o CIF de la academia */
    @Column(length = 20)
    private String nifCif;

    /** Email de contacto de la academia */
    @Column(length = 100)
    private String emailContacto;

    /** Teléfono de contacto de la academia */
    @Column(length = 20)
    private String telefono;

    /** Dirección física de la academia */
    @Column(length = 300)
    private String direccion;

    /** Propietario al que pertenece la academia */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "propietario_id", nullable = false)
    private Propietario propietario;

    /**
     * Constructor por defecto.
     * Inicializa la fecha de alta con la fecha actual y marca la academia como activa.
     */
    public Academia() {
        this.fechaAlta = LocalDateTime.now();
        this.activa = true;
    }

    // Getters y Setters

    /**
     * Obtiene el ID de la academia.
     * @return el ID de la academia
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la academia.
     * @param id el ID a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la academia.
     * @return el nombre de la academia
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la academia.
     * @param nombre el nombre a establecer
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Verifica si la academia está activa.
     * @return true si la academia está activa, false en caso contrario
     */
    public Boolean getActiva() {
        return activa;
    }

    /**
     * Establece el estado activo de la academia.
     * @param activa true para activar, false para desactivar
     */
    public void setActiva(Boolean activa) {
        this.activa = activa;
    }

    /**
     * Obtiene la fecha de alta de la academia.
     * @return la fecha y hora de alta
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Establece la fecha de alta de la academia.
     * @param fechaAlta la fecha y hora de alta a establecer
     */
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Obtiene el NIF o CIF de la academia.
     * @return el NIF/CIF de la academia
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Establece el NIF o CIF de la academia.
     * @param nifCif el NIF/CIF a establecer
     */
    public void setNifCif(String nifCif) {
        this.nifCif = nifCif;
    }

    /**
     * Obtiene el email de contacto de la academia.
     * @return el email de contacto
     */
    public String getEmailContacto() {
        return emailContacto;
    }

    /**
     * Establece el email de contacto de la academia.
     * @param emailContacto el email de contacto a establecer
     */
    public void setEmailContacto(String emailContacto) {
        this.emailContacto = emailContacto;
    }

    /**
     * Obtiene el teléfono de contacto de la academia.
     * @return el teléfono de contacto
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono de contacto de la academia.
     * @param telefono el teléfono a establecer
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección de la academia.
     * @return la dirección de la academia
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección de la academia.
     * @param direccion la dirección a establecer
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene el propietario de la academia.
     * @return el propietario de la academia
     */
    public Propietario getPropietario() {
        return propietario;
    }

    /**
     * Establece el propietario de la academia.
     * @param propietario el propietario a establecer
     */
    public void setPropietario(Propietario propietario) {
        this.propietario = propietario;
    }
}
