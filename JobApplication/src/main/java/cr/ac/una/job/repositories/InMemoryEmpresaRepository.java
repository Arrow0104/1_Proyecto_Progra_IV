package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Empresa;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile({"default", "dev", "test"})
public class InMemoryEmpresaRepository implements IEmpresaRepository {

    private final List<Empresa> empresas = new ArrayList<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        // Datos de ejemplo
        save(new Empresa(0, "Empresa Demo 1", "8888-1111", null));
        save(new Empresa(0, "Empresa Demo 2", "8888-2222", null));
    }

    @Override
    public List<Empresa> findAll() {
        return new ArrayList<>(empresas);
    }

    @Override
    public Optional<Empresa> findById(Integer idEmpresa) {
        return empresas.stream().filter(e -> e.getIdEmpresa() == idEmpresa).findFirst();
    }

    @Override
    public Empresa save(Empresa empresa) {
        if (empresa.getIdEmpresa() <= 0) {
            empresa.setIdEmpresa(sequence.incrementAndGet());
            empresas.add(empresa);
        } else {
            empresas.removeIf(e -> e.getIdEmpresa() == empresa.getIdEmpresa());
            empresas.add(empresa);
        }
        return empresa;
    }

    @Override
    public Empresa update(Empresa empresa) {
        for (int i = 0; i < empresas.size(); i++) {
            if (empresas.get(i).getIdEmpresa() == empresa.getIdEmpresa()) {
                empresas.set(i, empresa);
                return empresa;
            }
        }
        throw new IllegalArgumentException("Empresa con ID " + empresa.getIdEmpresa() + " no encontrada.");
    }
}
