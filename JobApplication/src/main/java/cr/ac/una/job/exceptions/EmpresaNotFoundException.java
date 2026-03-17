package cr.ac.una.job.exceptions;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException(Long id) {
        super("Empresa con id " + id + " no encontrada");
    }
}
