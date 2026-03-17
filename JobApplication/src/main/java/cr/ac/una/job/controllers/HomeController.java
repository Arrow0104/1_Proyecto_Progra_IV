package cr.ac.una.job.controllers;

import cr.ac.una.job.services.PuestoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PuestoService puestoService;

    public HomeController(PuestoService puestoService) {
        this.puestoService = puestoService;
    }

    @GetMapping({"/", "/inicio"})
    public String inicio(Model model) {
        var puestos = puestoService.getAllPuestos();

        if (puestos != null && puestos.size() > 5) {
            puestos = puestos.subList(0, 5);
        }

        model.addAttribute("puestos", puestos);
        return "public/inicio";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/register-empresa")
    public String registerEmpresa() {
        return "public/registro-empresa";
    }

    @GetMapping("/register-oferente")
    public String registerOferente() {
        return "public/registro-oferente";
    }
}
