package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Puesto;

import java.util.List;
import java.util.Optional;

public interface IPuestoRepository {
    List<Puesto> findAll();
    Optional<Puesto> findById(Integer idPuesto);
    Puesto save(Puesto puesto);
    Puesto update(Puesto puesto);
}
