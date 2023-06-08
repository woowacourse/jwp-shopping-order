package cart.repository;

import cart.domain.product.Product;
import cart.db.dao.ProductDao;
import cart.db.entity.ProductEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        return productDao.getAllProducts().stream()
                .map(ProductEntity::toDomain)
                .collect(Collectors.toList());
    }

    public Product getProductById(Long productId) {
        return productDao.getProductById(productId)
                .toDomain();
    }

    public Long createProduct(Product product) {
        return productDao.createProduct(ProductEntity.of(product));
    }

    public void updateProduct(Long productId, Product product) {
        productDao.updateProduct(productId, ProductEntity.of(product));
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
