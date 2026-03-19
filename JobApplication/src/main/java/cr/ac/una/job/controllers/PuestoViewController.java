package cr.ac.una.job.controllers;

import cr.ac.una.job.models.Caracteristica;
import cr.ac.una.job.models.Puesto;
import cr.ac.una.job.repositories.ICaracteristicaRepository;
import cr.ac.una.job.repositories.IPuestoCaracteristicaRepository;
import cr.ac.una.job.repositories.IPuestoRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/puestos")
public class PuestoViewController {

    private final IPuestoRepository puestoRepository;
    private final ICaracteristicaRepository caracteristicaRepository;
    private final IPuestoCaracteristicaRepository puestoCaracteristicaRepository;

    public PuestoViewController(IPuestoRepository puestoRepository,
                                ICaracteristicaRepository caracteristicaRepository,
                                IPuestoCaracteristicaRepository puestoCaracteristicaRepository) {
        this.puestoRepository = puestoRepository;
        this.caracteristicaRepository = caracteristicaRepository;
        this.puestoCaracteristicaRepository = puestoCaracteristicaRepository;
    }

    /** Construye mapa raiz -> hijos para el panel de filtros */
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

    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) List<Long> caracteristicas,
                         Model model) {

        Map<Caracteristica, List<Caracteristica>> arbol = buildArbol();
        model.addAttribute("arbol", arbol);
        model.addAttribute("seleccionadas", caracteristicas != null ? caracteristicas : List.of());

        if (caracteristicas == null || caracteristicas.isEmpty()) {
            // Sin filtro: mostrar todos los puestos activos
            List<Puesto> todos = puestoRepository.findByActiveTrue();
            model.addAttribute("resultados", todos);
            model.addAttribute("filtroActivo", false);
        } else {
            // Con filtro: puestos que tengan AL MENOS UNA de las características
            List<Long> puestoIds = puestoCaracteristicaRepository
                    .findPuestoIdsByCaracteristicasIn(caracteristicas);

            List<Puesto> resultados = puestoIds.isEmpty()
                    ? List.of()
                    : puestoRepository.findAllById(puestoIds).stream()
                    .filter(Puesto::isActive)
                    .toList();

            model.addAttribute("resultados", resultados);
            model.addAttribute("filtroActivo", true);
        }

        return "public/buscar-puestos";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Puesto puesto = puestoRepository.findById(id).orElse(null);
        if (puesto == null) return "redirect:/puestos/buscar?error=Puesto%20no%20encontrado";

        var caracteristicas = puestoCaracteristicaRepository.findByPuestoIdPuesto(id);

        model.addAttribute("puesto", puesto);
        model.addAttribute("caracteristicas", caracteristicas);
        return "public/detalle-puesto";
    }
}
