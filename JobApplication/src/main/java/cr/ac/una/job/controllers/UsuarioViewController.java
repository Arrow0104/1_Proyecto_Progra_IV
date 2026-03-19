package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.usuario.CreateUsuarioRequest;
import cr.ac.una.job.dtos.usuario.UpdateUsuarioRequest;
import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IUsuarioRepository;
import cr.ac.una.job.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService service;
    private final IUsuarioRepository usuarioRepository;

    public UsuarioViewController(UsuarioService service, IUsuarioRepository usuarioRepository) {
        this.service = service;
        this.usuarioRepository = usuarioRepository;
    }

    // GET /usuarios → dashboard del admin con conteos reales
    @GetMapping
    public String list(Model model) {
        long pendientesEmpresa = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.EMPRESA && u.getEstado() == EstadoUsuario.PENDIENTE)
                .count();

        long pendientesOferente = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.OFERENTE && u.getEstado() == EstadoUsuario.PENDIENTE)
                .count();

        model.addAttribute("pendientesEmpresa", pendientesEmpresa);
        model.addAttribute("pendientesOferente", pendientesOferente);
        model.addAttribute("usuarios", service.getAllUsuarios());
        return "admin/dashboard";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.getUsuarioById(id));
        return "admin/empresas-pendientes";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("usuarioForm", new CreateUsuarioRequest());
        return "public/login";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateUsuarioRequest request) {
        service.createUsuario(request);
        return "redirect:/usuarios";
    }
}
