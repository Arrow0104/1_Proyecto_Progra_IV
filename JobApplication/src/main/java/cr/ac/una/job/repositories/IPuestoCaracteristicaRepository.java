package cr.ac.una.job.repositories;

import cr.ac.una.job.models.PuestoCaracteristica;
import cr.ac.una.job.models.PuestoCaracteristica.PuestoCaracteristicaId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPuestoCaracteristicaRepository
        extends JpaRepository<PuestoCaracteristica, PuestoCaracteristicaId> {

    List<PuestoCaracteristica> findByPuestoIdPuesto(Long idPuesto);

    void deleteByPuestoIdPuesto(Long idPuesto);


    @Query("""
        SELECT DISTINCT pc.puesto.idPuesto
        FROM PuestoCaracteristica pc
        WHERE pc.caracteristica.idCaracteristica IN :ids
        AND pc.puesto.active = true
    """)
    List<Long> findPuestoIdsByCaracteristicasIn(@Param("ids") List<Long> ids);


    @Query("""
        SELECT pc.puesto.idPuesto
        FROM PuestoCaracteristica pc
        WHERE pc.caracteristica.idCaracteristica IN :ids
        AND pc.puesto.active = true
        GROUP BY pc.puesto.idPuesto
        HAVING COUNT(DISTINCT pc.caracteristica.idCaracteristica) = :total
    """)
    List<Long> findPuestoIdsByAllCaracteristicas(@Param("ids") List<Long> ids,
                                                 @Param("total") long total);
}
