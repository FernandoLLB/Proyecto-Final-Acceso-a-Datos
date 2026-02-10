package es.fempa.acd.demosecurityproductos.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;

/**
 * Inicializador de usuario administrador del sistema.
 * Este componente asegura que siempre exista un usuario admin con la contraseña correcta
 * al iniciar la aplicación. Si el usuario no existe, lo crea automáticamente.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Configuration
public class AdminUserInitializer {

    /**
     * Crea un CommandLineRunner que inicializa el usuario administrador.
     * Se ejecuta automáticamente al iniciar la aplicación.
     *
     * @param usuarioRepository repositorio de usuarios
     * @return CommandLineRunner que inicializa el admin
     */
    @Bean
    public CommandLineRunner initAdminUser(UsuarioRepository usuarioRepository) {
        return args -> {
            PasswordEncoder encoder = new BCryptPasswordEncoder();

            // Contraseña para el admin
            String plainPassword = "admin123";
            String hashedPassword = encoder.encode(plainPassword);

            // Buscar si existe el usuario admin
            Usuario admin = usuarioRepository.findByUsername("admin").orElse(null);

            if (admin == null) {
                // Crear nuevo usuario admin
                admin = new Usuario();
                admin.setUsername("admin");
                admin.setEmail("admin@sistema.com");
                admin.setNombre("Administrador");
                admin.setApellidos("del Sistema");
                admin.setRol(Rol.ADMIN);
                admin.setActivo(true);
                admin.setAcademia(null);
                admin.setPassword(hashedPassword);
                usuarioRepository.save(admin);
            } else {
                // Actualizar la contraseña del admin existente
                admin.setPassword(hashedPassword);
                admin.setActivo(true);
                admin.setRol(Rol.ADMIN);
                admin.setEmail("admin@sistema.com");
                admin.setNombre("Administrador");
                admin.setApellidos("del Sistema");
                admin.setAcademia(null);
                usuarioRepository.save(admin);
            }
        };
    }
}
