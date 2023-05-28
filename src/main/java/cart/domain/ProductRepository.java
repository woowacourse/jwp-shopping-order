package cart.domain;

import java.util.List;

public interface ProductRepository {
    List<Product> findAllProducts();

    Product findProductById(final Long productId);

    Long saveProduct(final Product product);

    void updateProduct(final Long productId, final Product product);

    void deleteProduct(final Long productId);
}
