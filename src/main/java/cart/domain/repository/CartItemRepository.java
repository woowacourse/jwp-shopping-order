package cart.domain.repository;

import cart.domain.carts.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    void deleteById(long cartItemId);

    Optional<List<CartItem>> findCartItemsByIds(List<Long> cartIds);

    Optional<CartItem> findCartItemById(long cartId);

    Optional<List<CartItem>> findCartItemByMemberId(long memberId);

    long save(CartItem cartItem);

    void updateQuantity(CartItem cartItem);
}
