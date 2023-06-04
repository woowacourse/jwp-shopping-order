package cart.application.cart;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cart.application.cart.dto.CartItemResponse;
import cart.domain.cart.Cart;
import cart.domain.cart.CartRepository;

@Transactional(readOnly = true)
@Service
public class CartQueryService {
	private final CartRepository cartRepository;

	public CartQueryService(final CartRepository cartRepository) {
		this.cartRepository = cartRepository;
	}

	public List<CartItemResponse> findByMemberId(final Long memberId) {
		final Cart cart = cartRepository.findByMemberId(memberId);

		return cart.getCartItems().stream()
			.map(CartItemResponse::from)
			.collect(Collectors.toList());
	}

}
