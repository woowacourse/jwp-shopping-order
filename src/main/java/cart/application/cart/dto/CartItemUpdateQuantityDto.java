package cart.application.cart.dto;

public class CartItemUpdateQuantityDto {

	private final Long memberId;
	private final Long cartItemId;
	private final int quantity;

	public CartItemUpdateQuantityDto(final Long memberId, final Long cartItemId, final int quantity) {
		this.memberId = memberId;
		this.cartItemId = cartItemId;
		this.quantity = quantity;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Long getCartItemId() {
		return cartItemId;
	}

	public int getQuantity() {
		return quantity;
	}
}
