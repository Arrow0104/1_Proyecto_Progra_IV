package cr.ac.una.job.services;

import cr.ac.una.job.dtos.oferente.CreateOferenteRequest;
import cr.ac.una.job.dtos.oferente.OferenteResponse;
import cr.ac.una.job.dtos.oferente.UpdateOferenteRequest;
import cr.ac.una.job.exceptions.OferenteNotFoundException;
import cr.ac.una.job.models.Oferente;
import cr.ac.una.job.repositories.IOferenteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OferenteService {
    private static final Logger log = LoggerFactory.getLogger(OferenteService.class);

    private final IOferenteRepository repository;

    public OferenteService(IOferenteRepository repository) {
        this.repository = repository;
    }

    public List<OferenteResponse> getAllOferentes() {
        log.info("Fetching all oferentes");
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public OferenteResponse getOferenteById(Integer id) {
        log.info("Fetching oferente with id {}", id);
        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        return toResponse(oferente);
    }

    public Oferente getDomainOferenteById(Integer id) {
        log.info("Fetching domain oferente with id {}", id);
        return repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
    }

    public OferenteResponse createOferente(CreateOferenteRequest request) {
        log.info("Creating oferente");
        Oferente oferente = new Oferente(
                0,
                request.getNombre().trim(),
                request.getResidencia().trim(),
                (request.getCvPath() == null ? null : request.getCvPath().trim()),
                null
        );

        Oferente saved = repository.save(oferente);
        return toResponse(saved);
    }

    public OferenteResponse updateOferente(Integer id, UpdateOferenteRequest request) {
        log.info("Updating oferente with id {}", id);

        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));

        oferente.setNombre(request.getNombre().trim());
        oferente.setResidencia(request.getResidencia().trim());
        oferente.setCvPath(request.getCvPath() == null ? null : request.getCvPath().trim());

        Oferente updated = repository.update(oferente);
        return toResponse(updated);
    }

    public UpdateOferenteRequest buildUpdateRequest(Integer id) {
        log.info("Building update request for oferente id {}", id);
        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        return new UpdateOferenteRequest(oferente.getNombre(), oferente.getResidencia(), oferente.getCvPath());
    }

    private OferenteResponse toResponse(Oferente oferente) {
        return new OferenteResponse(
                oferente.getIdOferente(),
                oferente.getNombre(),
                oferente.getResidencia(),
                oferente.getCvPath()
        );
    }
}
