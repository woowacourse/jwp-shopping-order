package cart.persistence.repository;

import cart.domain.product.Product;
import cart.persistence.dao.ProductDao;
import cart.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static cart.persistence.repository.Mapper.productEntityToProductMapper;
import static cart.persistence.repository.Mapper.productToProductEntityMapper;

@Component
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> getAllProducts() {
        final List<ProductEntity> productEntities = productDao.getAllProducts();
        return productEntities.stream()
                .map(Mapper::productEntityToProductMapper)
                .collect(Collectors.toList());
    }

    public Product getProductById(final Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId);
        return productEntityToProductMapper(productEntity);
    }

    public Long createProduct(final Product product) {
        final ProductEntity productEntity = productToProductEntityMapper(product);
        return productDao.createProduct(productEntity);
    }

    public void updateProduct(final Product product) {
        final ProductEntity productEntity = productToProductEntityMapper(product);
        productDao.updateProduct(productEntity);
    }

    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
