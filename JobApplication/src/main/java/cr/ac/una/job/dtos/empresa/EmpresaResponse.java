package cr.ac.una.job.dtos.empresa;

import java.time.LocalDateTime;

public class EmpresaResponse {
    private Long idEmpresa;
    private String nombre;
    private String telefono;
    private boolean active;
    private LocalDateTime createdAt;

    public EmpresaResponse(Long idEmpresa, String nombre, String telefono, boolean active, LocalDateTime createdAt) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.telefono = telefono;
        this.active = active;
        this.createdAt = createdAt;
    }

    public Long getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Long idEmpresa) {
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