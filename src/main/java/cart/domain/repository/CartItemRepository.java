package cart.domain.repository;

import cart.domain.CartItem;
import cart.domain.Member;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
public interface CartItemRepository {

    List<CartItem> findByMemberId(Long id);

    Long save(CartItem cartItem);

    CartItem findById(Long id);

    void deleteById(Long id);

    void updateQuantity(CartItem cartItem);

    List<CartItem> findAllByIds(Member member, List<Long> cartProductIds);

    void deleteByMemberCartItemIds(Long memberId, List<CartItem> cartItems);
}
