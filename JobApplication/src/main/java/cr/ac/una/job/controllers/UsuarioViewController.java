package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.usuario.CreateUsuarioRequest;
import cr.ac.una.job.dtos.usuario.UpdateUsuarioRequest;
import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
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

    public UsuarioViewController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("usuarios", service.getAllUsuarios());
        return "admin/dashboard";  // ← Panel admin
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("usuario", service.getUsuarioById(id));
        return "admin/empresas-pendientes";  // ← Usuarios pendientes
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("usuarioForm", new CreateUsuarioRequest());
        return "public/login";  // ← Registro/Login
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateUsuarioRequest request) {
        service.createUsuario(request);
        return "redirect:/usuarios";
    }
}
