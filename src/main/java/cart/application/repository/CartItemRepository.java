package cart.application.repository;

import cart.domain.Member;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItems;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository {

    long create(CartItem cartItem);

    // TODO: CartItems(일급 컬렉션) 활용하도록 수정
    List<CartItem> findByMember(Member member);

    Optional<CartItem> findById(long id);

    CartItems findByIds(List<Long> ids);

    void deleteById(long id);

    void updateQuantity(CartItem cartItem);
}
