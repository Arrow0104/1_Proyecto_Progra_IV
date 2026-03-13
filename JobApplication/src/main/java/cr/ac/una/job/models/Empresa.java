package cr.ac.una.job.models;

import java.util.Objects;

public class Empresa {
    private int idEmpresa;
    private String nombre;
    private String telefono;

    // Relación 0..1 con Usuario (según diagrama)
    private Usuario usuario;

    public Empresa() {}

    public Empresa(int idEmpresa, String nombre, String telefono, Usuario usuario) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.telefono = telefono;
        this.usuario = usuario;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empresa empresa)) return false;
        return idEmpresa == empresa.idEmpresa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idEmpresa);
    }

    @Override
    public String toString() {
        return "Empresa{" +
                "idEmpresa=" + idEmpresa +
                ", nombre='" + nombre + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
