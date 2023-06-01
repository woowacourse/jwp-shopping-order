package cart.domain.repository;

import cart.domain.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    Optional<CartItem> findById(Long id);

    List<CartItem> findAllByMemberId(Long id);

    Long create(CartItem cartItem);

    void updateQuantity(CartItem cartItem);

    void deleteById(Long id);
}
