package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_verificacion")
public class TokenVerificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    public TokenVerificacion() {
        this.fechaCreacion = LocalDateTime.now();
        // Token válido por 24 horas
        this.fechaExpiracion = LocalDateTime.now().plusHours(24);
    }

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
