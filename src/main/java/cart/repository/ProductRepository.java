package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.product.Product;
import cart.exception.ProductException.NotFound;
import cart.repository.mapper.ProductMapper;
import java.util.List;
import java.util.stream.Collectors;
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
                .map(ProductMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Product getProductById(Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId)
                .orElseThrow(() -> new NotFound());
        return ProductMapper.toDomain(productEntity);
    }

    public Long createProduct(Product product) {
        ProductEntity productEntity = ProductMapper.toEntity(product);
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(Product product) {
        ProductEntity productEntity = ProductMapper.toEntity(product);
        productDao.updateProduct(productEntity);
    }

    public void deleteProduct(Long productId) {
        productDao.deleteProduct(productId);
    }
}
