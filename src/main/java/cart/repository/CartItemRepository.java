package cart.repository;

import cart.domain.carts.CartItem;
import cart.repository.dao.CartItemDao;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public CartItem findCartItemById(long cartId) {
        return cartItemDao.findById(cartId);
    }

    public void deleteById(long cartItemId) {
        cartItemDao.deleteById(cartItemId);
    }
}
