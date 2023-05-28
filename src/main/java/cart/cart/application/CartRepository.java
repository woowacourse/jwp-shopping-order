package cart.cart.application;

import cart.cart.CartItem;

import java.util.List;

public interface CartRepository {
    List<CartItem> findByMemberId(Long memberId);

    Long save(CartItem cartItem);

    CartItem findById(Long id);

    void delete(Long memberId, Long productId);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);
}
