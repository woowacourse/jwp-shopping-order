package cart.application.repository;

import cart.application.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {

    Long insert(Product product);

    Product findById(Long productId);

    List<Product> findAll();

    void update(Product product);

    void delete(Long productId);
}
