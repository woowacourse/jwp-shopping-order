package cart.domain.repository;

import cart.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Optional<Product> getProductById(long productId);

    Optional<List<Product>> getAllProducts();

    long createProduct(Product product);

    void updateProduct(long productId, Product product);

    void deleteProduct(long productId);
}
