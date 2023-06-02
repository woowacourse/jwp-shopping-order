package cart.domain.cartitem;

import cart.domain.cartitem.dto.CartItemSaveReq;
import java.util.List;

public interface CartRepository {

    Cart findByMemberName(final String memberName);

    Long save(final String memberName, final CartItemSaveReq cartItemSaveReq);

    void deleteById(final Long cartItemId);

    void updateQuantity(final Long cartItemId, final int quantity);

    Cart findById(final Long cartItemId);

    Long countByCartItemIdsAndMemberId(final List<Long> cartItemIds, final String memberName);

    void deleteByCartItemIdsAndMemberId(final List<Long> cartItemIds, final String memberName);

    boolean existByMemberNameAndProductId(final String memberName, final Long productId);

    void deleteByProductIdsAndMemberName(final List<Long> productIds, final String memberName);
}
