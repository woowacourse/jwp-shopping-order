package shop.domain.repository;

import shop.domain.product.Product;

import java.util.List;


public interface ProductRepository {
    Long save(Product product);

    List<Product> findAll();

    Product findById(Long id);

    void update(Product product);

    void deleteById(Long id);
}
