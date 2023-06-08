package cart.domain.cart;

import cart.domain.member.Member;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {
    Optional<CartItem> findById(Long id);

    List<CartItem> findByMember(Member member);

    Long create(CartItem cartItem);

    void update(CartItem cartItem);

    void deleteById(Long id);
}
