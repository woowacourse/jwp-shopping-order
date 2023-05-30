package cart.repository;

import static java.util.stream.Collectors.toList;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.entity.ProductEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(ProductEntity::toDomain)
                .collect(toList());
    }

    public Optional<Product> getProductById(Long productId) {
        return productDao.findById(productId)
                .map(ProductEntity::toDomain);
    }

    public Long createProduct(Product product) {
        ProductEntity productEntity = toEntity(product);
        return productDao.save(productEntity);
    }

    public void updateProduct(Long productId, Product product) {
        ProductEntity productEntity = toEntity(product);
        productDao.updateProduct(productId, productEntity);
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getName(),
                product.getPrice(),
                product.getImageUrl()
        );
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
