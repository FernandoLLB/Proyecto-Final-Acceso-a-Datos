package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un propietario (cliente) del sistema SaaS.
 * Un propietario puede gestionar múltiples academias y está asociado a un usuario con rol PROPIETARIO.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Entity
@Table(name = "propietario")
public class Propietario {

    /** Identificador único del propietario */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Usuario asociado al propietario (relación uno a uno) */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    /** NIF o CIF del propietario */
    @Column(name = "nif_cif", length = 20)
    private String nifCif;

    /** Razón social o nombre comercial del propietario */
    @Column(name = "razon_social", length = 300)
    private String razonSocial;

    /** Fecha y hora de alta del propietario en el sistema */
    @Column(name = "fecha_alta", nullable = false)
    private LocalDateTime fechaAlta;

    /** Indica si el propietario está activo */
    @Column(nullable = false)
    private Boolean activo = true;

    /** Teléfono de contacto del propietario */
    @Column(length = 20)
    private String telefono;

    /** Dirección del propietario */
    @Column(length = 300)
    private String direccion;

    /** Lista de academias gestionadas por este propietario */
    @OneToMany(mappedBy = "propietario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Academia> academias = new ArrayList<>();

    /**
     * Constructor por defecto.
     * Inicializa la fecha de alta con la fecha actual y marca el propietario como activo.
     */
    public Propietario() {
        this.fechaAlta = LocalDateTime.now();
        this.activo = true;
    }

    // Getters y Setters

    /**
     * Obtiene el ID del propietario.
     * @return el ID del propietario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del propietario.
     * @param id el ID a establecer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario asociado al propietario.
     * @return el usuario asociado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario asociado al propietario.
     * @param usuario el usuario a asociar
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el NIF o CIF del propietario.
     * @return el NIF/CIF del propietario
     */
    public String getNifCif() {
        return nifCif;
    }

    /**
     * Establece el NIF o CIF del propietario.
     * @param nifCif el NIF/CIF a establecer
     */
    public void setNifCif(String nifCif) {
        this.nifCif = nifCif;
    }

    /**
     * Obtiene la razón social del propietario.
     * @return la razón social
     */
    public String getRazonSocial() {
        return razonSocial;
    }

    /**
     * Establece la razón social del propietario.
     * @param razonSocial la razón social a establecer
     */
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    /**
     * Obtiene la fecha de alta del propietario.
     * @return la fecha y hora de alta
     */
    public LocalDateTime getFechaAlta() {
        return fechaAlta;
    }

    /**
     * Establece la fecha de alta del propietario.
     * @param fechaAlta la fecha y hora de alta a establecer
     */
    public void setFechaAlta(LocalDateTime fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    /**
     * Verifica si el propietario está activo.
     * @return true si el propietario está activo, false en caso contrario
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el estado activo del propietario.
     * @param activo true para activar, false para desactivar
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Obtiene el teléfono del propietario.
     * @return el teléfono
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del propietario.
     * @param telefono el teléfono a establecer
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene la dirección del propietario.
     * @return la dirección
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Establece la dirección del propietario.
     * @param direccion la dirección a establecer
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Obtiene la lista de academias del propietario.
     * @return lista de academias
     */
    public List<Academia> getAcademias() {
        return academias;
    }

    /**
     * Establece la lista de academias del propietario.
     * @param academias la lista de academias a establecer
     */
    public void setAcademias(List<Academia> academias) {
        this.academias = academias;
    }
}
