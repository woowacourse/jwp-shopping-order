package cart.repository;

import cart.domain.CartItem;
import java.util.List;

public interface CartItemRepository {

    CartItem findById(final long id);

    List<CartItem> findByMemberId(final long id);

    CartItem save(final CartItem cartItem);

    void deleteById(final long id);

    void updateQuantity(final CartItem cartItem);
}
