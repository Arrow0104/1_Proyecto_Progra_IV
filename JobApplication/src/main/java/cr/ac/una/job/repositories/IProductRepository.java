package cr.ac.una.job.repositories;

import cr.ac.una.job.models.Product;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {
    List<Product> findAll();

    List<Product> findAllActive();

    Optional<Product> findById(Long id);

    List<Product> findByNameContaining(String name);

    Product save(Product product);

    Product update(Product product);
}
