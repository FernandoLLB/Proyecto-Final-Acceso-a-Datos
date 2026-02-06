package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.service.PropietarioService;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * Controlador para la gestión de secretarias por parte de los propietarios.
 * Los propietarios pueden crear y gestionar secretarias únicamente para sus propias academias.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Controller
@RequestMapping("/propietario/secretarias")
@PreAuthorize("hasRole('PROPIETARIO')")
public class PropietarioGestionSecretariaController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final AcademiaService academiaService;
    private final PropietarioService propietarioService;
    private final UsuarioRepository usuarioRepository;

    public PropietarioGestionSecretariaController(UsuarioService usuarioService,
                                                 SecurityUtils securityUtils,
                                                 AcademiaService academiaService,
                                                 PropietarioService propietarioService,
                                                 UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.academiaService = academiaService;
        this.propietarioService = propietarioService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Lista las secretarias de las academias del propietario autenticado.
     */
    @GetMapping
    public String listarSecretarias(@RequestParam(required = false, defaultValue = "true") Boolean soloActivas,
                                    Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Obtener IDs de las academias del propietario
        List<Long> academiaIds = propietarioService.obtenerAcademiasActivas(propietario.getId())
                .stream()
                .map(Academia::getId)
                .toList();

        // Obtener secretarias de esas academias
        List<Usuario> secretarias;
        if (academiaIds.isEmpty()) {
            secretarias = List.of();
        } else {
            secretarias = usuarioRepository.findAll().stream()
                    .filter(u -> u.getRol() == Rol.SECRETARIA)
                    .filter(u -> u.getAcademia() != null && academiaIds.contains(u.getAcademia().getId()))
                    .filter(u -> !soloActivas || u.getActivo())
                    .toList();
        }

        model.addAttribute("secretarias", secretarias);
        model.addAttribute("soloActivas", soloActivas);
        return "propietario/secretarias-lista";
    }

    /**
     * Muestra el formulario para crear una nueva secretaria.
     */
    @GetMapping("/nueva")
    public String nuevaSecretariaForm(Model model) {
        Usuario usuario = securityUtils.getUsuarioAutenticado();
        Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

        // Cargar solo las academias activas del propietario
        List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());

        if (academias.isEmpty()) {
            model.addAttribute("error", "No tienes academias activas. Debes tener al menos una academia para crear secretarias.");
        }

        model.addAttribute("academias", academias);
        model.addAttribute("usuario", new Usuario());
        return "propietario/secretaria-nueva";
    }

    /**
     * Crea una nueva secretaria asignada a una academia del propietario.
     */
    @PostMapping("/crear")
    public String crearSecretaria(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email,
                                 @RequestParam String nombre,
                                 @RequestParam String apellidos,
                                 @RequestParam Long academiaId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            // Verificar que la academia pertenece al propietario
            Academia academia = academiaService.obtenerPorId(academiaId);
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                throw new IllegalArgumentException("No tienes permisos para asignar secretarias a esta academia");
            }

            // Crear usuario con rol SECRETARIA
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.SECRETARIA);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(academia);
            nuevoUsuario.setEmailVerificado(true); // Verificar email automáticamente
            usuarioService.actualizar(nuevoUsuario);

            redirectAttributes.addFlashAttribute("success", "Secretaria creada exitosamente");
            return "redirect:/propietario/secretarias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("academias", academias);
            model.addAttribute("usuario", new Usuario());
            return "propietario/secretaria-nueva";
        }
    }

    /**
     * Muestra el formulario para editar una secretaria.
     */
    @GetMapping("/{id}/editar")
    public String editarSecretariaForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Verificar que la secretaria pertenece a una academia del propietario
            if (secretaria.getAcademia() == null ||
                !secretaria.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar esta secretaria");
                return "redirect:/propietario/secretarias";
            }

            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("secretaria", secretaria);
            model.addAttribute("academias", academias);
            return "propietario/secretaria-editar";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", "Secretaria no encontrada");
            return "redirect:/propietario/secretarias";
        }
    }

    /**
     * Actualiza los datos de una secretaria.
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarSecretaria(@PathVariable Long id,
                                      @RequestParam String nombre,
                                      @RequestParam String apellidos,
                                      @RequestParam String email,
                                      @RequestParam Long academiaId,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Verificar que la academia pertenece al propietario
            Academia academia = academiaService.obtenerPorId(academiaId);
            if (!academia.getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para asignar secretarias a esta academia");
                return "redirect:/propietario/secretarias";
            }

            // Actualizar datos
            secretaria.setNombre(nombre);
            secretaria.setApellidos(apellidos);
            secretaria.setEmail(email);
            secretaria.setAcademia(academia);

            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria actualizada exitosamente");
            return "redirect:/propietario/secretarias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);
            List<Academia> academias = propietarioService.obtenerAcademiasActivas(propietario.getId());
            model.addAttribute("secretaria", secretaria);
            model.addAttribute("academias", academias);
            return "propietario/secretaria-editar";
        }
    }

    /**
     * Desactiva una secretaria.
     */
    @PostMapping("/{id}/eliminar")
    public String eliminarSecretaria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Verificar que la secretaria pertenece a una academia del propietario
            if (secretaria.getAcademia() == null ||
                !secretaria.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para desactivar esta secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Desactivar en lugar de eliminar
            secretaria.setActivo(false);
            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar la secretaria: " + e.getMessage());
        }
        return "redirect:/propietario/secretarias";
    }

    /**
     * Reactiva una secretaria.
     */
    @PostMapping("/{id}/reactivar")
    public String reactivarSecretaria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = securityUtils.getUsuarioAutenticado();
            Propietario propietario = propietarioService.obtenerPorUsuario(usuario)
                    .orElseThrow(() -> new RuntimeException("Propietario no encontrado"));

            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Verificar que la secretaria pertenece a una academia del propietario
            if (secretaria.getAcademia() == null ||
                !secretaria.getAcademia().getPropietario().getId().equals(propietario.getId())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para reactivar esta secretaria");
                return "redirect:/propietario/secretarias";
            }

            // Reactivar el usuario
            secretaria.setActivo(true);
            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria reactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reactivar la secretaria: " + e.getMessage());
        }
        return "redirect:/propietario/secretarias";
    }
}
