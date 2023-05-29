package cart.dao;

import cart.domain.Product;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    Optional<Product> findProductById(Long productId);

    List<Product> findAllProducts();

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);
}
