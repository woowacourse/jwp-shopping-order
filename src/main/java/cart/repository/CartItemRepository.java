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

    private static final int QUANTITY_EMPTY_VALUE = 0;

    private final CartItemDao cartItemDao;

    public CartItemRepository(CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public Long saveCartItem(CartItemEntity cartItemEntity) {
        return cartItemDao.insertCartItem(cartItemEntity);
    }

    public CartItem findByCartItemId(Long cartItemId) {
        return cartItemDao.findByCartItemId(cartItemId)
                .orElseThrow(CartItemException.NotFound::new);
    }

    public CartItems findByCartItemIds(List<Long> cartItemIds) {
        List<CartItem> findCartItems = cartItemDao.findByCartItemIds(cartItemIds);
        return CartItems.from(findCartItems);
    }

    public CartItems findByMemberId(Long memberId) {
        List<CartItem> findCartItems = cartItemDao.findByMemberId(memberId);
        return CartItems.from(findCartItems);
    }

    public void updateQuantityOrDelete(CartItemEntity cartItemEntity) {
        if (cartItemEntity.getQuantity() == QUANTITY_EMPTY_VALUE) {
            cartItemDao.deleteByCartItemId(cartItemEntity.getId());
            return;
        }
        cartItemDao.updateQuantity(cartItemEntity.getId(), cartItemEntity.getQuantity());
    }

    public void deleteByCartItemIds(List<Long> cartItemIds) {
        cartItemDao.deleteByCateItemIds(cartItemIds);
    }

    public void deleteByCartItemId(Long cartItemId) {
        cartItemDao.deleteByCartItemId(cartItemId);
    }
}
