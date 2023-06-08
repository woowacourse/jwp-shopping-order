package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.exception.notfound.ProductNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductRepository(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public Long save(final Product product) {
        final Optional<Product> productOptional = productDao.findById(product.getId());
        if (productOptional.isPresent()) {
            return productOptional.get().getId();
        }
        return productDao.insert(product);
    }

    public Product findById(final Long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void update(final Product product) {
        productDao.update(product);
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteByProductId(id);
        productDao.deleteById(id);
    }
}
