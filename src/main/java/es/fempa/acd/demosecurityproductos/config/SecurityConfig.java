package es.fempa.acd.demosecurityproductos.config;

import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad de Spring Security.
 * Define las reglas de autorización, autenticación y configuración de sesiones.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Configuration
@EnableMethodSecurity // Habilita seguridad en métodos como @PreAuthorize
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    /**
     * Constructor de la configuración de seguridad.
     *
     * @param userDetailsService servicio personalizado de detalles de usuario
     */
    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configura la cadena de filtros de seguridad.
     * Define rutas protegidas por rol, login, logout y gestión de sesiones.
     *
     * @param http configurador de seguridad HTTP
     * @return la cadena de filtros de seguridad configurada
     * @throws Exception si hay un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http	
                .cors(Customizer.withDefaults())
                // CSRF habilitado con configuración por defecto (Thymeleaf añade tokens automáticamente)
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole(Rol.ADMIN.name())
                        .requestMatchers("/propietario/**").hasRole(Rol.PROPIETARIO.name())
                        .requestMatchers("/secretaria/**").hasRole(Rol.SECRETARIA.name())
                        .requestMatchers("/profesor/**").hasRole(Rol.PROFESOR.name())
                        .requestMatchers("/alumno/**").hasRole(Rol.ALUMNO.name())
                        .requestMatchers("/profesores/**").hasAnyRole(
                                Rol.ADMIN.name(),
                                Rol.PROPIETARIO.name(),
                                Rol.SECRETARIA.name()
                        )
                        .requestMatchers("/css/**", "/js/**", "/locale/**", "/registro", "/registro/**",
                                "/verificar-email", "/reenviar-verificacion").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/default", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                )
                .sessionManagement(session -> session
                        .maximumSessions(1)
                        .expiredUrl("/login?expired=true")
                );

        return http.build();
    }

    /**
     * Configura el codificador de contraseñas.
     * Utiliza BCrypt para el hash de contraseñas.
     *
     * @return el codificador de contraseñas BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el proveedor de autenticación DAO.
     * Conecta el servicio de detalles de usuario con el codificador de contraseñas.
     *
     * @return el proveedor de autenticación configurado
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Proporciona el gestor de autenticación.
     *
     * @param authConfig configuración de autenticación
     * @return el gestor de autenticación
     * @throws Exception si hay un error al obtener el gestor
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
