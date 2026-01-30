package es.fempa.acd.demosecurityproductos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.base.url}")
    private String baseUrl;

    /**
     * Envía un correo de verificación al usuario
     */
    public void enviarEmailVerificacion(String destinatario, String nombreUsuario, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("Verificación de cuenta - Gestor de Academias");

            String linkVerificacion = baseUrl + "/verificar-email?token=" + token;

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #f9f9f9;
                        }
                        .header {
                            background-color: #4a90e2;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            background-color: white;
                            padding: 30px;
                            border-radius: 0 0 5px 5px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 30px;
                            margin: 20px 0;
                            background-color: #4a90e2;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            font-size: 12px;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Gestor de Academias</h1>
                        </div>
                        <div class="content">
                            <h2>¡Bienvenido, """ + nombreUsuario + """
                !</h2>
                            <p>Gracias por registrarte en nuestro Gestor de Academias. Para activar tu cuenta, necesitamos que verifiques tu dirección de correo electrónico.</p>
                            <p>Por favor, haz clic en el siguiente botón para verificar tu cuenta:</p>
                            <div style="text-align: center;">
                                <a href=\"""" + linkVerificacion + """
                " class="button">Verificar mi cuenta</a>
                            </div>
                            <p>Si el botón no funciona, copia y pega este enlace en tu navegador:</p>
                            <p style="word-break: break-all; color: #4a90e2;">""" + linkVerificacion + """
                </p>
                            <p><strong>Importante:</strong> Este enlace es válido por 24 horas.</p>
                            <p>Si no has creado una cuenta en nuestro sistema, puedes ignorar este correo.</p>
                        </div>
                        <div class="footer">
                            <p>© 2026 Gestor de Academias. Todos los derechos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo de verificación: " + e.getMessage(), e);
        }
    }

    /**
     * Envía un correo de bienvenida después de la verificación
     */
    public void enviarEmailBienvenida(String destinatario, String nombreCompleto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(destinatario);
            helper.setSubject("¡Cuenta verificada! - Gestor de Academias");

            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <style>
                        body {
                            font-family: Arial, sans-serif;
                            line-height: 1.6;
                            color: #333;
                        }
                        .container {
                            max-width: 600px;
                            margin: 0 auto;
                            padding: 20px;
                            background-color: #f9f9f9;
                        }
                        .header {
                            background-color: #28a745;
                            color: white;
                            padding: 20px;
                            text-align: center;
                            border-radius: 5px 5px 0 0;
                        }
                        .content {
                            background-color: white;
                            padding: 30px;
                            border-radius: 0 0 5px 5px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 30px;
                            margin: 20px 0;
                            background-color: #28a745;
                            color: white;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                        }
                        .footer {
                            text-align: center;
                            padding: 20px;
                            font-size: 12px;
                            color: #777;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>✓ Cuenta Verificada</h1>
                        </div>
                        <div class="content">
                            <h2>¡Hola """ + nombreCompleto + """
                !</h2>
                            <p>Tu cuenta ha sido verificada exitosamente. Ya puedes acceder a todas las funcionalidades del Gestor de Academias.</p>
                            <div style="text-align: center;">
                                <a href=\"""" + baseUrl + """
                /login" class="button">Iniciar Sesión</a>
                            </div>
                            <p>Esperamos que disfrutes de tu experiencia en nuestra plataforma.</p>
                        </div>
                        <div class="footer">
                            <p>© 2026 Gestor de Academias. Todos los derechos reservados.</p>
                        </div>
                    </div>
                </body>
                </html>
                """;

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            // No lanzamos excepción aquí para no interrumpir el flujo
            System.err.println("Error al enviar el correo de bienvenida: " + e.getMessage());
        }
    }
}
