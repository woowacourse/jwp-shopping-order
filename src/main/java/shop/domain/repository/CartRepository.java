package shop.domain.repository;

import shop.domain.cart.CartItem;

import java.util.List;

public interface CartRepository {
    Long save(CartItem cartItem);

    List<CartItem> findAllByMemberId(Long memberId);

    CartItem findById(Long id);

    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    void deleteByMemberIdAndProductIds(Long memberId, List<Long> productIds);

    void update(CartItem cartItem);
}
