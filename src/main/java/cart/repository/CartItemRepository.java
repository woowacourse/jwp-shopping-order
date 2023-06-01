package cart.repository;

import cart.domain.CartItem;
import java.util.Optional;

public interface CartItemRepository {
    Optional<CartItem> findById(Long id);
}
