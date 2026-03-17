package cr.ac.una.job.services;

import cr.ac.una.job.dtos.puesto.CreatePuestoRequest;
import cr.ac.una.job.dtos.puesto.PuestoResponse;
import cr.ac.una.job.dtos.puesto.UpdatePuestoRequest;
import cr.ac.una.job.exceptions.PuestoNotFoundException;
import cr.ac.una.job.models.Empresa;
import cr.ac.una.job.models.Puesto;
import cr.ac.una.job.repositories.IPuestoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PuestoService {
    private static final Logger log = LoggerFactory.getLogger(PuestoService.class);

    private final IPuestoRepository repository;
    private final EmpresaService empresaService;

    public PuestoService(IPuestoRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    @Transactional(readOnly = true)
    public List<PuestoResponse> getAllPuestos() {
        log.info("Fetching all puestos from the database");
        return repository.findByActiveTrue().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PuestoResponse getPuestoById(Long id) {
        log.info("Fetching puesto with id {} from the database", id);
        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        return toResponse(puesto);
    }

    @Transactional(readOnly = true)
    public Puesto getDomainPuestoById(Long id) {
        log.info("Fetching domain puesto with id {} from the database", id);
        return repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<PuestoResponse> searchPuestosByTitulo(String titulo) {
        if (titulo == null || titulo.trim().length() < 2) {
            log.warn("Search term '{}' is too short. Returning empty list.", titulo);
            return List.of();
        }
        log.info("Searching puestos with titulo containing '{}' in the database", titulo);
        return repository.findByActiveTrueAndTituloContaining(titulo).stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PuestoResponse> getPuestosByEmpresa(Long empresaId) {
        log.info("Fetching puestos for empresa with id {} from the database", empresaId);
        return repository.findByActiveTrueAndEmpresaIdEmpresa(empresaId).stream().map(this::toResponse).toList();
    }

    public PuestoResponse createPuesto(CreatePuestoRequest request) {
        log.info("Creating new puesto from the database (empresaId={})", request.getIdEmpresa());

        Empresa empresa = empresaService.getDomainEmpresaById(request.getIdEmpresa());

        Puesto puesto = new Puesto(
                null,
                request.getTitulo().trim(),
                request.getDescripcion().trim(),
                request.getSalario(),
                request.getEstado(),
                true,
                LocalDateTime.now(),
                empresa
        );

        Puesto saved = repository.save(puesto);
        return toResponse(saved);
    }

    public PuestoResponse updatePuesto(Long id, UpdatePuestoRequest request) {
        log.info("Updating puesto with id {} in the database (empresaId={})", id, request.getIdEmpresa());

        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        Empresa empresa = empresaService.getDomainEmpresaById(request.getIdEmpresa());

        puesto.setTitulo(request.getTitulo().trim());
        puesto.setDescripcion(request.getDescripcion().trim());
        puesto.setSalario(request.getSalario());
        puesto.setEstado(request.getEstado());
        puesto.setEmpresa(empresa);

        Puesto updated = repository.save(puesto);
        return toResponse(updated);
    }

    public void changeActiveStatus(Long id, boolean value) {
        log.info("Changing active status of puesto with id {} to {} in the database", id, value);

        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        puesto.setActive(value);

        repository.save(puesto);
    }

    public void deleteLogical(Long id) {
        log.info("Logically deleting puesto with id {} in the database", id);

        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        puesto.setActive(false);

        repository.save(puesto);
    }

    public UpdatePuestoRequest buildUpdateRequest(Long id) {
        log.info("Building update request for puesto id {} from the database", id);
        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));

        Long idEmpresa = (puesto.getEmpresa() == null) ? null : puesto.getEmpresa().getIdEmpresa();

        return new UpdatePuestoRequest(
                puesto.getTitulo(),
                puesto.getDescripcion(),
                puesto.getSalario(),
                puesto.getEstado(),
                idEmpresa
        );
    }

    private PuestoResponse toResponse(Puesto puesto) {
        Long idEmpresa = (puesto.getEmpresa() == null) ? null : puesto.getEmpresa().getIdEmpresa();

        return new PuestoResponse(
                puesto.getIdPuesto(),
                puesto.getTitulo(),
                puesto.getDescripcion(),
                puesto.getSalario(),
                puesto.getEstado(),
                idEmpresa,
                puesto.isActive(),
                puesto.getCreatedAt()
        );
    }
}
