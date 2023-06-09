package cart.domain.respository.product;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    List<Product> getAllProducts();

    Optional<Product> getProductById(Long productId);

    Product createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);

}
