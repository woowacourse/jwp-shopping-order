package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDbRepository implements ProductRepository {
    private final ProductDao productDao;

    public ProductDbRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public List<Product> findAll() {
        return productDao.getAllProducts();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        try {
            return Optional.of(productDao.getProductById(productId));
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }

    @Override
    public Long create(Product product) {
        return productDao.createProduct(product);
    }

    @Override
    public void update(Product product) {
        productDao.updateProduct(product.getId(), product);
    }

    @Override
    public void deleteById(Long productId) {
        productDao.deleteProduct(productId);
    }
}
