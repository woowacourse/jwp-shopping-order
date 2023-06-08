package cart.application.repository.product;

import cart.domain.product.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public interface ProductRepository {

    Long createProduct(final Product product);

    List<Product> findAll();

    Optional<Product> findById(final Long productId);
    default Product getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NoSuchElementException("일치하는 상품이 없습니다."));
    }

    void updateProduct(final Product product);

    void deleteProduct(final Long productId);

}
