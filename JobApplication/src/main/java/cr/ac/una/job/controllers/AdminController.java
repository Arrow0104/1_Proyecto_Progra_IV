package cr.ac.una.job.controllers;

import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IUsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final IUsuarioRepository usuarioRepository;

    public AdminController(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // ── Dashboard ────────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long pendientesEmpresa = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.EMPRESA && u.getEstado() == EstadoUsuario.PENDIENTE)
                .count();

        long pendientesOferente = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.OFERENTE && u.getEstado() == EstadoUsuario.PENDIENTE)
                .count();

        model.addAttribute("pendientesEmpresa", pendientesEmpresa);
        model.addAttribute("pendientesOferente", pendientesOferente);
        return "admin/dashboard";
    }

    // ── Empresas Pendientes ──────────────────────────────────────────────────

    @GetMapping("/empresas/pendientes")
    public String empresasPendientes(Model model) {
        List<Usuario> pendientes = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.EMPRESA && u.getEstado() == EstadoUsuario.PENDIENTE)
                .toList();

        model.addAttribute("usuarios", pendientes);
        return "admin/empresas-pendientes";
    }

    @PostMapping("/empresas/aprobar/{id}")
    public String aprobarEmpresa(@PathVariable Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setEstado(EstadoUsuario.ACTIVO);
            usuarioRepository.save(u);
        });
        return "redirect:/admin/empresas/pendientes?msg=Empresa%20aprobada";
    }

    // ── Oferentes Pendientes ─────────────────────────────────────────────────

    @GetMapping("/oferentes/pendientes")
    public String oferentesPendientes(Model model) {
        List<Usuario> pendientes = usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Rol.OFERENTE && u.getEstado() == EstadoUsuario.PENDIENTE)
                .toList();

        model.addAttribute("usuarios", pendientes);
        return "admin/oferentes-pendientes";
    }

    @PostMapping("/oferentes/aprobar/{id}")
    public String aprobarOferente(@PathVariable Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setEstado(EstadoUsuario.ACTIVO);
            usuarioRepository.save(u);
        });
        return "redirect:/admin/oferentes/pendientes?msg=Oferente%20aprobado";
    }
}
