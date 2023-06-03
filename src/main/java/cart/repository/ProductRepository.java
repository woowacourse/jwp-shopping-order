package cart.repository;

import cart.domain.Product;
import java.util.List;

public interface ProductRepository {

    Long save(final Product product);

    Product findById(final Long id);

    List<Product> findAll();

    void update(final Product product);

    void deleteById(final Long id);
}
