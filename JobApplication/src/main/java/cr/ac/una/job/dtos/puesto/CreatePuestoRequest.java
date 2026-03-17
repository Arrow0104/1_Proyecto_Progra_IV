package cr.ac.una.job.dtos.puesto;

import cr.ac.una.job.models.EstadoPuesto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class CreatePuestoRequest {
    @NotBlank(message = "El título es requerido")
    @Size(min = 2, max = 120, message = "El título debe tener entre 2 y 120 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción es requerida")
    @Size(min = 5, max = 2000, message = "La descripción debe tener entre 5 y 2000 caracteres")
    private String descripcion;

    @NotNull(message = "El salario es requerido")
    @PositiveOrZero(message = "El salario no puede ser negativo")
    private BigDecimal salario;

    @NotNull(message = "El estado es requerido")
    private EstadoPuesto estado;

    @NotNull(message = "El idEmpresa es requerido")
    private Integer idEmpresa;

    public CreatePuestoRequest() {}

    public CreatePuestoRequest(String titulo, String descripcion, BigDecimal salario, EstadoPuesto estado, Integer idEmpresa) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.salario = salario;
        this.estado = estado;
        this.idEmpresa = idEmpresa;
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

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
}
