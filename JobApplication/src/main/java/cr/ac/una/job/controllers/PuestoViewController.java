package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.puesto.CreatePuestoRequest;
import cr.ac.una.job.dtos.puesto.UpdatePuestoRequest;
import cr.ac.una.job.models.EstadoPuesto;
import cr.ac.una.job.services.PuestoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/puestos")
public class PuestoViewController {

    private final PuestoService service;

    public PuestoViewController(PuestoService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("puestos", service.getAllPuestos());
        model.addAttribute("pageTitle", "Puestos");
        return "puestos/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("puesto", service.getPuestoById(id));
        model.addAttribute("pageTitle", "Detalle Puesto");
        return "puestos/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        // Estado por defecto para el form si quieres
        CreatePuestoRequest form = new CreatePuestoRequest();
        form.setEstado(EstadoPuesto.ACTIVO);

        model.addAttribute("puestoForm", form);
        model.addAttribute("pageTitle", "Crear Puesto");
        model.addAttribute("formTitle", "Crear Puesto");
        model.addAttribute("formAction", "/puestos");
        model.addAttribute("isEdit", false);

        return "puestos/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("puestoForm") CreatePuestoRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Crear Puesto");
            model.addAttribute("formTitle", "Crear Puesto");
            model.addAttribute("formAction", "/puestos");
            model.addAttribute("isEdit", false);
            return "puestos/form";
        }

        service.createPuesto(request);
        return "redirect:/puestos";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        UpdatePuestoRequest request = service.buildUpdateRequest(id);

        model.addAttribute("puestoForm", request);
        model.addAttribute("puestoId", id);
        model.addAttribute("pageTitle", "Editar Puesto");
        model.addAttribute("formTitle", "Editar Puesto");
        model.addAttribute("formAction", "/puestos/" + id);
        model.addAttribute("isEdit", true);

        return "puestos/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("puestoForm") UpdatePuestoRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("puestoId", id);
            model.addAttribute("pageTitle", "Editar Puesto");
            model.addAttribute("formTitle", "Editar Puesto");
            model.addAttribute("formAction", "/puestos/" + id);
            model.addAttribute("isEdit", true);
            return "puestos/form";
        }

        service.updatePuesto(id, request);
        return "redirect:/puestos/" + id;
    }
}
