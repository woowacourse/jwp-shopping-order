package cart.repository;

import cart.dao.ProductDao;
import cart.dao.entity.ProductEntity;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findAll() {
        return Product.from(productDao.getAllProducts());
    }

    public Optional<Product> findById(final Long productId) {
        final Optional<ProductEntity> productEntity = productDao.findById(productId);
        if (productEntity.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(Product.from(productEntity.get()));
    }

    public Long add(final ProductEntity product) {
        return productDao.save(product);
    }

    public void update(final ProductEntity product) {
        productDao.updateProduct(product);
    }

    public void remove(final Long id) {
        productDao.deleteProduct(id);
    }
}
