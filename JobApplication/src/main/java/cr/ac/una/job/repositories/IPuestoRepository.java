package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Puesto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IPuestoRepository extends JpaRepository<Puesto, Long> {

    List<Puesto> findByActiveTrue();

    List<Puesto> findByTituloContaining(String titulo);

    List<Puesto> findByActiveTrueAndTituloContaining(String titulo);

    List<Puesto> findByEmpresaIdEmpresa(Long empresaId);

    List<Puesto> findByActiveTrueAndEmpresaIdEmpresa(Long empresaId);
}
