package cart.application.repository;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;

import java.util.Optional;

public interface CartItemRepository {
    Long createCartItem(CartItem cartItem);

    CartItems findAllCartItemsByMemberId(Long memberId);

    Optional<CartItem> findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);

}
