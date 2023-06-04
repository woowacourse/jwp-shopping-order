package cart.domain.repository;

import cart.domain.Product;

import java.util.List;

public interface ProductRepository {
    Product findById(Long productId);

    List<Product> findAll();

    Long save(Product product);

    void update(Long productId, Product product);

    void deleteById(Long productId);
}
