package cart.application.repository.cartItem;

import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;

import java.util.Optional;

public interface CartItemRepository {
    Long createCartItem(CartItem cartItem);

    CartItems findAllCartItemsByMemberId(Long memberId);

    Optional<CartItem> findById(Long id);

    default CartItem getById(Long id) {
        return findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일치하는 상품이 없습니다."));
    }

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);

}
