package cart.repository;

import cart.domain.carts.CartItem;
import cart.repository.dao.CartItemDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

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

    public List<CartItem> findCartItemsByIds(List<Long> cartIds) {
        return cartIds.stream()
                .map(this::findCartItemById)
                .collect(Collectors.toList());
    }
}
