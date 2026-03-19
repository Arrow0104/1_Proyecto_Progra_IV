package cr.ac.una.job.controllers;

import cr.ac.una.job.models.*;
import cr.ac.una.job.repositories.*;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/oferente")
public class OferenteController {

    private static final Logger log = LoggerFactory.getLogger(OferenteController.class);

    private final IOferenteRepository oferenteRepository;
    private final ICaracteristicaRepository caracteristicaRepository;
    private final IOferenteCaracteristicaRepository habilidadRepository;

    public OferenteController(IOferenteRepository oferenteRepository,
                              ICaracteristicaRepository caracteristicaRepository,
                              IOferenteCaracteristicaRepository habilidadRepository) {
        this.oferenteRepository = oferenteRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.habilidadRepository = habilidadRepository;
    }

    private Oferente getOferenteFromSession(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return null;
        return oferenteRepository.findFirstByUsuarioIdUsuario(usuario.getIdUsuario()).orElse(null);
    }

    /** Construye mapa raiz -> lista de hijos para el selector del formulario */
    private Map<Caracteristica, List<Caracteristica>> buildArbol() {
        List<Caracteristica> raices = caracteristicaRepository.findByPadreIsNull();
        Map<Caracteristica, List<Caracteristica>> arbol = new LinkedHashMap<>();
        for (Caracteristica raiz : raices) {
            List<Caracteristica> hijos = caracteristicaRepository
                    .findByPadreIdCaracteristica(raiz.getIdCaracteristica());
            arbol.put(raiz, hijos);
        }
        return arbol;
    }

    // ── Habilidades ──────────────────────────────────────────────────────────

    @GetMapping("/habilidades")
    public String habilidades(HttpSession session, Model model) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        List<OferenteCaracteristica> habilidades =
                habilidadRepository.findByOferenteIdOferente(oferente.getIdOferente());

        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);
        model.addAttribute("arbol", buildArbol());
        model.addAttribute("pageTitle", "Mis Habilidades");
        return "oferentes/habilidades";
    }

    @Transactional
    @PostMapping("/habilidades/agregar")
    public String agregarHabilidad(@RequestParam Long idCaracteristica,
                                   @RequestParam int nivel,
                                   HttpSession session) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        Caracteristica car = caracteristicaRepository.findById(idCaracteristica).orElse(null);
        if (car == null) return "redirect:/oferente/habilidades?error=Característica%20no%20encontrada";

        OferenteCaracteristica.OferenteCaracteristicaId pk = new OferenteCaracteristica.OferenteCaracteristicaId(
                oferente.getIdOferente(), car.getIdCaracteristica());

        OferenteCaracteristica habilidad = habilidadRepository.findById(pk)
                .orElse(new OferenteCaracteristica(oferente, car, nivel));
        habilidad.setNivel(nivel);
        habilidadRepository.save(habilidad);

        log.info("Habilidad guardada: oferente={} car={} nivel={}",
                oferente.getIdOferente(), idCaracteristica, nivel);
        return "redirect:/oferente/habilidades?msg=Habilidad%20guardada";
    }

    @Transactional
    @PostMapping("/habilidades/eliminar")
    public String eliminarHabilidad(@RequestParam Long idCaracteristica,
                                    HttpSession session) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        habilidadRepository.deleteByOferenteIdOferenteAndCaracteristicaIdCaracteristica(
                oferente.getIdOferente(), idCaracteristica);

        log.info("Habilidad eliminada: oferente={} car={}", oferente.getIdOferente(), idCaracteristica);
        return "redirect:/oferente/habilidades?msg=Habilidad%20eliminada";
    }

    // ── CV ───────────────────────────────────────────────────────────────────

    @GetMapping("/cv")
    public String verCv(HttpSession session, Model model) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        model.addAttribute("oferente", oferente);
        model.addAttribute("pageTitle", "Mi Currículo");
        return "oferentes/cv";
    }

    @Transactional
    @PostMapping("/cv/guardar")
    public String guardarCv(@RequestParam String cvPath, HttpSession session) {
        Oferente oferente = getOferenteFromSession(session);
        if (oferente == null) return "redirect:/login?error=Sesión%20no%20válida";

        oferente.setCvPath(cvPath == null ? "" : cvPath.trim());
        oferenteRepository.save(oferente);

        log.info("CV actualizado para oferente={}", oferente.getIdOferente());
        return "redirect:/oferente/cv?msg=CV%20actualizado%20correctamente";
    }
}