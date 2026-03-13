package cr.ac.una.job.models;
import java.util.Objects;

public class PuestoCaracteristica {
    private int idPuesto;
    private int idCaracteristica;
    private int nivelRequerido;

    // referencias opcionales (útiles si trabajas orientado a objetos)
    private Puesto puesto;
    private Caracteristica caracteristica;

    public PuestoCaracteristica() {}

    public PuestoCaracteristica(int idPuesto, int idCaracteristica, int nivelRequerido, Puesto puesto, Caracteristica caracteristica) {
        this.idPuesto = idPuesto;
        this.idCaracteristica = idCaracteristica;
        this.nivelRequerido = nivelRequerido;
        this.puesto = puesto;
        this.caracteristica = caracteristica;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
        this.idPuesto = idPuesto;
    }

    public int getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(int idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public int getNivelRequerido() {
        return nivelRequerido;
    }

    public void setNivelRequerido(int nivelRequerido) {
        this.nivelRequerido = nivelRequerido;
    }

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
        if (puesto != null) this.idPuesto = puesto.getIdPuesto();
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
        if (caracteristica != null) this.idCaracteristica = caracteristica.getIdCaracteristica();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuestoCaracteristica that)) return false;
        return idPuesto == that.idPuesto && idCaracteristica == that.idCaracteristica;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPuesto, idCaracteristica);
    }

    @Override
    public String toString() {
        return "PuestoCaracteristica{" +
                "idPuesto=" + idPuesto +
                ", idCaracteristica=" + idCaracteristica +
                ", nivelRequerido=" + nivelRequerido +
                '}';
    }
}
