package cr.ac.una.job.models;
import java.math.BigDecimal;
import java.util.Objects;

public class Puesto {
    private int idPuesto;
    private String titulo;
    private String descripcion;
    private BigDecimal salario;
    private EstadoPuesto estado;

    // En el diagrama, Empresa 1 -> 0..* Puesto
    private Empresa empresa;

    public Puesto() {}

    public Puesto(int idPuesto, String titulo, String descripcion, BigDecimal salario, EstadoPuesto estado, Empresa empresa) {
        this.idPuesto = idPuesto;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.salario = salario;
        this.estado = estado;
        this.empresa = empresa;
    }

    public int getIdPuesto() {
        return idPuesto;
    }

    public void setIdPuesto(int idPuesto) {
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

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Puesto puesto)) return false;
        return idPuesto == puesto.idPuesto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPuesto);
    }

    @Override
    public String toString() {
        return "Puesto{" +
                "idPuesto=" + idPuesto +
                ", titulo='" + titulo + '\'' +
                ", estado=" + estado +
                ", salario=" + salario +
                '}';
    }
}
