package cart.cartitem.domain;

import java.util.List;

public interface CartItemRepository {

    Long save(CartItem cartItem);

    void update(CartItem cartItem);

    void deleteById(Long id);

    CartItem findById(Long id);

    List<CartItem> findByMemberId(Long memberId);
}
