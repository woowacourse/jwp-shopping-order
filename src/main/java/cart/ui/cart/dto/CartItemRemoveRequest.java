package cart.ui.cart.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemRemoveRequest {

	@JsonProperty("cartItemIdList")
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
