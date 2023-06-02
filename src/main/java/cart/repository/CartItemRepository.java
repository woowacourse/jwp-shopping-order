package cart.repository;

import cart.domain.CartItem;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    Long create(final CartItem cartItem);

    CartItem findById(final Long id);

    Optional<CartItem> findByMemberIdAndProductId(final Long memberId, final Long productId);

    List<CartItem> findByMemberId(final Long memberId);

    List<CartItem> findAllByIds(final List<Long> ids);

    void updateQuantity(final CartItem cartItem);

    void deleteById(final Long id);

    void deleteAll(final List<CartItem> cartItems);
}
