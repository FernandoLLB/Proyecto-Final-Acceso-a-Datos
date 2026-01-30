package es.fempa.acd.demosecurityproductos.dto;

public class RegistroAlumnoDTO {

    private String username;
    private String password;
    private String confirmPassword;
    private String email;
    private String nombre;
    private String apellidos;
    private Long academiaId;

    public RegistroAlumnoDTO() {
    }

    // Getters y Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Long getAcademiaId() {
        return academiaId;
    }

    public void setAcademiaId(Long academiaId) {
        this.academiaId = academiaId;
    }
}
