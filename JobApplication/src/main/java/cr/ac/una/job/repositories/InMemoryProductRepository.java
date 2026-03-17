package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Product;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile({"default", "dev", "test"})
public class InMemoryProductRepository implements IProductRepository {
    private final List<Product> products = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(0);

    @PostConstruct
    public void init() {
        // Initialize with some sample products
        save(new Product(null, "Laptop", 450000.0, true, LocalDateTime.now()));
        save(new Product(null, "Smartphone", 250000.0, true, LocalDateTime.now()));
        save(new Product(null, "Headphones", 50000.0, true, LocalDateTime.now()));
        save(new Product(null, "Tablet", 30000.0, true, LocalDateTime.now()));
        save(new Product(null, "Mouse", 250000.0, true, LocalDateTime.now()));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    @Override
    public List<Product> findAllActive() {
        return products.stream().filter(Product::isActive).toList();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return products.stream().filter(product -> product.getId().equals(id)).findFirst();
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        String query = name.toLowerCase().trim();

        return products.stream().filter(Product::isActive).filter(product -> product.getName().toLowerCase().contains(query)).toList();
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(sequence.incrementAndGet());
            products.add(product);
        } else {
            products.removeIf(p -> p.getId().equals(product.getId()));
            products.add(product);
        }
        return product;
    }

    @Override
    public Product update(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(product.getId())) {
                products.set(i, product);
                return product;
            }
        }

        throw new IllegalArgumentException("Product with ID " + product.getId() + " not found.");
    }
}
