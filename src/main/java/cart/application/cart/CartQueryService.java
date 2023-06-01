package cart.application.cart;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.cart.dto.CartItemDto;
import cart.domain.cart.Cart;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartRepository;

@Transactional(readOnly = true)
@Service
public class CartQueryService {
	private final CartRepository cartRepository;

	public CartQueryService(final CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public List<CartItemDto> findByMemberId(final Long memberId) {
		final Cart cart = cartRepository.findByMemberId(memberId);
		final List<CartItem> cartItems = cart.getCartItems();

		return cartItems.stream()
			.map(CartItemDto::from)
			.collect(Collectors.toList());
	}

}
