package cr.ac.una.job.dtos.puesto;

import cr.ac.una.job.models.EstadoPuesto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PuestoResponse {
    private Long idPuesto;
    private String titulo;
    private String descripcion;
    private BigDecimal salario;
    private EstadoPuesto estado;
    private Long idEmpresa;
    private boolean active;
    private LocalDateTime createdAt;

    public PuestoResponse(Long idPuesto, String titulo, String descripcion, BigDecimal salario, EstadoPuesto estado, Long idEmpresa, boolean active, LocalDateTime createdAt) {
        this.idPuesto = idPuesto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.salario = salario;
        this.estado = estado;
        this.idEmpresa = idEmpresa;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(Long idPuesto) {
        this.idPuesto = idPuesto;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }

    public EstadoPuesto getEstado() {
        return estado;
    }

    public void setEstado(EstadoPuesto estado) {
        this.estado = estado;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
        this.idEmpresa = idEmpresa;
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
}
