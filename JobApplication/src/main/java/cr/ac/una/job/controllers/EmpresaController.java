package cr.ac.una.job.controllers;

import cr.ac.una.job.dtos.puesto.UpdatePuestoRequest;
import cr.ac.una.job.models.*;
import cr.ac.una.job.repositories.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    private static final Logger log = LoggerFactory.getLogger(EmpresaController.class);

    private final IEmpresaRepository empresaRepository;
    private final IPuestoRepository puestoRepository;
    private final IOferenteRepository oferenteRepository;
    private final ICaracteristicaRepository caracteristicaRepository;
    private final IPuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final IOferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    public EmpresaController(IEmpresaRepository empresaRepository,
                             IPuestoRepository puestoRepository,
                             IOferenteRepository oferenteRepository,
                             ICaracteristicaRepository caracteristicaRepository,
                             IPuestoCaracteristicaRepository puestoCaracteristicaRepository,
                             IOferenteCaracteristicaRepository oferenteCaracteristicaRepository) {
        this.empresaRepository = empresaRepository;
        this.puestoRepository = puestoRepository;
        this.oferenteRepository = oferenteRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.puestoCaracteristicaRepository = puestoCaracteristicaRepository;
        this.oferenteCaracteristicaRepository = oferenteCaracteristicaRepository;
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Empresa getEmpresaFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;
        return empresaRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
    }

    private Map<Caracteristica, List<Caracteristica>> buildArbol() {
        List<Caracteristica> raices = caracteristicaRepository.findByPadreIsNull();
        Map<Caracteristica, List<Caracteristica>> arbol = new LinkedHashMap<>();
        for (Caracteristica raiz : raices) {
            arbol.put(raiz, caracteristicaRepository.findByPadreIdCaracteristica(raiz.getIdCaracteristica()));
        }
        return arbol;
    }

    // ── Puestos: formulario nuevo ────────────────────────────────────────────

    @GetMapping("/puestos/nuevo")
    public String formNuevo(HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        model.addAttribute("puestoForm", null);
        model.addAttribute("puestoId", null);
        model.addAttribute("arbol", buildArbol());
        model.addAttribute("caracteristicasSeleccionadas", List.of());
        return "empresas/publicar-puesto";
    }

    @Transactional
    @PostMapping("/puestos/nuevo")
    public String crearPuesto(@RequestParam String titulo,
                              @RequestParam String descripcion,
                              @RequestParam BigDecimal salario,
                              @RequestParam(defaultValue = "ACTIVO") String estado,
                              @RequestParam(required = false) List<Long> idsCaracteristicas,
                              @RequestParam Map<String, String> allParams,
                              HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.save(new Puesto(
                null, titulo.trim(), descripcion.trim(), salario,
                EstadoPuesto.valueOf(estado), true, LocalDateTime.now(), empresa));

        guardarCaracteristicasPuesto(puesto, idsCaracteristicas, allParams);
        log.info("Puesto creado: '{}' empresa={}", titulo, empresa.getIdEmpresa());

        return "redirect:/empresas?msg=Puesto%20publicado%20correctamente";
    }

    // ── Puestos: editar ──────────────────────────────────────────────────────

    @GetMapping("/puestos/{id}/editar")
    public String formEditar(@PathVariable Long id, HttpSession session, Model model) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa()))
            return "redirect:/empresas?error=Puesto%20no%20encontrado";

        // IDs de características ya asignadas (para marcar checkboxes)
        List<Long> seleccionadas = puestoCaracteristicaRepository
                .findByPuestoIdPuesto(id).stream()
                .map(pc -> pc.getCaracteristica().getIdCaracteristica())
                .toList();

        UpdatePuestoRequest form = new UpdatePuestoRequest(
                puesto.getTitulo(), puesto.getDescripcion(),
                puesto.getSalario(), puesto.getEstado(), empresa.getIdEmpresa());

        model.addAttribute("puestoForm", form);
        model.addAttribute("puestoId", id);
        model.addAttribute("arbol", buildArbol());
        model.addAttribute("caracteristicasSeleccionadas", seleccionadas);
        return "empresas/publicar-puesto";
    }

    @Transactional
    @PostMapping("/puestos/{id}/editar")
    public String editarPuesto(@PathVariable Long id,
                               @RequestParam String titulo,
                               @RequestParam String descripcion,
                               @RequestParam BigDecimal salario,
                               @RequestParam(defaultValue = "ACTIVO") String estado,
                               @RequestParam(required = false) List<Long> idsCaracteristicas,
                               @RequestParam Map<String, String> allParams,
                               HttpSession session) {
        Empresa empresa = getEmpresaFromSession(session);
        if (empresa == null) return "redirect:/login?error=Sesión%20no%20válida";

        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa()))
            return "redirect:/empresas?error=Puesto%20no%20encontrado";

        puesto.setTitulo(titulo.trim());
        puesto.setDescripcion(descripcion.trim());
        puesto.setSalario(salario);
        puesto.setEstado(EstadoPuesto.valueOf(estado));
        puestoRepository.save(puesto);

        // Reemplazar características: borrar las anteriores y guardar las nuevas
        puestoCaracteristicaRepository.deleteByPuestoIdPuesto(id);
        guardarCaracteristicasPuesto(puesto, idsCaracteristicas, allParams);
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
        if (puesto == null || !puesto.getEmpresa().getIdEmpresa().equals(empresa.getIdEmpresa()))
            return "redirect:/empresas?error=Puesto%20no%20encontrado";

        // Mapa Oferente -> sus habilidades
        List<Oferente> todos = oferenteRepository.findByActiveTrue();
        Map<Oferente, List<OferenteCaracteristica>> candidatosConHabilidades = new LinkedHashMap<>();
        for (Oferente o : todos) {
            List<OferenteCaracteristica> habilidades =
                    oferenteCaracteristicaRepository.findByOferenteIdOferente(o.getIdOferente());
            candidatosConHabilidades.put(o, habilidades);
        }

        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatosConHabilidades", candidatosConHabilidades);
        model.addAttribute("pageTitle", "Candidatos");
        return "empresas/detalle-candidato";
    }

    // ── Helper: guardar características de un puesto ─────────────────────────

    private void guardarCaracteristicasPuesto(Puesto puesto,
                                              List<Long> idsCaracteristicas,
                                              Map<String, String> allParams) {
        if (idsCaracteristicas == null || idsCaracteristicas.isEmpty()) return;

        for (Long idCar : idsCaracteristicas) {
            String nivelKey = "nivel_" + idCar;
            int nivel = 1;
            if (allParams.containsKey(nivelKey)) {
                try { nivel = Integer.parseInt(allParams.get(nivelKey)); } catch (NumberFormatException ignored) {}
            }
            int nivelFinal = nivel;
            caracteristicaRepository.findById(idCar).ifPresent(car ->
                    puestoCaracteristicaRepository.save(
                            new PuestoCaracteristica(puesto, car, nivelFinal)));
        }
    }
}
