package cr.ac.una.job.exceptions;

public class PuestoNotFoundException extends RuntimeException {
    public PuestoNotFoundException(Integer id) {
        super("Puesto con id " + id + " no encontrado");
    }
}
