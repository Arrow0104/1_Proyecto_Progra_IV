package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IOferenteRepository extends JpaRepository<Oferente, Long> {

    List<Oferente> findAll();

    List<Oferente> findAllActive();

    Optional<Oferente> findById(Long id);

    List<Oferente> findByNombreContaining(String nombre);

    List<Oferente> findByActiveTrue();

    List<Oferente> findByActiveTrueAndNombreContaining(String nombre);

    Oferente save(Oferente oferente);

    Oferente update(Oferente oferente);
}
