package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.empresa.CreateEmpresaRequest;
import cr.ac.una.job.dtos.empresa.UpdateEmpresaRequest;
import cr.ac.una.job.services.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import cr.ac.una.job.models.Empresa;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IEmpresaRepository;
import cr.ac.una.job.services.PuestoService;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
@RequestMapping("/empresas")
public class EmpresaViewController {

    private final EmpresaService service;
    private final PuestoService puestoService;
    private final IEmpresaRepository empresaRepository;

    public EmpresaViewController(EmpresaService service, PuestoService puestoService, IEmpresaRepository empresaRepository) {
        this.service = service;
        this.puestoService = puestoService;
        this.empresaRepository = empresaRepository;
    }

    @GetMapping
    public String list(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login?error=Debe%20iniciar%20sesión";
        }

        Optional<Empresa> empresaOpt = empresaRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario());
        if (empresaOpt.isEmpty()) {
            return "redirect:/inicio?error=No%20tiene%20empresa%20asociada";
        }

        Empresa empresa = empresaOpt.get();

        model.addAttribute("puestos", puestoService.getPuestosByEmpresa(empresa.getIdEmpresa()));
        model.addAttribute("pageTitle", "Mis Puestos");
        model.addAttribute("empresaNombre", empresa.getNombre());

        return "empresas/puestos";
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
