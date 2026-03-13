package cr.ac.una.job.models;

import java.util.Objects;

public class Oferente {
    private int idOferente;
    private String nombre;
    private String residencia;
    private String cvPath;

    // Relación 0..1 con Usuario (según diagrama)
    private Usuario usuario;

    public Oferente() {}

    public Oferente(int idOferente, String nombre, String residencia, String cvPath, Usuario usuario) {
        this.idOferente = idOferente;
        this.nombre = nombre;
        this.residencia = residencia;
        this.cvPath = cvPath;
        this.usuario = usuario;
    }

    public int getIdOferente() {
        return idOferente;
    }

    public void setIdOferente(int idOferente) {
        this.idOferente = idOferente;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Oferente oferente)) return false;
        return idOferente == oferente.idOferente;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOferente);
    }

    @Override
    public String toString() {
        return "Oferente{" +
                "idOferente=" + idOferente +
                ", nombre='" + nombre + '\'' +
                ", residencia='" + residencia + '\'' +
                ", cvPath='" + cvPath + '\'' +
                '}';
    }
}
