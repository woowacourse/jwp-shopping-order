package cart.application.repository.product;

import cart.domain.product.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Long createProduct(final Product product);

    List<Product> findAll();

    Optional<Product> findById(final Long productId);

    void updateProduct(final Product product);

    void deleteProduct(final Long productId);

}
