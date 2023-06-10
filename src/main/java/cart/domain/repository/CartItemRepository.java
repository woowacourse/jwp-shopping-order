package cart.domain.repository;

import cart.domain.carts.CartItem;

import java.util.List;

public interface CartItemRepository {

    void deleteById(long cartItemId);

    List<CartItem> findCartItemsByIds(List<Long> cartIds);

    CartItem findCartItemById(long cartId);

    List<CartItem> findCartItemByMemberId(long memberId);

    long save(CartItem cartItem);

    void updateQuantity(CartItem cartItem);
}
