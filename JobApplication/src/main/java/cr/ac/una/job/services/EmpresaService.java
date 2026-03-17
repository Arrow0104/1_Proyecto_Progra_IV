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

import java.util.List;

@Service
public class EmpresaService {
    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);
    private final IEmpresaRepository repository;

    public EmpresaService(IEmpresaRepository repository) {
        this.repository = repository;
    }

    public List<EmpresaResponse> getAllEmpresas() {
        log.info("Fetching all empresas");
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public EmpresaResponse getEmpresaById(Integer id) {
        log.info("Fetching empresa with id {}", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        return toResponse(empresa);
    }

    public Empresa getDomainEmpresaById(Integer id) {
        log.info("Fetching domain empresa with id {}", id);
        return repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
    }

    public EmpresaResponse createEmpresa(CreateEmpresaRequest request) {
        log.info("Creating empresa");
        Empresa empresa = new Empresa(0, request.getNombre().trim(), request.getTelefono().trim(), null);
        Empresa saved = repository.save(empresa);
        return toResponse(saved);
    }

    public EmpresaResponse updateEmpresa(Integer id, UpdateEmpresaRequest request) {
        log.info("Updating empresa with id {}", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));

        empresa.setNombre(request.getNombre().trim());
        empresa.setTelefono(request.getTelefono().trim());

        Empresa updated = repository.update(empresa);
        return toResponse(updated);
    }

    public UpdateEmpresaRequest buildUpdateRequest(Integer id) {
        log.info("Building update request for empresa id {}", id);
        Empresa empresa = repository.findById(id).orElseThrow(() -> new EmpresaNotFoundException(id));
        return new UpdateEmpresaRequest(empresa.getNombre(), empresa.getTelefono());
    }

    private EmpresaResponse toResponse(Empresa empresa) {
        return new EmpresaResponse(empresa.getIdEmpresa(), empresa.getNombre(), empresa.getTelefono());
    }
}
