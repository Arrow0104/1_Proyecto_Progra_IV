package cr.ac.una.job.models;
import java.util.Objects;

public class OferenteCaracteristica {
    private int idOferente;
    private int idCaracteristica;
    private int nivel;

    private Oferente oferente;
    private Caracteristica caracteristica;

    public OferenteCaracteristica() {}

    public OferenteCaracteristica(int idOferente, int idCaracteristica, int nivel, Oferente oferente, Caracteristica caracteristica) {
        this.idOferente = idOferente;
        this.idCaracteristica = idCaracteristica;
        this.nivel = nivel;
        this.oferente = oferente;
        this.caracteristica = caracteristica;
    }

    public int getIdOferente() {
        return idOferente;
    }

    public void setIdOferente(int idOferente) {
        this.idOferente = idOferente;
    }

    public int getIdCaracteristica() {
        return idCaracteristica;
    }

    public void setIdCaracteristica(int idCaracteristica) {
        this.idCaracteristica = idCaracteristica;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public Oferente getOferente() {
        return oferente;
    }

    public void setOferente(Oferente oferente) {
        this.oferente = oferente;
        if (oferente != null) this.idOferente = oferente.getIdOferente();
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
        if (!(o instanceof OferenteCaracteristica that)) return false;
        return idOferente == that.idOferente && idCaracteristica == that.idCaracteristica;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idOferente, idCaracteristica);
    }

    @Override
    public String toString() {
        return "OferenteCaracteristica{" +
                "idOferente=" + idOferente +
                ", idCaracteristica=" + idCaracteristica +
                ", nivel=" + nivel +
                '}';
    }
}
