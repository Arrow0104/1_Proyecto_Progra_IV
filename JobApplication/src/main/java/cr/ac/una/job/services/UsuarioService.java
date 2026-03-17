package cr.ac.una.job.services;

import cr.ac.una.job.dtos.usuario.CreateUsuarioRequest;
import cr.ac.una.job.dtos.usuario.UpdateUsuarioRequest;
import cr.ac.una.job.dtos.usuario.UsuarioResponse;
import cr.ac.una.job.exceptions.UsuarioNotFoundException;
import cr.ac.una.job.models.Usuario;
import cr.ac.una.job.repositories.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> getAllUsuarios() {
        log.info("Fetching all usuarios from the database");
        return repository.findAllActive().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioResponse getUsuarioById(Long id) {
        log.info("Fetching usuario with id {} from the database", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        return toResponse(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario getDomainUsuarioById(Long id) {
        log.info("Fetching domain usuario with id {} from the database", id);
        return repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> searchUsuariosByCorreo(String correo) {
        if (correo == null || correo.trim().length() < 2) {
            log.warn("Search term '{}' is too short. Returning empty list.", correo);
            return List.of();
        }
        log.info("Searching usuarios with correo containing '{}' in the database", correo);
        return repository.findByActiveTrueAndCorreoContaining(correo).stream().map(this::toResponse).toList();
    }

    public UsuarioResponse createUsuario(CreateUsuarioRequest request) {
        log.info("Creating new usuario from the database (correo={})", request.getCorreo());

        Usuario usuario = new Usuario(
                null,
                request.getCorreo().trim(),
                request.getIdentificacion().trim(),
                request.getPassword(),
                request.getRol(),
                request.getEstado(),
                true,
                LocalDateTime.now()
        );

        Usuario saved = repository.save(usuario);
        return toResponse(saved);
    }

    public UsuarioResponse updateUsuario(Long id, UpdateUsuarioRequest request) {
        log.info("Updating usuario with id {} in the database", id);

        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        usuario.setCorreo(request.getCorreo().trim());
        usuario.setIdentificacion(request.getIdentificacion().trim());

        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            usuario.setPassword(request.getPassword().trim());
        }

        usuario.setRol(request.getRol());
        usuario.setEstado(request.getEstado());

        Usuario updated = repository.update(usuario);
        return toResponse(updated);
    }

    public void changeActiveStatus(Long id, boolean value) {
        log.info("Changing active status of usuario with id {} to {} in the database", id, value);

        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setActive(value);

        repository.update(usuario);
    }

    public void deleteLogical(Long id) {
        log.info("Logically deleting usuario with id {} in the database", id);

        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        usuario.setActive(false);

        repository.update(usuario);
    }

    public UpdateUsuarioRequest buildUpdateRequest(Long id) {
        log.info("Building update request for usuario id {} from the database", id);

        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));

        return new UpdateUsuarioRequest(
                usuario.getCorreo(),
                usuario.getIdentificacion(),
                null, // no mostramos password
                usuario.getRol(),
                usuario.getEstado()
        );
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getIdUsuario(),
                u.getCorreo(),
                u.getIdentificacion(),
                u.getRol(),
                u.getEstado(),
                u.isActive(),
                u.getCreatedAt()
        );
    }
}
