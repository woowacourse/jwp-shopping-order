package cart.application.repository;

import cart.application.domain.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository {

    CartItem insert(CartItem cartItem);

    CartItem findById(Long id);

    List<CartItem> findByMemberId(Long id);

    void update(CartItem cartItem);

    void deleteById(Long id);

    void deleteByMemberId(Long memberId);
}
