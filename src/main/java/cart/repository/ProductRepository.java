package cart.repository;

import cart.domain.Product;
import java.util.List;

public interface ProductRepository {

    List<Product> findAll();

    Product findById(final long id);

    Product save(final Product product);

    void updateById(final long id, final Product product);

    void deleteById(final long id);
}
