package cart.domain.repository;

import cart.domain.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    List<CartItem> findAllByMemberId(Long id);

    CartItem save(CartItem cartItem);

    Optional<CartItem> findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);
}
