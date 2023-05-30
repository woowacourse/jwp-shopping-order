package cart.domain.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import cart.error.exception.CartItemException;

public class Cart {

	private final Long memberId;
	private final Map<Long, CartItem> cartItems;

	public Cart(final Long memberId, final List<CartItem> cartItems) {
		this.memberId = memberId;
		this.cartItems = cartItemsToMapById(cartItems);
	}

	public void checkOwner(final Long memberId) {
		if (!Objects.equals(this.memberId, memberId)) {
			throw new CartItemException.IllegalMember(memberId);
		}
	}

	public Optional<CartItem> findCartItemById(final Long cartItemId) {
		final CartItem cartItem = cartItems.get(cartItemId);
		return Optional.ofNullable(cartItem);
	}

	public Long getMemberId() {
		return memberId;
	}

	public List<CartItem> getCartItems() {
		return new ArrayList<>(cartItems.values());
	}

	private static Map<Long, CartItem> cartItemsToMapById(final List<CartItem> cartItems) {
		return cartItems.stream()
			.collect(Collectors.toMap(CartItem::getId, Function.identity()));
	}
}
