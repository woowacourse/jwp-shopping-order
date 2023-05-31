package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
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


    public List<Product> findAll() {
        List<ProductEntity> productEntities = productDao.findAll();
        return productEntities.stream()
            .map(ProductEntity::toDomain)
            .collect(Collectors.toList());
    }

    public Product findById(Long productId) {
        ProductEntity productEntity = productDao.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품 id 입니다."));
        return productEntity.toDomain();
    }

    public long save(Product product) {
        return productDao.save(ProductEntity.fromDomain(product));
    }


    public void updateProduct(Long productId, Product product) {
        validateProductExistence(productId);
        productDao.updateProduct(productId, ProductEntity.fromDomain(product));
    }

    public void deleteById(long productId) {
        validateProductExistence(productId);
        productDao.deleteById(productId);
    }

    private void validateProductExistence(long productId) {
        if (productDao.isExistingId(productId)) {
            throw new IllegalArgumentException("존재하지 않는 상품 id 입니다.");
        }
    }
}
