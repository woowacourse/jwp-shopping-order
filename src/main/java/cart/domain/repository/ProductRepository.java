package cart.domain.repository;

import cart.domain.product.Product;

import java.util.List;

public interface ProductRepository {

    Product getProductById(long productId);

    List<Product> getAllProducts();

    long createProduct(Product product);

    void updateProduct(long productId, Product product);

    void deleteProduct(long productId);
}
