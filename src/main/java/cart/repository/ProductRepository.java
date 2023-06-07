package cart.repository;

import cart.domain.Product;
import cart.entity.ProductEntity;

import java.util.List;
import java.util.stream.Collectors;

public interface ProductRepository {

    List<Product> getAllProducts();

    Product getProductById(Long productId);

    List<Product> findAllByIds(List<Long> productIds);

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);
}
