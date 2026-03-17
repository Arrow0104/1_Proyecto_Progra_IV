package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Empresa;

import java.util.List;
import java.util.Optional;

public interface IEmpresaRepository {
    List<Empresa> findAll();
    Optional<Empresa> findById(Integer idEmpresa);
    Empresa save(Empresa empresa);
    Empresa update(Empresa empresa);
}
