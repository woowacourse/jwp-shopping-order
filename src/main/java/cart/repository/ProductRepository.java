package cart.repository;

import cart.dao.dto.PageInfo;
import cart.domain.Product;
import java.util.List;

public interface ProductRepository {
    Product findProductById(Long productId);

    Long createProduct(Product product);

    void updateProduct(Long productId, Product product);

    void deleteProduct(Long productId);

    List<Product> findProductsByPage(int size, int page);

    PageInfo findPageInfo(int size, int page);

    List<Product> getAllProducts();
}
