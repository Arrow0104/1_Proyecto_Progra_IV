package cr.ac.una.job.models;
import java.util.Objects;

public class Caracteristica {
    private int idCaracteristica;
    private String nombre;

    // en el diagrama: idPadre : Integer (nullable) y relación padre 0..1 (auto-referencia)
    private Integer idPadre;
    private Caracteristica padre;

    public Caracteristica() {}

    public Caracteristica(int idCaracteristica, String nombre, Integer idPadre, Caracteristica padre) {
        this.idCaracteristica = idCaracteristica;
        this.nombre = nombre;
        this.idPadre = idPadre;
        this.padre = padre;
    }

    public int getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(int idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdPadre() {
        return idPadre;
    }

    public void setIdPadre(Integer idPadre) {
        this.idPadre = idPadre;
    }

    public Caracteristica getPadre() {
        return padre;
    }

    public void setPadre(Caracteristica padre) {
        this.padre = padre;
        this.idPadre = (padre == null) ? null : padre.getIdCaracteristica();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caracteristica that)) return false;
        return idCaracteristica == that.idCaracteristica;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCaracteristica);
    }

    @Override
    public String toString() {
        return "Caracteristica{" +
                "idCaracteristica=" + idCaracteristica +
                ", nombre='" + nombre + '\'' +
                ", idPadre=" + idPadre +
                '}';
    }

}
