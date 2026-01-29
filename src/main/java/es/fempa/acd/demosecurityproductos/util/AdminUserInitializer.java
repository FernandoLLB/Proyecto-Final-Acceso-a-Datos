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
 * Inicializador de usuario admin
 * Este componente asegura que siempre exista un usuario admin con la contraseña correcta
 */
@Configuration
public class AdminUserInitializer {

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

                System.out.println("╔════════════════════════════════════════════════════════════╗");
                System.out.println("║  ✅ USUARIO ADMIN CREADO EXITOSAMENTE                      ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║  Usuario:    admin                                         ║");
                System.out.println("║  Contraseña: admin123                                      ║");
                System.out.println("║  URL:        http://localhost:8090/login                   ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
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

                System.out.println("╔════════════════════════════════════════════════════════════╗");
                System.out.println("║  🔄 CONTRASEÑA DE ADMIN ACTUALIZADA                        ║");
                System.out.println("╠════════════════════════════════════════════════════════════╣");
                System.out.println("║  Usuario:    admin                                         ║");
                System.out.println("║  Contraseña: admin123                                      ║");
                System.out.println("║  URL:        http://localhost:8090/login                   ║");
                System.out.println("║                                                            ║");
                System.out.println("║  ℹ️  El hash BCrypt ha sido regenerado                     ║");
                System.out.println("╚════════════════════════════════════════════════════════════╝");
            }

            // Mostrar el hash generado para debugging
            System.out.println("\n🔐 Hash BCrypt generado: " + hashedPassword);

            // Verificar que el hash funciona
            boolean matches = encoder.matches(plainPassword, hashedPassword);
            System.out.println("✅ Verificación del hash: " + (matches ? "CORRECTO ✓" : "ERROR ✗"));
            System.out.println();
        };
    }
}
