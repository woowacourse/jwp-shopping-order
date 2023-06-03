package cart.domain.respository.cartitem;

import cart.domain.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    CartItem save(final CartItem cartItem);

    Optional<CartItem> findById(final Long id);

    List<CartItem> findByMemberId(final Long memberId);

    void updateQuantity(final CartItem cartItem);

    void delete(final Long memberId, final Long productId);

    void deleteById(final Long id);

    void deleteByMemberIdAndProductIds(final Long memberId, final List<Long> productIds);
}
