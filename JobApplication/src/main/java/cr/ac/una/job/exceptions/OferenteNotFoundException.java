package cr.ac.una.job.exceptions;

public class OferenteNotFoundException extends RuntimeException {
    public OferenteNotFoundException(Long id) {
        super("Oferente con id " + id + " no encontrado");
    }
}
