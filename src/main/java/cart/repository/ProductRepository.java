package cart.repository;

import cart.domain.product.Product;
import cart.persistance.dao.ProductDao;
import cart.persistance.entity.ProductEntity;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
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
