package cart.domain.cartitem;

import cart.domain.cartitem.dto.CartItemSaveReq;
import cart.domain.cartitem.dto.CartItemWithId;
import java.util.List;

public interface CartItemRepository {

    List<CartItemWithId> findByMemberName(final String memberName);

    Long save(final String memberName, final CartItemSaveReq cartItemSaveReq);

    void deleteById(final Long cartItemId);

    void updateQuantity(final Long cartItemId, final int quantity);

    CartItem findById(final Long cartItemId);

    Long countByIdsAndMemberId(final List<Long> cartItemIds, final String memberName);

    void deleteByIdsAndMemberId(final List<Long> cartItemIds, final String memberName);
}
