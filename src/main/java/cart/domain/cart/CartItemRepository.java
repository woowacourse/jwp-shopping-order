package cart.domain.cart;

import java.util.Optional;

public interface CartItemRepository {

	Optional<CartItem> findById(Long id);

	void updateQuantity(CartItem cartItem);

	void delete(Long memberId, Long productId);

	void deleteById(Long id);
}
