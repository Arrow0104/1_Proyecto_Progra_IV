package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOferenteRepository extends JpaRepository<Oferente, Long> {

    List<Oferente> findByActiveTrue();

    List<Oferente> findByNombreContaining(String nombre);

    Optional<Oferente> findFirstByUsuarioIdUsuario(Long idUsuario);

    List<Oferente> findByActiveTrueAndNombreContaining(String nombre);
}
