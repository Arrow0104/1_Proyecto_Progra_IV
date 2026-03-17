package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findAll();

    List<Usuario> findAllActive();

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByIdentificacion(String identificacion);

    List<Usuario> findByCorreoContaining(String correo);

    List<Usuario> findByActiveTrue();

    List<Usuario> findByActiveTrueAndCorreoContaining(String correo);

    Usuario save(Usuario usuario);

    Usuario update(Usuario usuario);
}
