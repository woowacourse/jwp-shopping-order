package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.exception.notfound.ProductNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductRepository(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public Long save(final Product product) {
        if (product.getId() == null || productDao.findById(product.getId()).isEmpty()) {
            return productDao.insert(product);
        }
        productDao.update(product);
        return product.getId();
    }

    public Product findById(final Long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> findAll() {
        return productDao.findAll();
    }

    public void deleteById(final Long id) {
        cartItemDao.deleteByProductId(id);
        productDao.deleteById(id);
    }
}
