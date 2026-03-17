package cr.ac.una.job.exceptions;

public class UsuarioNotFoundException extends RuntimeException {
    public UsuarioNotFoundException(Integer id) {
        super("Usuario con id " + id + " no encontrado");
    }
}
