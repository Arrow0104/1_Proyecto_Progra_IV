package cr.ac.una.job.dtos.usuario;

import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UpdateUsuarioRequest {

    @NotBlank(message = "El correo es requerido")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 150, message = "El correo no puede exceder 150 caracteres")
    private String correo;

    @NotBlank(message = "La identificación es requerida")
    @Size(min = 5, max = 30, message = "La identificación debe tener entre 5 y 30 caracteres")
    private String identificacion;


    @Size(min = 4, max = 100, message = "La contraseña debe tener entre 4 y 100 caracteres")
    private String password;

    @NotNull(message = "El rol es requerido")
    private Rol rol;

    @NotNull(message = "El estado es requerido")
    private EstadoUsuario estado;

    public UpdateUsuarioRequest() {}

    public UpdateUsuarioRequest(String correo, String identificacion, String password, Rol rol, EstadoUsuario estado) {
        this.correo = correo;
        this.identificacion = identificacion;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public EstadoUsuario getEstado() {
        return estado;
    }

    public void setEstado(EstadoUsuario estado) {
        this.estado = estado;
    }
}
