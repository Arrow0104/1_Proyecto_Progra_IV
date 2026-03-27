package cr.ac.una.job.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "puesto_caracteristicas")
@IdClass(PuestoCaracteristica.PuestoCaracteristicaId.class)
public class PuestoCaracteristica {

    @Id
    @ManyToOne
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;

    @Id
    @ManyToOne
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;

    @Column(nullable = false)
    private int nivelRequerido;

    public PuestoCaracteristica() {}

    public PuestoCaracteristica(Puesto puesto, Caracteristica caracteristica, int nivelRequerido) {
        this.puesto = puesto;
        this.caracteristica = caracteristica;
        this.nivelRequerido = nivelRequerido;
    }

    public Puesto getPuesto() { return puesto; }
    public void setPuesto(Puesto puesto) { this.puesto = puesto; }

    public Caracteristica getCaracteristica() { return caracteristica; }
    public void setCaracteristica(Caracteristica caracteristica) { this.caracteristica = caracteristica; }

    public int getNivelRequerido() { return nivelRequerido; }
    public void setNivelRequerido(int nivelRequerido) { this.nivelRequerido = nivelRequerido; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PuestoCaracteristica that)) return false;
        return Objects.equals(puesto, that.puesto) && Objects.equals(caracteristica, that.caracteristica);
    }

    @Override
    public int hashCode() { return Objects.hash(puesto, caracteristica); }


    public static class PuestoCaracteristicaId implements Serializable {
        private Long puesto;
        private Long caracteristica;

        public PuestoCaracteristicaId() {}
        public PuestoCaracteristicaId(Long puesto, Long caracteristica) {
            this.puesto = puesto;
            this.caracteristica = caracteristica;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PuestoCaracteristicaId that)) return false;
            return Objects.equals(puesto, that.puesto) && Objects.equals(caracteristica, that.caracteristica);
        }

        @Override
        public int hashCode() { return Objects.hash(puesto, caracteristica); }
    }
}
