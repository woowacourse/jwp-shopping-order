package cart.cartitem.application;

import cart.cartitem.CartItem;

import java.util.List;

public interface CartItemRepository {
    List<CartItem> findAllByMemberId(Long memberId);

    Long save(CartItem cartItem);

    CartItem findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);
}
