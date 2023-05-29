package cart.application.repository;

import cart.domain.Product;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    long create(Product product);

    List<Product> findAll();

    Optional<Product> findById(long id);

    void update(Product product);

    void deleteById(long id);
}
