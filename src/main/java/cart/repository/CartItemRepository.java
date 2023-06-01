package cart.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import cart.repository.dto.CartItemWithProductDto;

import java.util.List;

public interface CartItemRepository {

    List<CartItemWithProductDto> findByIds(final List<Long> ids);
    List<CartItem> findByMember(final Member member);
    long save(final Member member, final long productId, final int quantity);
    void updateQuantity(final long id, final int quantity);
    void delete(final long id);
    long findMemberIdById(final long id);
    void deleteAll(final List<Long> id);
}
