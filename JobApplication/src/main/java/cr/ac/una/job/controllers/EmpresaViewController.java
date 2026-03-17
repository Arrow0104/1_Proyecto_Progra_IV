package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.empresa.CreateEmpresaRequest;
import cr.ac.una.job.dtos.empresa.UpdateEmpresaRequest;
import cr.ac.una.job.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/empresas")
public class EmpresaViewController {

    private final EmpresaService service;

    public EmpresaViewController(EmpresaService service) {
        this.service = service;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("empresas", service.getAllEmpresas());
        model.addAttribute("pageTitle", "Mis Puestos");
        return "empresas/puestos";  // ← "puestos.html" (tabla con mis puestos)
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("empresa", service.getEmpresaById(id));
        model.addAttribute("pageTitle", "Detalle Puesto");
        return "empresas/detalle-candidato";  // ← Para ver candidatos
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("empresaForm", new CreateEmpresaRequest());
        model.addAttribute("pageTitle", "Publicar Puesto");
        return "empresas/publicar-puesto";  // ← Formulario crear puesto
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateEmpresaRequest request) {
        service.createEmpresa(request);
        return "redirect:/empresas";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("empresaForm", service.buildUpdateRequest(id));
        return "empresas/publicar-puesto";  // ← Editar puesto
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @Valid @ModelAttribute UpdateEmpresaRequest request) {
        service.updateEmpresa(id, request);
        return "redirect:/empresas";
    }
}
