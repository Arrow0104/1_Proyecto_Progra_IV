package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.puesto.UpdatePuestoRequest;
import cr.ac.una.job.models.*;
import cr.ac.una.job.repositories.IEmpresaRepository;
import cr.ac.una.job.repositories.IOferenteRepository;
import cr.ac.una.job.repositories.IPuestoRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    private final IEmpresaRepository empresaRepository;
    private final IPuestoRepository puestoRepository;
    private final IOferenteRepository oferenteRepository;

    public EmpresaController(IEmpresaRepository empresaRepository,
                             IPuestoRepository puestoRepository,
                             IOferenteRepository oferenteRepository) {
        this.empresaRepository = empresaRepository;
        this.puestoRepository = puestoRepository;
        this.oferenteRepository = oferenteRepository;
    }

    // ── Helper: obtener Empresa de la sesión ─────────────────────────────────

    private Empresa getEmpresaFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;
        return empresaRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
    }

    // ── Puestos: formulario nuevo ────────────────────────────────────────────

    @GetMapping("/puestos/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        model.addAttribute("puestoForm", null);
        model.addAttribute("puestoId", null);
        return "empresas/publicar-puesto";
    }

    @Transactional
    @PostMapping("/puestos/nuevo")
    public String crearPuesto(@RequestParam String titulo,
                              @RequestParam String descripcion,
                              @RequestParam BigDecimal salario,
                              @RequestParam(defaultValue = "ACTIVO") String estado,
                              HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = new Puesto(
                null,
                titulo.trim(),
                descripcion.trim(),
                salario,
                EstadoPuesto.valueOf(estado),
                true,
                LocalDateTime.now(),
                empresa
        );
        puestoRepository.save(puesto);
        log.info("Puesto creado: '{}' para empresa id={}", titulo, empresa.getIdEmpresa());

        return "redirect:/empresas?msg=Puesto%20publicado%20correctamente";
    }

    // ── Puestos: editar ──────────────────────────────────────────────────────

    @GetMapping("/puestos/{id}/editar")
    public String formEditar(@PathVariable Long id, HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            return "redirect:/empresas?error=Puesto%20no%20encontrado";
        }

        UpdatePuestoRequest form = new UpdatePuestoRequest(
                puesto.getTitulo(), puesto.getDescripcion(),
                puesto.getSalario(), puesto.getEstado(),
                empresa.getIdEmpresa());

        model.addAttribute("puestoForm", form);
        model.addAttribute("puestoId", id);
        return "empresas/publicar-puesto";
    }

    @Transactional
    @PostMapping("/puestos/{id}/editar")
    public String editarPuesto(@PathVariable Long id,
                               @RequestParam String titulo,
                               @RequestParam String descripcion,
                               @RequestParam BigDecimal salario,
                               @RequestParam(defaultValue = "ACTIVO") String estado,
                               HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            return "redirect:/empresas?error=Puesto%20no%20encontrado";
        }

        puesto.setTitulo(titulo.trim());
        puesto.setDescripcion(descripcion.trim());
        puesto.setSalario(salario);
        puesto.setEstado(EstadoPuesto.valueOf(estado));
        puestoRepository.save(puesto);
        log.info("Puesto editado id={}", id);

        return "redirect:/empresas?msg=Puesto%20actualizado%20correctamente";
    }

    // ── Puestos: cerrar / activar ────────────────────────────────────────────

    @Transactional
    @PostMapping("/puestos/{id}/cerrar")
    public String cerrarPuesto(@PathVariable Long id, HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        puestoRepository.findById(id).ifPresent(p -> {
            if (p.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
                p.setEstado(EstadoPuesto.CERRADO);
                puestoRepository.save(p);
            }
        });
        return "redirect:/empresas?msg=Puesto%20cerrado";
    }

    @Transactional
    @PostMapping("/puestos/{id}/activar")
    public String activarPuesto(@PathVariable Long id, HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        puestoRepository.findById(id).ifPresent(p -> {
            if (p.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
                p.setEstado(EstadoPuesto.ACTIVO);
                puestoRepository.save(p);
            }
        });
        return "redirect:/empresas?msg=Puesto%20reactivado";
    }

    // ── Candidatos ───────────────────────────────────────────────────────────

    @GetMapping("/candidatos")
    public String verCandidatos(@RequestParam Long puestoId,
                                HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.findById(puestoId).orElse(null);
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa())) {
            return "redirect:/empresas?error=Puesto%20no%20encontrado";
        }

        // Por ahora muestra todos los oferentes activos como candidatos potenciales
        List<Oferente> candidatos = oferenteRepository.findByActiveTrue();

        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos", candidatos);
        model.addAttribute("pageTitle", "Candidatos");
        return "empresas/detalle-candidato";
    }

    @GetMapping("/candidatos/{id}")
    public String detalleCandidato(@PathVariable Long id,
                                   HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Oferente oferente = oferenteRepository.findById(id).orElse(null);
        if (oferente == null) return "redirect:/empresas?error=Candidato%20no%20encontrado";

        model.addAttribute("oferente", oferente);
        model.addAttribute("pageTitle", "Detalle Candidato");
        return "empresas/detalle-candidato-individual";
    }
}
