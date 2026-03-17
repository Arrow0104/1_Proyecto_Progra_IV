package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOferenteRepository extends JpaRepository<Oferente, Long> {

    List<Oferente> findByActiveTrue();

    List<Oferente> findByNombreContaining(String nombre);

    List<Oferente> findByActiveTrueAndNombreContaining(String nombre);
}
