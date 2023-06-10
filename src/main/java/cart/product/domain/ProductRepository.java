package cart.product.domain;

import java.util.List;

public interface ProductRepository {

    Long save(Product product);

    void update(Product product);

    void delete(Long productId);

    Product findById(Long id);

    List<Product> findAll();
}
