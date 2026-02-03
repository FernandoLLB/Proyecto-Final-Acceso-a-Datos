package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa un token de verificación de email.
 * Se genera cuando un usuario se registra y se envía por correo para verificar su email.
 * El token tiene una validez de 24 horas.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Entity
@Table(name = "token_verificacion")
public class TokenVerificacion {

    /** Identificador único del token */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Token único generado aleatoriamente */
    @Column(nullable = false, unique = true)
    private String token;

    /** Usuario asociado al token */
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    /** Fecha y hora de expiración del token (24 horas desde su creación) */
    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    /** Fecha y hora de creación del token */
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Constructor por defecto.
     * Inicializa la fecha de creación y establece la expiración en 24 horas.
     */
    public TokenVerificacion() {
        this.fechaCreacion = LocalDateTime.now();
        // Token válido por 24 horas
        this.fechaExpiracion = LocalDateTime.now().plusHours(24);
    }

    /**
     * Constructor con token y usuario.
     *
     * @param token el token de verificación único
     * @param usuario el usuario asociado al token
     */
    public TokenVerificacion(String token, Usuario usuario) {
        this();
        this.token = token;
        this.usuario = usuario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isExpirado() {
        return LocalDateTime.now().isAfter(fechaExpiracion);
    }
}
