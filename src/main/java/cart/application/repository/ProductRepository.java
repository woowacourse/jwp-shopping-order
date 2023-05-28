package cart.application.repository;

import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository {

    List<Product> findAll();

    Product findById(Long productId);

    Long insert(Product product);

    void update(Product product);

    void delete(Long productId);
}
