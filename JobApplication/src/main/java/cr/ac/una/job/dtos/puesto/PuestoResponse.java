package cr.ac.una.job.dtos.puesto;

import cr.ac.una.job.models.EstadoPuesto;

import java.math.BigDecimal;

public record PuestoResponse(
        Integer idPuesto,
        String titulo,
        String descripcion,
        BigDecimal salario,
        EstadoPuesto estado,
        Integer idEmpresa
) {
}
