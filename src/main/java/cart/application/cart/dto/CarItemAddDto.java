package cart.application.cart.dto;

public class CarItemAddDto {

	private final Long memberId;
	private final Long productId;

	public CarItemAddDto(final Long memberId, final Long productId) {
		this.memberId = memberId;
		this.productId = productId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public Long getProductId() {
		return productId;
	}
}
