package cart.domain.repository;

import cart.domain.CartItem;

public interface CartItemRepository {

    CartItem findCartItemById(Long id);

    void deleteCartItemById(Long id);
}
