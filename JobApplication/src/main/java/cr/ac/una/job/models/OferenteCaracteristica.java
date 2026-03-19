package cr.ac.una.job.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "oferente_caracteristicas")
@IdClass(OferenteCaracteristica.OferenteCaracteristicaId.class)
public class OferenteCaracteristica {

    @Id
    @ManyToOne
    @JoinColumn(name = "oferente_id", nullable = false)
    private Oferente oferente;

    @Id
    @ManyToOne
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @Column(nullable = false)
    private int nivel;

    public OferenteCaracteristica() {}

    public OferenteCaracteristica(Oferente oferente, Caracteristica caracteristica, int nivel) {
        this.oferente = oferente;
        this.caracteristica = caracteristica;
        this.nivel = nivel;
    }

    public Oferente getOferente() { return oferente; }
    public void setOferente(Oferente oferente) { this.oferente = oferente; }

    public Caracteristica getCaracteristica() { return caracteristica; }
    public void setCaracteristica(Caracteristica caracteristica) { this.caracteristica = caracteristica; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OferenteCaracteristica that)) return false;
        return Objects.equals(oferente, that.oferente) && Objects.equals(caracteristica, that.caracteristica);
    }

    @Override
    public int hashCode() { return Objects.hash(oferente, caracteristica); }

    // ── Clave compuesta ──────────────────────────────────────────────────────
    public static class OferenteCaracteristicaId implements Serializable {
        private Long oferente;
        private Long caracteristica;

        public OferenteCaracteristicaId() {}
        public OferenteCaracteristicaId(Long oferente, Long caracteristica) {
            this.oferente = oferente;
            this.caracteristica = caracteristica;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OferenteCaracteristicaId that)) return false;
            return Objects.equals(oferente, that.oferente) && Objects.equals(caracteristica, that.caracteristica);
        }

        @Override
        public int hashCode() { return Objects.hash(oferente, caracteristica); }
    }
}
