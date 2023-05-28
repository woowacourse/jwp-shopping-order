package cart.application.repository;

import cart.domain.CartItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository {

    List<CartItem> findByMemberId(Long id);

    CartItem findById(Long id);

    Long insert(CartItem cartItem);

    void deleteById(Long id);

    void update(CartItem cartItem);
}
