package cart.application.cart.dto;

import java.util.List;

public class CartItemsRemoveDto {

	private final Long memberId;
	private final List<Long> cartItemIds;

	public CartItemsRemoveDto(final Long memberId, final List<Long> cartItemIds) {
		this.memberId = memberId;
		this.cartItemIds = cartItemIds;
	}

	public Long getMemberId() {
		return memberId;
	}

	public List<Long> getCartItemIds() {
		return cartItemIds;
	}
}
