package cart.domain.cart;

import java.util.List;

public interface CartItemRepository {

    CartItems findAllByCartItemIds(final List<Long> itemIds);

    void deleteAllByIds(List<Long> cartItemIds);
}
