package cart.domain.repository;

import cart.domain.cart.CartItem;

import java.util.List;

public interface CartRepository {
    Long save(CartItem cartItem);

    List<CartItem> findAllByMemberId(Long memberId);

    CartItem findById(Long id);

    void deleteById(Long id);

    void update(CartItem cartItem);

    void deleteByMemberIdAndCartItemIds(Long memberId, List<Long> ids);
}
