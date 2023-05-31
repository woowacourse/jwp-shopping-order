package cart.repository;

import cart.dao.CartItemDao;
import cart.dao.ProductDao;
import cart.domain.product.Product;
import cart.exception.notfound.ProductNotFoundException;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;

    public ProductRepository(final ProductDao productDao, final CartItemDao cartItemDao) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
    }

    public void deleteProduct(final Product product) {
        cartItemDao.deleteByProductId(product.getId());
        productDao.delete(product.getId());
    }

    public Product findById(final Long id) {
        return productDao.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
