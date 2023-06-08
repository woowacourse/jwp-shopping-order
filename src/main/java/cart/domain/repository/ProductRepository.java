package cart.domain.repository;

import cart.domain.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProductRepository {
    List<Product> findAll();

    Product findById(Long productId);

    Long save(Product product);

    void updateById(Long productId, Product product);

    void deleteById(Long productId);
}
