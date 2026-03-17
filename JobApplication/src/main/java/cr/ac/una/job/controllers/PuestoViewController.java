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

    @GetMapping("/buscar")
    public String list(Model model) {
        model.addAttribute("puestos", service.getAllPuestos());
        return "public/buscar-puestos";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("puesto", service.getPuestoById(id));
        return "public/detalle-puesto";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("puestoForm", new CreatePuestoRequest());
        return "empresas/publicar-puesto";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreatePuestoRequest request) {
        service.createPuesto(request);
        return "redirect:/puestos/buscar";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("puestoForm", service.buildUpdateRequest(id));
        return "empresas/publicar-puesto";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, UpdatePuestoRequest request) {
        service.updatePuesto(id, request);
        return "redirect:/puestos/{id}";
    }
}
