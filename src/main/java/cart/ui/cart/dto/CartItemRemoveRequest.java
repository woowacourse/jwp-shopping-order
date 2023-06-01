package cart.ui.cart.dto;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemRemoveRequest {

	@NotEmpty(message = "한 가지 이상의 상품을 선택하셔야합니다.")
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
