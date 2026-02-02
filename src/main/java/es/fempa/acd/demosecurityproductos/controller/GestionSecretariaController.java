package es.fempa.acd.demosecurityproductos.controller;

import es.fempa.acd.demosecurityproductos.model.Usuario;
import es.fempa.acd.demosecurityproductos.model.Rol;
import es.fempa.acd.demosecurityproductos.model.Academia;
import es.fempa.acd.demosecurityproductos.service.UsuarioService;
import es.fempa.acd.demosecurityproductos.service.SecurityUtils;
import es.fempa.acd.demosecurityproductos.service.AcademiaService;
import es.fempa.acd.demosecurityproductos.repository.UsuarioRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/secretarias")
@PreAuthorize("hasRole('ADMIN')")
public class GestionSecretariaController {

    private final UsuarioService usuarioService;
    private final SecurityUtils securityUtils;
    private final AcademiaService academiaService;
    private final UsuarioRepository usuarioRepository;

    public GestionSecretariaController(UsuarioService usuarioService,
                                      SecurityUtils securityUtils,
                                      AcademiaService academiaService,
                                      UsuarioRepository usuarioRepository) {
        this.usuarioService = usuarioService;
        this.securityUtils = securityUtils;
        this.academiaService = academiaService;
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public String listarSecretarias(@RequestParam(required = false, defaultValue = "true") Boolean soloActivas,
                                    Model model) {
        Long academiaId = securityUtils.getAcademiaIdActual();
        List<Usuario> secretarias;

        if (academiaId == null) {
            // Si es ADMIN sin academia, mostrar todas las secretarias
            if (soloActivas) {
                secretarias = usuarioRepository.findByRolAndActivo(Rol.SECRETARIA, true);
            } else {
                secretarias = usuarioRepository.findByRol(Rol.SECRETARIA);
            }
        } else {
            // Si tiene academia, mostrar solo las de su academia
            if (soloActivas) {
                secretarias = usuarioRepository.findByAcademiaIdAndRolAndActivo(academiaId, Rol.SECRETARIA, true);
            } else {
                secretarias = usuarioService.listarPorAcademiaYRol(academiaId, Rol.SECRETARIA);
            }
        }

        model.addAttribute("secretarias", secretarias);
        model.addAttribute("soloActivas", soloActivas);
        return "admin/secretarias-lista";
    }

    @GetMapping("/nueva")
    public String nuevaSecretariaForm(Model model) {
        // Cargar todas las academias activas para que el admin pueda elegir
        List<Academia> academias = academiaService.listarActivas();
        model.addAttribute("academias", academias);
        model.addAttribute("usuario", new Usuario());
        return "admin/secretaria-nueva";
    }

    @PostMapping("/crear")
    public String crearSecretaria(@RequestParam String username,
                                 @RequestParam String password,
                                 @RequestParam String email,
                                 @RequestParam String nombre,
                                 @RequestParam String apellidos,
                                 @RequestParam(required = false) Long academiaId,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        try {
            Academia academiaAsignar = null;

            // Si se proporcionó una academia, obtenerla
            if (academiaId != null) {
                academiaAsignar = academiaService.obtenerPorId(academiaId);
            }

            // Crear usuario con rol SECRETARIA
            Usuario nuevoUsuario = usuarioService.crearUsuario(username, password, email, Rol.SECRETARIA);
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setApellidos(apellidos);
            nuevoUsuario.setAcademia(academiaAsignar);
            nuevoUsuario.setEmailVerificado(true); // Verificar email automáticamente al crear desde admin
            usuarioService.actualizar(nuevoUsuario);

            redirectAttributes.addFlashAttribute("success", "Secretaria creada exitosamente");
            return "redirect:/secretarias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());

            // Recargar academias
            List<Academia> academias = academiaService.listarActivas();
            model.addAttribute("academias", academias);
            model.addAttribute("usuario", new Usuario());
            return "admin/secretaria-nueva";
        }
    }

    @GetMapping("/{id}/editar")
    public String editarSecretariaForm(@PathVariable Long id, Model model) {
        try {
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                return "redirect:/secretarias";
            }

            List<Academia> academias = academiaService.listarActivas();
            model.addAttribute("secretaria", secretaria);
            model.addAttribute("academias", academias);
            return "admin/secretaria-editar";
        } catch (IllegalArgumentException e) {
            return "redirect:/secretarias";
        }
    }

    @PostMapping("/{id}/actualizar")
    public String actualizarSecretaria(@PathVariable Long id,
                                      @RequestParam String nombre,
                                      @RequestParam String apellidos,
                                      @RequestParam String email,
                                      @RequestParam(required = false) Long academiaId,
                                      RedirectAttributes redirectAttributes,
                                      Model model) {
        try {
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/secretarias";
            }

            // Actualizar datos
            secretaria.setNombre(nombre);
            secretaria.setApellidos(apellidos);
            secretaria.setEmail(email);

            // Actualizar academia
            if (academiaId != null) {
                Academia academia = academiaService.obtenerPorId(academiaId);
                secretaria.setAcademia(academia);
            } else {
                secretaria.setAcademia(null);
            }

            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria actualizada exitosamente");
            return "redirect:/secretarias";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);
            List<Academia> academias = academiaService.listarActivas();
            model.addAttribute("secretaria", secretaria);
            model.addAttribute("academias", academias);
            return "admin/secretaria-editar";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarSecretaria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/secretarias";
            }

            // Desactivar en lugar de eliminar para mantener integridad referencial
            secretaria.setActivo(false);
            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria desactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al desactivar la secretaria: " + e.getMessage());
        }
        return "redirect:/secretarias";
    }

    @PostMapping("/{id}/reactivar")
    public String reactivarSecretaria(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Usuario secretaria = usuarioService.obtenerUsuarioPorId(id);

            // Verificar que sea una secretaria
            if (secretaria.getRol() != Rol.SECRETARIA) {
                redirectAttributes.addFlashAttribute("error", "El usuario no es una secretaria");
                return "redirect:/secretarias";
            }

            // Reactivar el usuario
            secretaria.setActivo(true);
            usuarioService.actualizar(secretaria);

            redirectAttributes.addFlashAttribute("success", "Secretaria reactivada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al reactivar la secretaria: " + e.getMessage());
        }
        return "redirect:/secretarias";
    }
}
