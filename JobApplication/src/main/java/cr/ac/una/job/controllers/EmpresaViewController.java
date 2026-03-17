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
        model.addAttribute("pageTitle", "Empresas");
        return "empresas/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        model.addAttribute("empresa", service.getEmpresaById(id));
        model.addAttribute("pageTitle", "Detalle Empresa");
        return "empresas/detail";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("empresaForm", new CreateEmpresaRequest());
        model.addAttribute("pageTitle", "Crear Empresa");
        model.addAttribute("formTitle", "Crear Empresa");
        model.addAttribute("formAction", "/empresas");
        model.addAttribute("isEdit", false);
        return "empresas/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("empresaForm") CreateEmpresaRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Crear Empresa");
            model.addAttribute("formTitle", "Crear Empresa");
            model.addAttribute("formAction", "/empresas");
            model.addAttribute("isEdit", false);
            return "empresas/form";
        }

        service.createEmpresa(request);
        return "redirect:/empresas";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        UpdateEmpresaRequest request = service.buildUpdateRequest(id);

        model.addAttribute("empresaForm", request);
        model.addAttribute("empresaId", id);
        model.addAttribute("pageTitle", "Editar Empresa");
        model.addAttribute("formTitle", "Editar Empresa");
        model.addAttribute("formAction", "/empresas/" + id);
        model.addAttribute("isEdit", true);

        return "empresas/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Integer id,
                         @Valid @ModelAttribute("empresaForm") UpdateEmpresaRequest request,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("empresaId", id);
            model.addAttribute("pageTitle", "Editar Empresa");
            model.addAttribute("formTitle", "Editar Empresa");
            model.addAttribute("formAction", "/empresas/" + id);
            model.addAttribute("isEdit", true);
            return "empresas/form";
        }

        service.updateEmpresa(id, request);
        return "redirect:/empresas/" + id;
    }
}
