package cart.application.repository;

import cart.domain.Member;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    long create(CartItem cartItem);

    List<CartItem> findByMember(Member member);

    Optional<CartItem> findById(long id);

    CartItems findByIds(List<Long> ids);

    void updateQuantity(CartItem cartItem);

    void delete(CartItem cartItem);

    void deleteAll(CartItems cartItems);
}
