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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OferenteService {
    private static final Logger log = LoggerFactory.getLogger(OferenteService.class);

    private final IOferenteRepository repository;

    public OferenteService(IOferenteRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OferenteResponse> getAllOferentes() {
        log.info("Fetching all oferentes from the database");
        return repository.findAllActive().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OferenteResponse getOferenteById(Long id) {
        log.info("Fetching oferente with id {} from the database", id);
        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        return toResponse(oferente);
    }

    @Transactional(readOnly = true)
    public Oferente getDomainOferenteById(Long id) {
        log.info("Fetching domain oferente with id {} from the database", id);
        return repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<OferenteResponse> searchOferentesByName(String nombre) {
        if (nombre == null || nombre.trim().length() < 2) {
            log.warn("Search term '{}' is too short. Returning empty list.", nombre);
            return List.of();
        }
        log.info("Searching oferentes with name containing '{}' in the database", nombre);
        return repository.findByActiveTrueAndNombreContaining(nombre).stream().map(this::toResponse).toList();
    }

    public OferenteResponse createOferente(CreateOferenteRequest request) {
        log.info("Creating new oferente from the database");
        Oferente oferente = new Oferente(
                null,
                request.getNombre().trim(),
                request.getResidencia().trim(),
                (request.getCvPath() == null ? null : request.getCvPath().trim()),
                true,
                LocalDateTime.now(),
                null
        );

        Oferente saved = repository.save(oferente);
        return toResponse(saved);
    }

    public OferenteResponse updateOferente(Long id, UpdateOferenteRequest request) {
        log.info("Updating oferente with id {} in the database", id);

        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));

        oferente.setNombre(request.getNombre().trim());
        oferente.setResidencia(request.getResidencia().trim());
        oferente.setCvPath(request.getCvPath() == null ? null : request.getCvPath().trim());

        Oferente updated = repository.update(oferente);
        return toResponse(updated);
    }

    public void changeActiveStatus(Long id, boolean value) {
        log.info("Changing active status of oferente with id {} to {} in the database", id, value);

        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        oferente.setActive(value);

        repository.update(oferente);
    }

    public void deleteLogical(Long id) {
        log.info("Logically deleting oferente with id {} in the database", id);

        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        oferente.setActive(false);

        repository.update(oferente);
    }

    public UpdateOferenteRequest buildUpdateRequest(Long id) {
        log.info("Building update request for oferente id {} from the database", id);
        Oferente oferente = repository.findById(id).orElseThrow(() -> new OferenteNotFoundException(id));
        return new UpdateOferenteRequest(oferente.getNombre(), oferente.getResidencia(), oferente.getCvPath());
    }

    private OferenteResponse toResponse(Oferente oferente) {
        return new OferenteResponse(
                oferente.getIdOferente(),
                oferente.getNombre(),
                oferente.getResidencia(),
                oferente.getCvPath(),
                oferente.isActive(),
                oferente.getCreatedAt()
        );
    }
}
