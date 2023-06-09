package cart.domain.product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> findProductById(Long productId);

    List<Product> findAllProducts();

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void updateStock(Long productId, Long updateStock);

    void deleteProduct(Long productId);
}
