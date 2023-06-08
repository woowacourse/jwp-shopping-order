package cart.dao;

import cart.domain.CartItem;

import java.util.List;

public interface CartItemRepository {

    List<CartItem> findByIds(final List<Long> ids);

    void batchDelete(final Long memberId, final List<CartItem> removalCartItems);
}
