package cart.application.cart.dto;

public class CartDto {

	private final Long memberId;
	private final CartItemDto cartItemDto;

	public CartDto(final Long memberId, final CartItemDto cartItemDto) {
		this.memberId = memberId;
		this.cartItemDto = cartItemDto;
	}

	public Long getMemberId() {
		return memberId;
	}

	public CartItemDto getCartItemDto() {
		return cartItemDto;
	}
}
