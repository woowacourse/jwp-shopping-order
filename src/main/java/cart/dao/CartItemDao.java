package cart.dao;

import cart.domain.CartItem;

import java.util.List;
import java.util.Optional;

public interface CartItemDao {

    Optional<CartItem> findCartItemById(Long id);

    Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId);

    List<CartItem> findAllCartItemsByMemberId(Long memberId);

    Long save(CartItem cartItem);

    void updateQuantity(CartItem cartItem);

    void deleteById(Long id);

    void delete(Long memberId, Long productId);
}
