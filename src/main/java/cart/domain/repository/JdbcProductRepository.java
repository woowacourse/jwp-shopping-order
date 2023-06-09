package cart.domain.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcProductRepository implements ProductRepository {
    private final ProductDao productDao;

    public JdbcProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public Product findById(final Long productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public List<Product> findAll() {
        return productDao.getAllProducts();
    }

    @Override
    public Long save(final Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public void update(final Long productId, final Product product) {
        productDao.updateProduct(productId, product);
    }

    @Override
    public void deleteById(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
