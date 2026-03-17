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

import java.util.List;

@Service
public class UsuarioService {
    private static final Logger log = LoggerFactory.getLogger(UsuarioService.class);

    private final IUsuarioRepository repository;

    public UsuarioService(IUsuarioRepository repository) {
        this.repository = repository;
    }

    public List<UsuarioResponse> getAllUsuarios() {
        log.info("Fetching all usuarios");
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    public UsuarioResponse getUsuarioById(Integer id) {
        log.info("Fetching usuario with id {}", id);
        Usuario usuario = repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
        return toResponse(usuario);
    }

    public Usuario getDomainUsuarioById(Integer id) {
        log.info("Fetching domain usuario with id {}", id);
        return repository.findById(id).orElseThrow(() -> new UsuarioNotFoundException(id));
    }

    public UsuarioResponse createUsuario(CreateUsuarioRequest request) {
        log.info("Creating usuario (correo={})", request.getCorreo());

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(0);
        usuario.setCorreo(request.getCorreo().trim());
        usuario.setIdentificacion(request.getIdentificacion().trim());
        usuario.setPassword(request.getPassword()); // ya existe setPassword
        usuario.setRol(request.getRol());
        usuario.setEstado(request.getEstado());

        Usuario saved = repository.save(usuario);
        return toResponse(saved);
    }

    public UsuarioResponse updateUsuario(Integer id, UpdateUsuarioRequest request) {
        log.info("Updating usuario with id {}", id);

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

    public UpdateUsuarioRequest buildUpdateRequest(Integer id) {
        log.info("Building update request for usuario id {}", id);

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
        return new UsuarioResponse(u.getIdUsuario(), u.getCorreo(), u.getIdentificacion(), u.getRol(), u.getEstado());
    }
}
