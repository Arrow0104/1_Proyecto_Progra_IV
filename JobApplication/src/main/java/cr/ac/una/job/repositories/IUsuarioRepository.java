package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository {
    List<Usuario> findAll();
    Optional<Usuario> findById(Integer idUsuario);

    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByIdentificacion(String identificacion);

    Usuario save(Usuario usuario);
    Usuario update(Usuario usuario);
}
