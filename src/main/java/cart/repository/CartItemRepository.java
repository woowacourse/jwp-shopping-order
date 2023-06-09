package cart.repository;

import cart.dao.CartItemDao;
import cart.domain.CartItem;
import cart.exception.CartNotFoundException;
import cart.exception.DeleteFailException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;

@Repository
public class CartItemRepository {

    private final CartItemDao cartItemDao;

    public CartItemRepository(final CartItemDao cartItemDao) {
        this.cartItemDao = cartItemDao;
    }

    public List<CartItem> findCartItemsByIds(List<Long> cartItemIds) {
        return cartItemIds.stream()
                .map(this::findCartItemById)
                .collect(Collectors.toList());
    }

    private CartItem findCartItemById(Long id) {
        CartItem cartItem = cartItemDao.findById(id);

        if (cartItem == null) {
            throw new CartNotFoundException("찾는 장바구니가 없습니다.");
        }

        return cartItem;
    }

    public void deleteCartItems(List<Long> cartItemIds) {
        for (Long cartItemId : cartItemIds) {
            deleteCartItem(cartItemId);
        }
    }

    private void deleteCartItem(final Long cartItemId) {
        int deleteCount = cartItemDao.deleteById(cartItemId);

        if (deleteCount == 0) {
            throw new DeleteFailException("장바구니가 삭제되지 않았습니다.");
        }
    }

}

