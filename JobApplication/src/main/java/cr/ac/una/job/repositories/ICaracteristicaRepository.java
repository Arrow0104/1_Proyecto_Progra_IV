package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ICaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    // Todas las raíces (sin padre)
    List<Caracteristica> findByPadreIsNull();

    // Hijos de un padre dado
    List<Caracteristica> findByPadreIdCaracteristica(Long padreId);
}

