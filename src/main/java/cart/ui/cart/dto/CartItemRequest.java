package cart.ui.cart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class CartItemRequest {

	@NotNull
	@Positive(message = "존재하지 않는 상품입니다.")
	private Long productId;

	public CartItemRequest() {
	}

	public CartItemRequest(Long productId) {
		this.productId = productId;
	}

	public Long getProductId() {
		return productId;
	}
}
