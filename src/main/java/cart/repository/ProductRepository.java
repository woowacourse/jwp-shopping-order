package cart.repository;

import cart.domain.Product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAll();
    Product findById(final long id);
    long save(final Product product);

    void update(final Product product);
    void delete(final long id);
}