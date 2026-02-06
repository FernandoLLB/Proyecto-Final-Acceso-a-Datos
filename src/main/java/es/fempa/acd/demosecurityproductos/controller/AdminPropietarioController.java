package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Propietario;
import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.service.PropietarioService;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para la gestión de propietarios por parte del administrador.
 * Solo accesible por usuarios con rol ADMIN.
 *
 * @author Sistema de Gestión de Academias
 * @version 2.0
 */
@Controller
@RequestMapping("/admin/propietarios")
@PreAuthorize("hasRole('ADMIN')")
public class AdminPropietarioController {

    private final PropietarioService propietarioService;
    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor del controlador de propietarios.
     *
     * @param propietarioService servicio de gestión de propietarios
     * @param usuarioService servicio de gestión de usuarios
     * @param passwordEncoder encoder para encriptar contraseñas
     */
    public AdminPropietarioController(PropietarioService propietarioService,
                                     UsuarioService usuarioService,
                                     PasswordEncoder passwordEncoder) {
        this.propietarioService = propietarioService;
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lista todos los propietarios del sistema.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de lista de propietarios
     */
    @GetMapping("/lista")
    public String listarPropietarios(Model model) {
        model.addAttribute("propietarios", propietarioService.listarTodos());
        return "admin/propietarios-lista";
    }

    /**
     * Muestra el formulario para crear un nuevo propietario.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario
     */
    @GetMapping("/nuevo")
    public String nuevoPropietarioForm(Model model) {
        model.addAttribute("propietario", new Propietario());
        model.addAttribute("usuario", new Usuario());
        return "admin/propietario-nuevo";
    }

    /**
     * Procesa la creación de un nuevo propietario.
     * Crea tanto el usuario como el propietario asociado.
     *
     * @param usuario datos del usuario a crear
     * @param propietario datos del propietario a crear
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de propietarios
     */
    @PostMapping("/crear")
    public String crearPropietario(@ModelAttribute("usuario") Usuario usuario,
                                   @ModelAttribute("propietario") Propietario propietario,
                                   RedirectAttributes redirectAttributes) {
        try {
            // Configurar el usuario
            usuario.setRol(Rol.PROPIETARIO);
            usuario.setActivo(true);
            usuario.setEmailVerificado(true); // El admin lo crea ya verificado
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
            usuario.setAcademia(null); // Los propietarios no tienen academia asignada directamente

            // Guardar el usuario
            Usuario usuarioGuardado = usuarioService.guardar(usuario);

            // Asociar el usuario al propietario y guardar
            propietario.setUsuario(usuarioGuardado);
            propietarioService.crear(propietario);

            redirectAttributes.addFlashAttribute("success", "Propietario creado exitosamente");
            return "redirect:/admin/propietarios/lista";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear propietario: " + e.getMessage());
            return "redirect:/admin/propietarios/nuevo";
        }
    }

    /**
     * Muestra el formulario para editar un propietario existente.
     *
     * @param id identificador del propietario
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del formulario de edición
     */
    @GetMapping("/{id}/editar")
    public String editarPropietarioForm(@PathVariable Long id, Model model) {
        Propietario propietario = propietarioService.obtenerPorId(id);
        model.addAttribute("propietario", propietario);
        model.addAttribute("academias", propietarioService.obtenerAcademias(id));
        return "admin/propietario-editar";
    }

    /**
     * Procesa la actualización de un propietario.
     *
     * @param id identificador del propietario
     * @param propietario datos actualizados del propietario
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de propietarios
     */
    @PostMapping("/{id}/actualizar")
    public String actualizarPropietario(@PathVariable Long id,
                                       @ModelAttribute Propietario propietario,
                                       RedirectAttributes redirectAttributes) {
        try {
            propietarioService.actualizar(id, propietario);
            redirectAttributes.addFlashAttribute("success", "Propietario actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar propietario: " + e.getMessage());
        }
        return "redirect:/admin/propietarios/lista";
    }

    /**
     * Activa un propietario desactivado.
     *
     * @param id identificador del propietario
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de propietarios
     */
    @PostMapping("/{id}/activar")
    public String activarPropietario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propietarioService.cambiarEstado(id, true);
            redirectAttributes.addFlashAttribute("success", "Propietario activado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al activar propietario: " + e.getMessage());
        }
        return "redirect:/admin/propietarios/lista";
    }

    /**
     * Desactiva un propietario activo.
     *
     * @param id identificador del propietario
     * @param redirectAttributes atributos para mensajes flash
     * @return redirección a la lista de propietarios
     */
    @PostMapping("/{id}/desactivar")
    public String desactivarPropietario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            propietarioService.cambiarEstado(id, false);
            redirectAttributes.addFlashAttribute("success", "Propietario desactivado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar propietario: " + e.getMessage());
        }
        return "redirect:/admin/propietarios/lista";
    }

    /**
     * Muestra el detalle de un propietario con sus academias.
     *
     * @param id identificador del propietario
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de detalle
     */
    @GetMapping("/{id}/detalle")
    public String detallePropietario(@PathVariable Long id, Model model) {
        Propietario propietario = propietarioService.obtenerPorId(id);
        model.addAttribute("propietario", propietario);
        model.addAttribute("academias", propietarioService.obtenerAcademias(id));
        model.addAttribute("totalAcademias", propietarioService.contarAcademias(id));
        return "admin/propietario-detalle";
    }
}
