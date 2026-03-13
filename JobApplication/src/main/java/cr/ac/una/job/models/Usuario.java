package cr.ac.una.job.models;

import java.util.Objects;

public class Usuario {
    private int idUsuario;
    private String correo;
    private String identificacion;
    private String password;
    private Rol rol;
    private EstadoUsuario estado;

    public Usuario() {}

    public Usuario(int idUsuario, String correo, String identificacion, String passwordHash, String passwordSalt, Rol rol, EstadoUsuario estado) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.identificacion = identificacion;
        this.password = password;
        this.rol = rol;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
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

    public String getPassword() {
        return password;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Usuario usuario)) return false;
        return idUsuario == usuario.idUsuario;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUsuario);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", correo='" + correo + '\'' +
                ", identificacion='" + identificacion + '\'' +
                ", rol=" + rol +
                ", estado=" + estado +
                '}';
    }
}
