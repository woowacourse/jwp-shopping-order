package cart.domain.respository.product;

import cart.dao.ProductDao;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class DbProductRepository implements ProductRepository {

    private ProductDao productDao;

    public DbProductRepository(final ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public Optional<Product> getProductById(final Long productId) {
        return productDao.getProductById(productId);
    }

    @Override
    public Product createProduct(final Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public void updateProduct(final Long productId, final Product product) {
        productDao.updateProduct(productId, product);
    }

    @Override
    public void deleteProduct(final Long productId) {
        productDao.deleteProduct(productId);
    }
}
