package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.cartItem.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public void deleteAll(List<CartItem> cartItems) {
        List<Long> cartItemIds = cartItems.stream().map(CartItem::getId).collect(Collectors.toList());
        cartItemDao.deleteAll(cartItemIds);
    }

    public List<CartItem> findByMemberId(Long memberId) {
        return cartItemDao.findByMemberId(memberId);
    }

    public List<CartItem> findByIds(final List<Long> orderItemIds) {
        return cartItemDao.findByIds(orderItemIds);
    }
}
