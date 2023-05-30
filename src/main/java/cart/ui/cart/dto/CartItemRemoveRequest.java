package cart.ui.cart.dto;

import java.util.List;

public class CartItemRemoveRequest {

	private List<Long> cartItemIds;

	public CartItemRemoveRequest() {
	}

	public CartItemRemoveRequest(final List<Long> cartItemIds) {
		this.cartItemIds = cartItemIds;
	}

	public List<Long> getCartItemIds() {
		return cartItemIds;
	}
}
