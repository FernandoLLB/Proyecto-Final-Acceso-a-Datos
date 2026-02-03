package es.fempa.acd.demosecurityproductos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración de CORS (Cross-Origin Resource Sharing) para la aplicación.
 * Permite el acceso a la API REST desde diferentes orígenes.
 *
 * @author Sistema de Gestión de Academias
 * @version 1.0
 */
@Configuration
public class CorsConfig {

    /**
     * Configura las políticas de CORS para la aplicación.
     * Permite solicitudes desde cualquier origen, método y cabecera.
     * Habilita el envío de credenciales (cookies, headers de autenticación).
     *
     * @return la configuración CORS a aplicar en toda la aplicación
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Ajusta dominios, métodos y cabeceras permitidos según tu caso.
        configuration.addAllowedOriginPattern("*"); // Usar addAllowedOriginPattern en lugar de addAllowedOrigin con credenciales
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        // Si necesitas credenciales (cookies, auth headers), habilita:
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
