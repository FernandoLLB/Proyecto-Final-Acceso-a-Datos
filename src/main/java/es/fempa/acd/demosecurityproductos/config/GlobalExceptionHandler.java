package es.fempa.acd.demosecurityproductos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice(basePackages = "es.fempa.acd.demosecurityproductos.controller")
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidationException(MethodArgumentNotValidException ex, Model model, HttpServletRequest request) {
        logger.warn("Error de validación en {}: {}", request.getRequestURI(), ex.getBindingResult().getAllErrors());
        model.addAttribute("error", "Error de validación en el formulario");
        model.addAttribute("details", ex.getBindingResult().getAllErrors());
        return "error/400";
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUsernameNotFoundException(UsernameNotFoundException ex, Model model, HttpServletRequest request) {
        logger.warn("Usuario no encontrado en {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("error", "Usuario no encontrado");
        return "error/404";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException ex, Model model, HttpServletRequest request) {
        logger.warn("Argumento inválido en {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/400";
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleIllegalStateException(IllegalStateException ex, Model model, HttpServletRequest request) {
        logger.warn("Estado inválido en {}: {}", request.getRequestURI(), ex.getMessage());
        model.addAttribute("error", ex.getMessage());
        return "error/409";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, Model model, HttpServletRequest request) {
        logger.error("Violación de integridad de datos en {}", request.getRequestURI(), ex);
        model.addAttribute("error", "Error al guardar los datos. Puede existir información duplicada.");
        return "error/409";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model, HttpServletRequest request) {
        logger.warn("Acceso denegado en {} para usuario: {}", request.getRequestURI(), request.getRemoteUser());
        model.addAttribute("error", "No tienes permisos para acceder a esta página");
        return "error/403";
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(NoResourceFoundException ex, Model model, HttpServletRequest request) {
        logger.warn("Recurso no encontrado: {}", request.getRequestURI());
        model.addAttribute("error", "La página solicitada no existe");
        return "error/404";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception ex, Model model, HttpServletRequest request) {
        logger.error("Error inesperado en {}", request.getRequestURI(), ex);
        model.addAttribute("error", "Ha ocurrido un error inesperado. Por favor, contacte al administrador.");
        model.addAttribute("timestamp", System.currentTimeMillis());
        return "error/500";
    }
}
