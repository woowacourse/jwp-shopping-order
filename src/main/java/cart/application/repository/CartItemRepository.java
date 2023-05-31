package cart.application.repository;

import cart.domain.cartitem.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    Long createCartItem(CartItem cartItem);

    List<CartItem> findAllCartItemsByMemberId(Long memberId);

    Optional<CartItem> findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);

}
