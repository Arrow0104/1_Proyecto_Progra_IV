package cr.ac.una.job.controllers;

import cr.ac.una.job.models.Caracteristica;
import cr.ac.una.job.repositories.ICaracteristicaRepository;
import cr.ac.una.job.repositories.IPuestoCaracteristicaRepository;
import cr.ac.una.job.repositories.IOferenteCaracteristicaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/caracteristicas")
public class AdminCaracteristicaController {

    private final ICaracteristicaRepository caracteristicaRepository;
    private final IPuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final IOferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    public AdminCaracteristicaController(
            ICaracteristicaRepository caracteristicaRepository,
            IPuestoCaracteristicaRepository puestoCaracteristicaRepository,
            IOferenteCaracteristicaRepository oferenteCaracteristicaRepository) {
        this.caracteristicaRepository = caracteristicaRepository;
        this.puestoCaracteristicaRepository = puestoCaracteristicaRepository;
        this.oferenteCaracteristicaRepository = oferenteCaracteristicaRepository;
    }


    @GetMapping
    public String lista(@RequestParam(required = false) Long padreId, Model model) {
        List<Caracteristica> raices = caracteristicaRepository.findByPadreIsNull();
        List<Caracteristica> todas  = caracteristicaRepository.findAll();

        model.addAttribute("raices", raices);
        model.addAttribute("todasCaracteristicas", todas);

        if (padreId != null) {
            Caracteristica padre = caracteristicaRepository.findById(padreId).orElse(null);
            List<Caracteristica> hijos = caracteristicaRepository
                    .findByPadreIdCaracteristica(padreId);
            model.addAttribute("padreActual", padre);
            model.addAttribute("hijos", hijos);
        } else {
            model.addAttribute("padreActual", null);
            model.addAttribute("hijos", List.of());
        }

        return "admin/caracteristicas";
    }


    @Transactional
    @PostMapping("/crear")
    public String crear(@RequestParam String nombre,
                        @RequestParam(required = false) Long idPadre) {

        Caracteristica padre = null;
        if (idPadre != null) {
            padre = caracteristicaRepository.findById(idPadre).orElse(null);
        }

        Caracteristica nueva = new Caracteristica(null, nombre.trim(), padre);
        caracteristicaRepository.save(nueva);

        // Volver al nivel donde estábamos
        if (idPadre != null) {
            return "redirect:/admin/caracteristicas?padreId=" + idPadre;
        }
        return "redirect:/admin/caracteristicas";
    }


    @Transactional
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        Caracteristica car = caracteristicaRepository.findById(id).orElse(null);
        if (car == null) return "redirect:/admin/caracteristicas?error=No+encontrada";

        Long padreId = (car.getPadre() != null) ? car.getPadre().getIdCaracteristica() : null;

        // Verificar que no tenga hijos
        List<Caracteristica> hijos = caracteristicaRepository
                .findByPadreIdCaracteristica(id);
        if (!hijos.isEmpty()) {
            String redirect = (padreId != null)
                    ? "redirect:/admin/caracteristicas?padreId=" + padreId + "&error=Tiene+subcategorías"
                    : "redirect:/admin/caracteristicas?error=Tiene+subcategorías";
            return redirect;
        }


        puestoCaracteristicaRepository.findAll().stream()
                .filter(pc -> pc.getCaracteristica().getIdCaracteristica().equals(id))
                .forEach(puestoCaracteristicaRepository::delete);

        oferenteCaracteristicaRepository.findAll().stream()
                .filter(oc -> oc.getCaracteristica().getIdCaracteristica().equals(id))
                .forEach(oferenteCaracteristicaRepository::delete);

        caracteristicaRepository.deleteById(id);

        if (padreId != null) {
            return "redirect:/admin/caracteristicas?padreId=" + padreId + "&msg=Eliminada+correctamente";
        }
        return "redirect:/admin/caracteristicas?msg=Eliminada+correctamente";
    }
}