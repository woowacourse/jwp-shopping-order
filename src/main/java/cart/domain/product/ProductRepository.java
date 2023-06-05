package cart.domain.product;

import java.util.List;

public interface ProductRepository {
    List<Product> findAll();

    Product findById(Long id);

    Long add(Product product);

    void update(Long productId, Product product);

    void delete(Long productId);
}
