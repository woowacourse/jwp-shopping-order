package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long insertProduct(Product product) {
        return productDao.createProduct(product);
    }

    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, product);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }

    public Product getProductById(Long productId) {
        return productDao.getProductById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다."));
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        return productIds.stream()
                .map(this::getProductById)
                .collect(Collectors.toList());
    }
}
