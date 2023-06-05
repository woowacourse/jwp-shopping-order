package cart.repository;

import cart.dao.ProductDao;
import cart.domain.Product;
import cart.exception.ProductException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;

    public ProductRepository(ProductDao productDao) {
        this.productDao = productDao;
    }

    public Long save(Product product) {
        return productDao.save(product);
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public Optional<Product> findById(Long productId) {
        return productDao.findById(productId);
    }

    public void update(Long productId, Product product) {
        if (!productDao.isExistBy(productId)) {
            throw new ProductException.NotFound(productId);
        }

        productDao.update(productId, product);
    }

    public void deleteById(Long productId) {
        productDao.deleteById(productId);
    }
}
