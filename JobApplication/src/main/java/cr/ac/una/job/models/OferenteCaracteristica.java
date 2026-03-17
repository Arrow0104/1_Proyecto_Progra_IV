package cr.ac.una.job.models;
import java.util.Objects;

public class OferenteCaracteristica {
    private Long idOferente;
    private int idCaracteristica;
    private int nivel;

    private Oferente oferente;
    private Caracteristica caracteristica;

    public OferenteCaracteristica() {}

    public OferenteCaracteristica(Long idOferente, int idCaracteristica, int nivel, Oferente oferente, Caracteristica caracteristica) {
        this.idOferente = idOferente;
        this.idCaracteristica = idCaracteristica;
        this.nivel = nivel;
        this.oferente = oferente;
        this.caracteristica = caracteristica;
    }

    public Long getIdOferente() {
        return idOferente;
    }

    public void setIdOferente(Long idOferente) {
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
        else this.idOferente = null;
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
        return idCaracteristica == that.idCaracteristica && Objects.equals(idOferente, that.idOferente);
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
