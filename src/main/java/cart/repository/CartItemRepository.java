package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.cartitem.CartItem;
import cart.exception.notfound.CartItemNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long save(final CartItem cartItem) {
        if (cartItem.getId() == null || cartItemDao.findById(cartItem.getId()).isEmpty()) {
            return cartItemDao.insert(cartItem);
        }
        cartItemDao.update(cartItem);
        return cartItem.getId();
    }

    public List<CartItem> findAllByMemberId(final Long id) {
        return cartItemDao.findAllByMemberId(id);
    }

    public CartItem findById(final Long id) {
        return cartItemDao.findById(id)
                .orElseThrow(() -> new CartItemNotFoundException(id));
    }

    public void delete(final CartItem cartItem) {
        cartItemDao.delete(cartItem.getId());
    }
}
