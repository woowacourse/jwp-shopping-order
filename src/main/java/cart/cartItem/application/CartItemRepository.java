package cart.cartItem.application;

import cart.cartItem.domain.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    Long save(CartItem cartItem);
    Optional<CartItem> findById(Long id);
    List<CartItem> findByMemberId(Long memberId);
    void deleteById(Long id);
    void deleteByIds(List<Long> ids);
    void updateQuantity(CartItem cartItem);
}
