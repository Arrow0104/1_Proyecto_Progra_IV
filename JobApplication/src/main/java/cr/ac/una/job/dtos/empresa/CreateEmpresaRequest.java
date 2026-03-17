package cr.ac.una.job.dtos.empresa;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateEmpresaRequest {
    @NotBlank(message = "El nombre de la empresa es requerido")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El teléfono es requerido")
    @Size(min = 8, max = 20, message = "El teléfono debe tener entre 8 y 20 caracteres")
    private String telefono;

    public CreateEmpresaRequest() {}

    public CreateEmpresaRequest(String nombre, String telefono) {
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
