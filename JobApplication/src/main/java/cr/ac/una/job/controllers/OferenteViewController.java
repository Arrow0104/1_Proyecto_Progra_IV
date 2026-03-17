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
        model.addAttribute("pageTitle", "Oferentes");
        return "oferentes/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("oferente", service.getOferenteById(id));
        model.addAttribute("pageTitle", "Detalle Oferente");
        return "oferentes/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("oferenteForm", new CreateOferenteRequest());
        model.addAttribute("pageTitle", "Crear Oferente");
        model.addAttribute("formTitle", "Crear Oferente");
        model.addAttribute("formAction", "/oferentes");
        model.addAttribute("isEdit", false);
        return "oferentes/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("oferenteForm") CreateOferenteRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Crear Oferente");
            model.addAttribute("formTitle", "Crear Oferente");
            model.addAttribute("formAction", "/oferentes");
            model.addAttribute("isEdit", false);
            return "oferentes/form";
        }

        service.createOferente(request);
        return "redirect:/oferentes";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        UpdateOferenteRequest request = service.buildUpdateRequest(id);

        model.addAttribute("oferenteForm", request);
        model.addAttribute("oferenteId", id);
        model.addAttribute("pageTitle", "Editar Oferente");
        model.addAttribute("formTitle", "Editar Oferente");
        model.addAttribute("formAction", "/oferentes/" + id);
        model.addAttribute("isEdit", true);

        return "oferentes/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("oferenteForm") UpdateOferenteRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("oferenteId", id);
            model.addAttribute("pageTitle", "Editar Oferente");
            model.addAttribute("formTitle", "Editar Oferente");
            model.addAttribute("formAction", "/oferentes/" + id);
            model.addAttribute("isEdit", true);
            return "oferentes/form";
        }

        service.updateOferente(id, request);
        return "redirect:/oferentes/" + id;
    }
}
