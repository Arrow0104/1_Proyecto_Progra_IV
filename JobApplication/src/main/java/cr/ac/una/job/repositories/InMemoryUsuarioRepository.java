package cr.ac.una.job.repositories;

import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
import cr.ac.una.job.models.Usuario;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile({"default", "dev", "test"})
public class InMemoryUsuarioRepository implements IUsuarioRepository {

    private final List<Usuario> usuarios = new ArrayList<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        // Usuarios de ejemplo
        Usuario admin = new Usuario();
        admin.setIdUsuario(sequence.incrementAndGet());
        admin.setCorreo("admin@demo.com");
        admin.setIdentificacion("ADMIN-001");
        admin.setPassword("admin"); // demo
        admin.setRol(Rol.ADMIN);
        admin.setEstado(EstadoUsuario.ACTIVO);
        usuarios.add(admin);

        Usuario empresa = new Usuario();
        empresa.setIdUsuario(sequence.incrementAndGet());
        empresa.setCorreo("empresa@demo.com");
        empresa.setIdentificacion("EMP-001");
        empresa.setPassword("empresa"); // demo
        empresa.setRol(Rol.EMPRESA);
        empresa.setEstado(EstadoUsuario.ACTIVO);
        usuarios.add(empresa);

        Usuario oferente = new Usuario();
        oferente.setIdUsuario(sequence.incrementAndGet());
        oferente.setCorreo("oferente@demo.com");
        oferente.setIdentificacion("OFE-001");
        oferente.setPassword("oferente"); // demo
        oferente.setRol(Rol.OFERENTE);
        oferente.setEstado(EstadoUsuario.ACTIVO);
        usuarios.add(oferente);
    }

    @Override
    public List<Usuario> findAll() {
        return new ArrayList<>(usuarios);
    }

    @Override
    public Optional<Usuario> findById(Integer idUsuario) {
        return usuarios.stream().filter(u -> u.getIdUsuario() == idUsuario).findFirst();
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        if (correo == null) return Optional.empty();
        String q = correo.trim().toLowerCase();
        return usuarios.stream()
                .filter(u -> u.getCorreo() != null && u.getCorreo().trim().toLowerCase().equals(q))
                .findFirst();
    }

    @Override
    public Optional<Usuario> findByIdentificacion(String identificacion) {
        if (identificacion == null) return Optional.empty();
        String q = identificacion.trim();
        return usuarios.stream()
                .filter(u -> u.getIdentificacion() != null && u.getIdentificacion().trim().equals(q))
                .findFirst();
    }

    @Override
    public Usuario save(Usuario usuario) {
        if (usuario.getIdUsuario() <= 0) {
            usuario.setIdUsuario(sequence.incrementAndGet());
            usuarios.add(usuario);
        } else {
            usuarios.removeIf(u -> u.getIdUsuario() == usuario.getIdUsuario());
            usuarios.add(usuario);
        }
        return usuario;
    }

    @Override
    public Usuario update(Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getIdUsuario() == usuario.getIdUsuario()) {
                usuarios.set(i, usuario);
                return usuario;
            }
        }
        throw new IllegalArgumentException("Usuario con ID " + usuario.getIdUsuario() + " no encontrado.");
    }
}
