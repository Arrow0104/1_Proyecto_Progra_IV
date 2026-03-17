package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {

    List<Usuario> findByActiveTrue();

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByIdentificacion(String identificacion);

    List<Usuario> findByCorreoContaining(String correo);

    List<Usuario> findByActiveTrueAndCorreoContaining(String correo);
}
