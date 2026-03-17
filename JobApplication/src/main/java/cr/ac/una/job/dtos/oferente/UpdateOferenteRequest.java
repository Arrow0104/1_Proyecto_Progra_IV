package cr.ac.una.job.dtos.oferente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UpdateOferenteRequest {
    @NotBlank(message = "El nombre es requerido")
    @Size(min = 2, max = 120, message = "El nombre debe tener entre 2 y 120 caracteres")
    private String nombre;

    @NotBlank(message = "La residencia es requerida")
    @Size(min = 2, max = 120, message = "La residencia debe tener entre 2 y 120 caracteres")
    private String residencia;

    private String cvPath;

    public UpdateOferenteRequest() {}

    public UpdateOferenteRequest(String nombre, String residencia, String cvPath) {
        this.nombre = nombre;
        this.residencia = residencia;
        this.cvPath = cvPath;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getResidencia() {
        return residencia;
    }

    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    public String getCvPath() {
        return cvPath;
    }

    public void setCvPath(String cvPath) {
        this.cvPath = cvPath;
    }
}
