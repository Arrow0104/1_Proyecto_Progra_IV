package cr.ac.una.job.services;

import cr.ac.una.job.dtos.empresa.CreateEmpresaRequest;
import cr.ac.una.job.dtos.empresa.EmpresaResponse;
import cr.ac.una.job.dtos.empresa.UpdateEmpresaRequest;
import cr.ac.una.job.exceptions.EmpresaNotFoundException;
import cr.ac.una.job.models.Empresa;
import cr.ac.una.job.repositories.IEmpresaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EmpresaService {
    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);

    private final IEmpresaRepository repository;

    public EmpresaService(IEmpresaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> getAllEmpresas() {
        log.info("Fetching all empresas from the database");
        return repository.findAllActive().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EmpresaResponse getEmpresaById(Long id) {
        log.info("Fetching empresa with id {} from the database", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        return toResponse(empresa);
    }

    @Transactional(readOnly = true)
    public Empresa getDomainEmpresaById(Long id) {
        log.info("Fetching domain empresa with id {} from the database", id);
        return repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<EmpresaResponse> searchEmpresasByName(String nombre) {
        if (nombre == null || nombre.trim().length() < 2) {
            log.warn("Search term '{}' is too short. Returning empty list.", nombre);
            return List.of();
        }
        log.info("Searching empresas with name containing '{}' in the database", nombre);
        return repository.findByActiveTrueAndNombreContaining(nombre).stream().map(this::toResponse).toList();
    }

    public EmpresaResponse createEmpresa(CreateEmpresaRequest request) {
        log.info("Creating new empresa from the database");
        Empresa empresa = new Empresa(
                null,
                request.getNombre().trim(),
                request.getTelefono().trim(),
                true,
                LocalDateTime.now(),
                null
        );
        Empresa saved = repository.save(empresa);
        return toResponse(saved);
    }

    public EmpresaResponse updateEmpresa(Long id, UpdateEmpresaRequest request) {
        log.info("Updating empresa with id {} in the database", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));

        empresa.setNombre(request.getNombre().trim());
        empresa.setTelefono(request.getTelefono().trim());

        Empresa updated = repository.update(empresa);
        return toResponse(updated);
    }

    public void changeActiveStatus(Long id, boolean value) {
        log.info("Changing active status of empresa with id {} to {} in the database", id, value);

        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        empresa.setActive(value);

        repository.update(empresa);
    }

    public void deleteLogical(Long id) {
        log.info("Logically deleting empresa with id {} in the database", id);

        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        empresa.setActive(false);

        repository.update(empresa);
    }

    public UpdateEmpresaRequest buildUpdateRequest(Long id) {
        log.info("Building update request for empresa id {} from the database", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        return new UpdateEmpresaRequest(empresa.getNombre(), empresa.getTelefono());
    }

    private EmpresaResponse toResponse(Empresa empresa) {
        return new EmpresaResponse(
                empresa.getIdEmpresa(),
                empresa.getNombre(),
                empresa.getTelefono(),
                empresa.isActive(),
                empresa.getCreatedAt()
        );
    }
}
