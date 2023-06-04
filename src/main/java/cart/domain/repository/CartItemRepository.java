package cart.domain.repository;

import cart.domain.CartItem;

import java.util.List;

public interface CartItemRepository {

    CartItem findCartItemById(Long id);

    void deleteCartItemById(Long id);

    List<CartItem> findByMemberId(Long id);

    CartItem findByMemberIdAndProductId(Long id, Long productId);

    Long save(CartItem cartItem);

    void updateQuantity(CartItem cartItem);
}
