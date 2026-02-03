package es.fempa.acd.demosecurityproductos.model;

import jakarta.persistence.*;

/**
 * Entidad que representa un usuario del sistema de gestión de academias.
 * 
 * <p>Un usuario puede tener diferentes roles (ADMIN, PROPIETARIO, SECRETARIA, PROFESOR, ALUMNO)
 * y está asociado a una academia (excepto los administradores que pueden ser independientes).</p>
 * 
 * <p>Los usuarios deben verificar su email antes de poder acceder completamente al sistema.</p>
 * 
 * @author Sistema Gestor de Academias
 * @version 1.0
 * @since 1.0
 */
@Entity
@Table(name = "usuario")
public class Usuario {

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único para autenticación.
     * No puede ser nulo y debe ser único en el sistema.
     */
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    /**
     * Contraseña cifrada del usuario.
     * Se almacena usando BCrypt para mayor seguridad.
     */
    @Column(nullable = false)
    private String password;

    /**
     * Correo electrónico único del usuario.
     * Se utiliza para verificación de cuenta y comunicaciones.
     */
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    /**
     * Nombre real del usuario.
     */
    @Column(length = 100)
    private String nombre;

    /**
     * Apellidos del usuario.
     */
    @Column(length = 100)
    private String apellidos;

    /**
     * Rol del usuario en el sistema.
     * Determina los permisos y funcionalidades disponibles.
     * 
     * @see Rol
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    /**
     * Indica si el usuario está activo en el sistema.
     * Los usuarios inactivos no pueden autenticarse.
     */
    @Column(nullable = false)
    private Boolean activo = true;

    /**
     * Indica si el usuario ha verificado su email.
     * Requerido para acceso completo al sistema.
     */
    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado = false;

    /**
     * Academia a la que pertenece el usuario.
     * Puede ser null solo para usuarios con rol ADMIN.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "academia_id", nullable = true) // nullable solo para ADMIN
    private Academia academia;

    /**
     * Constructor por defecto.
     * Inicializa el usuario con valores predeterminados (activo y email no verificado).
     */
    public Usuario() {
        this.activo = true;
        this.emailVerificado = false;
    }

    // Getters y Setters
    
    /**
     * Obtiene el identificador único del usuario.
     * 
     * @return ID del usuario
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del usuario.
     * 
     * @param id ID del usuario
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuario.
     * 
     * @return nombre de usuario único
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario.
     * 
     * @param username nombre de usuario único
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña cifrada.
     * 
     * @return contraseña cifrada con BCrypt
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña del usuario.
     * Debe ser cifrada antes de almacenarse.
     * 
     * @param password contraseña cifrada
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Obtiene el correo electrónico del usuario.
     * 
     * @return email del usuario
     */
    public String getEmail() {
        return email;
    }

    /**
     * Establece el correo electrónico del usuario.
     * 
     * @param email correo electrónico único
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Obtiene el nombre real del usuario.
     * 
     * @return nombre del usuario
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre real del usuario.
     * 
     * @param nombre nombre del usuario
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos del usuario.
     * 
     * @return apellidos del usuario
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del usuario.
     * 
     * @param apellidos apellidos del usuario
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el rol del usuario en el sistema.
     * 
     * @return rol del usuario
     * @see Rol
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Establece el rol del usuario.
     * 
     * @param rol rol del usuario en el sistema
     * @see Rol
     */
    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Verifica si el usuario está activo.
     * 
     * @return true si el usuario está activo, false en caso contrario
     */
    public Boolean getActivo() {
        return activo;
    }

    /**
     * Establece el estado de activación del usuario.
     * 
     * @param activo true para activar, false para desactivar
     */
    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    /**
     * Verifica si el usuario ha verificado su email.
     * 
     * @return true si el email está verificado, false en caso contrario
     */
    public Boolean getEmailVerificado() {
        return emailVerificado;
    }

    /**
     * Establece el estado de verificación del email.
     * 
     * @param emailVerificado true si está verificado, false en caso contrario
     */
    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }

    /**
     * Obtiene la academia a la que pertenece el usuario.
     * 
     * @return academia del usuario, puede ser null para ADMIN
     */
    public Academia getAcademia() {
        return academia;
    }

    /**
     * Establece la academia a la que pertenece el usuario.
     * 
     * @param academia academia del usuario
     */
    public void setAcademia(Academia academia) {
        this.academia = academia;
    }
}
