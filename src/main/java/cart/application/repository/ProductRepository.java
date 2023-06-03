package cart.application.repository;

import cart.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Long createProduct(Product product);

    List<Product> findAll();

    Optional<Product> findById(Long productId);

    void updateProduct(Product product);

    void deleteProduct(Long productId);

}
