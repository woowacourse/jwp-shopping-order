package cart.domain.repository;

import cart.domain.Product;
import java.util.List;

public interface ProductRepository {

    Long create(final Product product);

    Product findById(final Long id);

    List<Product> findAll();

    void update(final Long id, final Product product);

    void deleteById(final Long id);
}
