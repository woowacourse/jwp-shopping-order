package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.domain.Product;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {
    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Product findProductOf(final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        return cartItem.getProduct();
    }

    public int findQuantityOf(final Long id) {
        final CartItem cartItem = cartItemDao.findById(id);
        return cartItem.getQuantity();
    }
}
