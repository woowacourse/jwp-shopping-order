package cart.repository;

import cart.domain.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();
    Product findById(final long id);
    List<Product> findByIds(final List<Long> ids);
    long save(final Product product);
    void update(final Product product);
    void delete(final long id);
}
