package cart.application.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    long create(CartItem cartItem);

    List<CartItem> findByMember(Member member);

    Optional<CartItem> findById(long id);

    void deleteById(long id);

    void updateQuantity(CartItem cartItem);
}
