package cr.ac.una.job.dtos.usuario;

import cr.ac.una.job.models.EstadoUsuario;
import cr.ac.una.job.models.Rol;
import java.time.LocalDateTime;

public class UsuarioResponse {
    private Long idUsuario;
    private String correo;
    private String identificacion;
    private Rol rol;
    private EstadoUsuario estado;
    private boolean active;
    private LocalDateTime createdAt;

    public UsuarioResponse(Long idUsuario, String correo, String identificacion, Rol rol, EstadoUsuario estado, boolean active, LocalDateTime createdAt) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.identificacion = identificacion;
        this.rol = rol;
        this.estado = estado;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
