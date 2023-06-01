package cart.domain.repository;

import cart.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long productId);

    List<Product> findAll();

    Long create(Product product);

    void update(Product product);

    void deleteProduct(Long productId);
}
