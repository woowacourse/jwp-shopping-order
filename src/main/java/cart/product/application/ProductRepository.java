package cart.product.application;

import cart.product.domain.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> getAllProducts();
    Optional<Product> findById(Long id);
    Long createProduct(Product product);
    void updateProduct(Long productId, Product product);
    void updateStocks(List<Product> products);
    void deleteProduct(Long productId);
}
