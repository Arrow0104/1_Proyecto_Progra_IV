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
        model.addAttribute("pageTitle", "Usuarios");
        return "usuarios/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("usuario", service.getUsuarioById(id));
        model.addAttribute("pageTitle", "Detalle Usuario");
        return "usuarios/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        CreateUsuarioRequest form = new CreateUsuarioRequest();
        form.setRol(Rol.OFERENTE);
        form.setEstado(EstadoUsuario.ACTIVO);

        model.addAttribute("usuarioForm", form);
        model.addAttribute("roles", Rol.values());
        model.addAttribute("estados", EstadoUsuario.values());

        model.addAttribute("pageTitle", "Crear Usuario");
        model.addAttribute("formTitle", "Crear Usuario");
        model.addAttribute("formAction", "/usuarios");
        model.addAttribute("isEdit", false);

        return "usuarios/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("usuarioForm") CreateUsuarioRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", Rol.values());
            model.addAttribute("estados", EstadoUsuario.values());

            model.addAttribute("pageTitle", "Crear Usuario");
            model.addAttribute("formTitle", "Crear Usuario");
            model.addAttribute("formAction", "/usuarios");
            model.addAttribute("isEdit", false);

            return "usuarios/form";
        }

        service.createUsuario(request);
        return "redirect:/usuarios";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        UpdateUsuarioRequest request = service.buildUpdateRequest(id);

        model.addAttribute("usuarioForm", request);
        model.addAttribute("usuarioId", id);

        model.addAttribute("roles", Rol.values());
        model.addAttribute("estados", EstadoUsuario.values());

        model.addAttribute("pageTitle", "Editar Usuario");
        model.addAttribute("formTitle", "Editar Usuario");
        model.addAttribute("formAction", "/usuarios/" + id);
        model.addAttribute("isEdit", true);

        return "usuarios/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("usuarioForm") UpdateUsuarioRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("usuarioId", id);

            model.addAttribute("roles", Rol.values());
            model.addAttribute("estados", EstadoUsuario.values());

            model.addAttribute("pageTitle", "Editar Usuario");
            model.addAttribute("formTitle", "Editar Usuario");
            model.addAttribute("formAction", "/usuarios/" + id);
            model.addAttribute("isEdit", true);

            return "usuarios/form";
        }

        service.updateUsuario(id, request);
        return "redirect:/usuarios/" + id;
    }
}
