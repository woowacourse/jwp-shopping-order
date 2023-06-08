package cart.domain.cartitem;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    Optional<CartItem> findCartItemById(Long id);

    Optional<CartItem> findCartItemByMemberIdAndProductId(Long memberId, Long productId);

    List<CartItem> findAllCartItemsByMemberId(Long memberId);

    List<Long> findAllCartIdsByMemberId(Long memberId);

    Long save(CartItem cartItem);

    void updateQuantity(CartItem cartItem);

    void deleteById(Long id);

    void delete(Long memberId, Long productId);

    void deleteAllIdIn(List<Long> ids);
}
