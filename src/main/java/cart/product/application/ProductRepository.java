package cart.product.application;

import cart.product.Product;

import java.util.List;

public interface ProductRepository {
    List<Product> getAllProducts();

    Product getProductById(Long productId);

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);
}
