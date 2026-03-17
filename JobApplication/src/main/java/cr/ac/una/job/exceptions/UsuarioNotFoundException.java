package cr.ac.una.job.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Long id) {
        super("Usuario con id " + id + " no encontrado");
    }
}
