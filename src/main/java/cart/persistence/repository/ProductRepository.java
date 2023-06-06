package cart.persistence.repository;

import cart.domain.product.Product;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.productEntityMapper;
import static cart.persistence.repository.Mapper.productMapper;

@Component
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAllProducts() {
        final List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(Mapper::productMapper)
                .collect(Collectors.toList());
    }

    public Product findProductById(final Long productId) {
        final ProductEntity productEntity = productDao.findById(productId);
        return productMapper(productEntity);
    }

    public Long createProduct(final Product product) {
        final ProductEntity productEntity = productEntityMapper(product);
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(final Product product) {
        final ProductEntity productEntity = productEntityMapper(product);
        productDao.updateProduct(productEntity);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
