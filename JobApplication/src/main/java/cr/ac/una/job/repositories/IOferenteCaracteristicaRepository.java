package cr.ac.una.job.repositories;

import cr.ac.una.job.models.OferenteCaracteristica;
import cr.ac.una.job.models.OferenteCaracteristica.OferenteCaracteristicaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOferenteCaracteristicaRepository
        extends JpaRepository<OferenteCaracteristica, OferenteCaracteristicaId> {

    List<OferenteCaracteristica> findByOferenteIdOferente(Long idOferente);

    void deleteByOferenteIdOferenteAndCaracteristicaIdCaracteristica(Long idOferente, Long idCaracteristica);
}