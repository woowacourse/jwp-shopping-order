package cart.repository;

import cart.domain.Product;
import cart.entity.ProductEntity;
import cart.repository.dao.ProductDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    public Product findById(Long productId) {
        final ProductEntity productEntity = productDao.getProductById(productId);
        return Product.from(productEntity);
    }

    public Long save(Product product) {
        final ProductEntity productEntity = ProductEntity.from(product);
        return productDao.createProduct(productEntity);
    }

    public void update(Long productId, Product product) {
        final ProductEntity productEntity = ProductEntity.from(product);
        productDao.updateProduct(productId, productEntity);
    }

    public void deleteById(Long productId) {
        productDao.deleteProduct(productId);
    }

    public Page<Product> findAll(Pageable pageable) {
        final Page<ProductEntity> productEntities = productDao.getProducts(pageable);

        return productEntities.map(productEntity -> Product.from(productEntity));
    }
}
