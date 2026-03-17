package cr.ac.una.job.dtos.oferente;

import java.time.LocalDateTime;

public class OferenteResponse {
    private Long idOferente;
    private String nombre;
    private String residencia;
    private String cvPath;
    private boolean active;
    private LocalDateTime createdAt;

    public OferenteResponse(Long idOferente, String nombre, String residencia, String cvPath, boolean active, LocalDateTime createdAt) {
        this.idOferente = idOferente;
        this.nombre = nombre;
        this.residencia = residencia;
        this.cvPath = cvPath;
        this.active = active;
        this.createdAt = createdAt;
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
}
