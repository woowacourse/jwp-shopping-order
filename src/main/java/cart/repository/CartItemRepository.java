package cart.repository;

import cart.dao.dto.PageInfo;
import cart.domain.CartItem;
import cart.domain.CartItems;
import cart.domain.Member;

public interface CartItemRepository {
    CartItems findByMember(Member member);

    Long add(Member member, Long productId);

    CartItem findById(final Long id);

    void updateQuantity(CartItem cartItem);

    void deleteById(Long id);

    CartItems findCartItemsByPage(Member member, int size, int page);

    PageInfo findPageInfo(Member member, int size, int page);
}
