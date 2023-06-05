package cart.domain.cartItem;

import java.util.List;

public interface CartItemRepository {
    List<CartItem> findCartItemsByMemberId(Long memberId);

    Long add(CartItem cartItem);

    CartItem findById(Long id);

    void delete(Long memberId, Long productId);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);
}
