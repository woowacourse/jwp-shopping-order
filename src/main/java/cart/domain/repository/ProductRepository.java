package cart.domain.repository;

import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {
    List<Product> findByIds(List<Long> productIds);

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);
}
