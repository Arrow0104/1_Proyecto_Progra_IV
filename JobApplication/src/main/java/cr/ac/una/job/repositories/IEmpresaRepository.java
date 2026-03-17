package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEmpresaRepository extends JpaRepository<Empresa, Long> {

    List<Empresa> findByActiveTrue();

    Optional<Empresa> findByNombreContaining(String nombre);

    List<Empresa> findByActiveTrueAndNombreContaining(String nombre);
}