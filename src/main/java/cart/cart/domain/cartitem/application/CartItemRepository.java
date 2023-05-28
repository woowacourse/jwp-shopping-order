package cart.cart.domain.cartitem.application;

import cart.cart.domain.cartitem.CartItem;

import java.util.List;

public interface CartItemRepository {
    List<CartItem> findByMemberId(Long memberId);

    Long save(CartItem cartItem);

    CartItem findById(Long id);

    void delete(Long memberId, Long productId);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);
}
