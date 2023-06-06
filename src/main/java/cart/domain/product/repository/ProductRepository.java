package cart.domain.product.repository;

import cart.domain.product.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> findAll();

    Optional<Product> findById(Long productId);

    Product save(Product product);

    void update(Product product);

    void deleteProduct(Long productId);
}
