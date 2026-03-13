package cr.ac.una.job.services;

import cr.ac.una.job.config.AppProperties;
import cr.ac.una.job.dtos.product.CreateProductRequest;
import cr.ac.una.job.dtos.product.ProductResponse;
import cr.ac.una.job.dtos.product.UpdateProductRequest;
import cr.ac.una.job.exceptions.ProductNotFoundException;
import cr.ac.una.job.models.Product;
import cr.ac.una.job.repositories.IProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final IProductRepository repository;
    private final AppProperties appProperties;

    public ProductService(IProductRepository repository, AppProperties appProperties) {
        this.repository = repository;
        this.appProperties = appProperties;
    }

    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all products from the database");

        return repository.findAllActive().stream().map(this::toResponse).toList();
    }

    public ProductResponse getProductById(Long id) {
        log.info("Fetching product with id {} from the database", id);

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(product);
    }

    public Product getDomainProductById(Long id) {
        log.info("Fetching product with id {} from the database", id);

        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating new product from the database");

        Product product = new Product(null, request.getName().trim(), request.getPrice(), true, LocalDateTime.now());

        Product saved = repository.save(product);

        return toResponse(saved);
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with id {} in the database", id);

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(request.getName().trim());
        product.setPrice(request.getPrice());

        Product updated = repository.update(product);

        return toResponse(updated);
    }

    public void deleteLogical(Long id) {
        log.info("Logically deleting product with id {} in the database", id);

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setActive(false);

        repository.update(product);
    }

    public UpdateProductRequest buildUpdateRequest(Long id) {
        log.info("Building update request for product with id {} from the database", id);

        Product product = repository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return new UpdateProductRequest(product.getName(), product.getPrice());
    }

    private ProductResponse toResponse(Product product) {
        double finalPrice = product.getPrice() + (product.getPrice() * appProperties.getTaxRate());

        return new ProductResponse(product.getId(), product.getName(), product.getPrice(), finalPrice, appProperties.getDefaultCurrency(), product.isActive(), product.getCreatedAt());
    }
}
