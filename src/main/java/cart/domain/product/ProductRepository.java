package cart.domain.product;

import cart.domain.product.dto.ProductWithId;
import java.util.List;

public interface ProductRepository {

    List<ProductWithId> getAllProducts();

    Product getProductById(final Long productId);

    Long save(final Product product);

    void updateProduct(final Long productId, final Product product);

    void deleteProduct(final Long productId);
}
