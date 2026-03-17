package cr.ac.una.job.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "oferentes")
public class Oferente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOferente;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 255)
    private String residencia;

    @Column(nullable = false)
    private String cvPath;

    @Column(nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // Relación 0..1 con Usuario (según diagrama)
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    public Oferente() {}

    public Oferente(Long idOferente, String nombre, String residencia, String cvPath, boolean active, LocalDateTime createdAt, Usuario usuario) {
        this.idOferente = idOferente;
        this.nombre = nombre;
        this.residencia = residencia;
        this.cvPath = cvPath;
        this.active = active;
        this.createdAt = createdAt;
        this.usuario = usuario;
    }

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        active = true;
    }

    public Long getIdOferente() {
        return idOferente;
    }

    public void setIdOferente(Long idOferente) {
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
        return Objects.equals(idOferente, oferente.idOferente);
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
                ", active=" + active +
                ", createdAt=" + createdAt +
                '}';
    }
}
