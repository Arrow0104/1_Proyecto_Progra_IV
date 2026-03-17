package cr.ac.una.job.repositories;

import cr.ac.una.job.models.EstadoPuesto;
import cr.ac.una.job.models.Puesto;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile({"default", "dev", "test"})
public class InMemoryPuestoRepository implements IPuestoRepository {

    private final List<Puesto> puestos = new ArrayList<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        // OJO: Empresa se asigna en el service, aquí dejamos empresa null en los mocks
        save(new Puesto(0, "Dev Junior", "Puesto de ejemplo", new BigDecimal("500000"), EstadoPuesto.ACTIVO, null));
        save(new Puesto(0, "QA", "Puesto de ejemplo", new BigDecimal("450000"), EstadoPuesto.ACTIVO, null));
    }

    @Override
    public List<Puesto> findAll() {
        return new ArrayList<>(puestos);
    }

    @Override
    public Optional<Puesto> findById(Integer idPuesto) {
        return puestos.stream().filter(p -> p.getIdPuesto() == idPuesto).findFirst();
    }

    @Override
    public Puesto save(Puesto puesto) {
        if (puesto.getIdPuesto() <= 0) {
            puesto.setIdPuesto(sequence.incrementAndGet());
            puestos.add(puesto);
        } else {
            puestos.removeIf(p -> p.getIdPuesto() == puesto.getIdPuesto());
            puestos.add(puesto);
        }
        return puesto;
    }

    @Override
    public Puesto update(Puesto puesto) {
        for (int i = 0; i < puestos.size(); i++) {
            if (puestos.get(i).getIdPuesto() == puesto.getIdPuesto()) {
                puestos.set(i, puesto);
                return puesto;
            }
        }
        throw new IllegalArgumentException("Puesto con ID " + puesto.getIdPuesto() + " no encontrado.");
    }
}
