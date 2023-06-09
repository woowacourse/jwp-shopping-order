package cart.repository;

import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.entity.ProductEntity;
import cart.exception.ProductException;
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
                .map(ProductMapper::toProduct)
                .collect(Collectors.toUnmodifiableList());
    }

    public Product getProductById(final Long productId) {
        ProductEntity productEntity = productDao.getProductById(productId)
                .orElseThrow(() -> new ProductException.NotFoundProduct(productId));

        return ProductMapper.toProduct(productEntity);
    }

    public Long createProduct(final Product product) {
        return productDao.createProduct(ProductMapper.toEntity(product));
    }


    public void updateProduct(final Long productId, final Product product) {
        try {
            productDao.updateProduct(productId, ProductMapper.toEntity(product));
        } catch (IllegalArgumentException e) {
            throw new ProductException.NotFoundProduct(productId);
        }
    }

    public void deleteProduct(Long productId) {
        try {
            productDao.deleteProduct(productId);
        } catch (IllegalArgumentException e) {
            throw new ProductException.NotFoundProduct(productId);
        }
    }
}
