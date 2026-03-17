package cr.ac.una.job.dtos.usuario;

import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;

public record UsuarioResponse(
        Integer idUsuario,
        String correo,
        String identificacion,
        Rol rol,
        EstadoUsuario estado
) {
}
