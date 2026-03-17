package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.oferente.CreateOferenteRequest;
import cr.ac.una.job.dtos.oferente.UpdateOferenteRequest;
import cr.ac.una.job.services.OferenteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oferentes")
public class OferenteViewController {

    private final OferenteService service;

    public OferenteViewController(OferenteService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("oferentes", service.getAllOferentes());
        return "oferentes/dashboard";  // ← Dashboard del oferente
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
