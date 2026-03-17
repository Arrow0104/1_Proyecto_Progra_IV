package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findAll();

    List<Puesto> findAllActive();

    Optional<Puesto> findById(Long id);

    List<Puesto> findByTituloContaining(String titulo);

    List<Puesto> findByActiveTrue();

    List<Puesto> findByActiveTrueAndTituloContaining(String titulo);

    List<Puesto> findByEmpresaIdEmpresa(Long empresaId);

    List<Puesto> findByActiveTrueAndEmpresaIdEmpresa(Long empresaId);

    Puesto save(Puesto puesto);

    Puesto update(Puesto puesto);
}
