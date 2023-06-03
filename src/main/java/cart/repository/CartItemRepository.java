package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import java.util.List;

public interface CartItemRepository {

    Long save(final CartItem cartItem);

    CartItem findById(final Long id);

    List<CartItem> findMembersItemByCartIds(final Member member, final List<Long> ids);

    List<CartItem> findByMemberId(final Long memberId);

    void update(final CartItem cartItem);

    void deleteById(final Long id);
}
