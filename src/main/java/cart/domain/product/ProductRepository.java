package cart.domain.product;

import cart.domain.product.dto.ProductWithId;
import java.util.List;

public interface ProductRepository {

    List<ProductWithId> getAllProducts();

    Product getProductById(final Long id);

    Long save(final Product product);

    void updateProduct(final Long id, final Product product);

    void deleteProduct(final Long id);

    boolean existById(final Long id);
}
