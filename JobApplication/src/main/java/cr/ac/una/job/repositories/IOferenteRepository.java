package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Oferente;

import java.util.List;
import java.util.Optional;

public interface IOferenteRepository {
    List<Oferente> findAll();
    Optional<Oferente> findById(Integer idOferente);
    Oferente save(Oferente oferente);
    Oferente update(Oferente oferente);
}
