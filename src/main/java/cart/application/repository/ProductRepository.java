package cart.application.repository;

import cart.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {

    Product insert(Product product);

    Optional<Product> findById(Long productId);

    List<Product> findAll();

    void update(Product product);

    void delete(Long productId);
}
