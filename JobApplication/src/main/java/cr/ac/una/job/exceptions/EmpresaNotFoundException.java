package cr.ac.una.job.exceptions;

public class EmpresaNotFoundException extends RuntimeException {
    public EmpresaNotFoundException(Integer id) {
        super("Empresa con id " + id + " no encontrada");
    }
}
