package cart.domain.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcCartItemRepository implements CartItemRepository{
    private final CartItemDao cartItemDao;

    public JdbcCartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    @Override
    public CartItem findCartItemById(final Long id) {
        return cartItemDao.findById(id);
    }

    @Override
    public void deleteCartItemById(final Long id) {
        cartItemDao.deleteById(id);
    }
}
