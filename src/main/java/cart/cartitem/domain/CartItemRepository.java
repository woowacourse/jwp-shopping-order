package cart.cartitem.domain;

import java.util.List;

public interface CartItemRepository {

    Long save(CartItem cartItem);

    void update(CartItem cartItem);

    CartItem findById(Long id);

    List<CartItem> findByMemberId(Long memberId);
}
