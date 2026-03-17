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

import java.util.List;

@Service
public class PuestoService {
    private static final Logger log = LoggerFactory.getLogger(PuestoService.class);

    private final IPuestoRepository repository;
    private final EmpresaService empresaService;

    public PuestoService(IPuestoRepository repository, EmpresaService empresaService) {
        this.repository = repository;
        this.empresaService = empresaService;
    }

    public List<PuestoResponse> getAllPuestos() {
        log.info("Fetching all puestos");
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public PuestoResponse getPuestoById(Integer id) {
        log.info("Fetching puesto with id {}", id);
        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        return toResponse(puesto);
    }

    public Puesto getDomainPuestoById(Integer id) {
        log.info("Fetching domain puesto with id {}", id);
        return repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
    }

    public PuestoResponse createPuesto(CreatePuestoRequest request) {
        log.info("Creating puesto (empresaId={})", request.getIdEmpresa());

        Empresa empresa = empresaService.getDomainEmpresaById(request.getIdEmpresa());

        Puesto puesto = new Puesto(
                0,
                request.getTitulo().trim(),
                request.getDescripcion().trim(),
                request.getSalario(),
                request.getEstado(),
                empresa
        );

        Puesto saved = repository.save(puesto);
        return toResponse(saved);
    }

    public PuestoResponse updatePuesto(Integer id, UpdatePuestoRequest request) {
        log.info("Updating puesto with id {} (empresaId={})", id, request.getIdEmpresa());

        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));
        Empresa empresa = empresaService.getDomainEmpresaById(request.getIdEmpresa());

        puesto.setTitulo(request.getTitulo().trim());
        puesto.setDescripcion(request.getDescripcion().trim());
        puesto.setSalario(request.getSalario());
        puesto.setEstado(request.getEstado());
        puesto.setEmpresa(empresa);

        Puesto updated = repository.update(puesto);
        return toResponse(updated);
    }

    public UpdatePuestoRequest buildUpdateRequest(Integer id) {
        log.info("Building update request for puesto id {}", id);
        Puesto puesto = repository.findById(id).orElseThrow(() -> new PuestoNotFoundException(id));

        Integer idEmpresa = (puesto.getEmpresa() == null) ? null : puesto.getEmpresa().getIdEmpresa();

        return new UpdatePuestoRequest(
                puesto.getTitulo(),
                puesto.getDescripcion(),
                puesto.getSalario(),
                puesto.getEstado(),
                idEmpresa
        );
    }

    private PuestoResponse toResponse(Puesto puesto) {
        Integer idEmpresa = (puesto.getEmpresa() == null) ? null : puesto.getEmpresa().getIdEmpresa();

        return new PuestoResponse(
                puesto.getIdPuesto(),
                puesto.getTitulo(),
                puesto.getDescripcion(),
                puesto.getSalario(),
                puesto.getEstado(),
                idEmpresa
        );
    }
}
