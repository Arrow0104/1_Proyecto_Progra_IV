package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.oferente.CreateOferenteRequest;
import cr.ac.una.job.dtos.oferente.UpdateOferenteRequest;
import cr.ac.una.job.services.OferenteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import cr.ac.una.job.models.Oferente;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IOferenteRepository;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
@RequestMapping("/oferentes")
public class OferenteViewController {

    private final OferenteService service;
    private final IOferenteRepository oferenteRepository;

    public OferenteViewController(OferenteService service, IOferenteRepository oferenteRepository) {
        this.service = service;
        this.oferenteRepository = oferenteRepository;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login?error=Debe%20iniciar%20sesión";
        }

        Optional<Oferente> oferenteOpt = oferenteRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario());
        if (oferenteOpt.isEmpty()) {
            return "redirect:/inicio?error=No%20tiene%20oferente%20asociado";
        }

        model.addAttribute("oferente", oferenteOpt.get());
        model.addAttribute("pageTitle", "Dashboard Oferente");
        return "oferentes/dashboard";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("oferente", service.getOferenteById(id));
        return "oferentes/habilidades";  // ← Gestionar habilidades
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("oferenteForm", new CreateOferenteRequest());
        return "oferentes/cv";  // ← Subir CV
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateOferenteRequest request) {
        service.createOferente(request);
        return "redirect:/oferentes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("oferenteForm", service.buildUpdateRequest(id));
        return "oferentes/cv";  // ← Editar CV
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, UpdateOferenteRequest request) {
        service.updateOferente(id, request);
        return "redirect:/oferentes";
    }
}
