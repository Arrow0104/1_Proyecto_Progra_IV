package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Oferente;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
@Profile({"default", "dev", "test"})
public class InMemoryOferenteRepository implements IOferenteRepository {

    private final List<Oferente> oferentes = new ArrayList<>();
    private final AtomicInteger sequence = new AtomicInteger(0);

    @PostConstruct
    public void init() {
        save(new Oferente(0, "Oferente Demo 1", "San José", null, null));
        save(new Oferente(0, "Oferente Demo 2", "Alajuela", null, null));
    }

    @Override
    public List<Oferente> findAll() {
        return new ArrayList<>(oferentes);
    }

    @Override
    public Optional<Oferente> findById(Integer idOferente) {
        return oferentes.stream().filter(o -> o.getIdOferente() == idOferente).findFirst();
    }

    @Override
    public Oferente save(Oferente oferente) {
        if (oferente.getIdOferente() <= 0) {
            oferente.setIdOferente(sequence.incrementAndGet());
            oferentes.add(oferente);
        } else {
            oferentes.removeIf(o -> o.getIdOferente() == oferente.getIdOferente());
            oferentes.add(oferente);
        }
        return oferente;
    }

    @Override
    public Oferente update(Oferente oferente) {
        for (int i = 0; i < oferentes.size(); i++) {
            if (oferentes.get(i).getIdOferente() == oferente.getIdOferente()) {
                oferentes.set(i, oferente);
                return oferente;
            }
        }
        throw new IllegalArgumentException("Oferente con ID " + oferente.getIdOferente() + " no encontrado.");
    }
}