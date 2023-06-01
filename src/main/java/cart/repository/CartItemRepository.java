package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.dao.entity.CartItemEntity;
import cart.exception.CartItemException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long saveCartItem(CartItemEntity cartItemEntity) {
        return cartItemDao.insertCartItem(cartItemEntity);
    }

    public CartItem findByCartItemId(Long cartItemId) {
        return cartItemDao.findByCartItemId(cartItemId)
                .orElseThrow(CartItemException.CartItemNotExists::new);
    }

    public CartItems findByCartItemIds(List<Long> cartItemIds) {
        List<CartItem> findCartItems = cartItemDao.findByCartItemIds(cartItemIds);
        return CartItems.from(findCartItems);
    }

    public CartItems findByMemberId(Long memberId) {
        List<CartItem> findCartItems = cartItemDao.findByMemberId(memberId);
        return CartItems.from(findCartItems);
    }

    public void updateQuantity(Long cartItemId, int quantity) {
        cartItemDao.updateQuantity(cartItemId, quantity);
    }

    public void deleteByCartItemId(Long cartItemId) {
        cartItemDao.deleteByCartItemId(cartItemId);
    }
}
