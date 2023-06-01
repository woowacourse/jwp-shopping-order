package cart.ui.cart.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class CartItemQuantityUpdateRequest {

	@NotNull
	@PositiveOrZero(message = "수량은 0보다 작을 수 없습니다. ")
	private int quantity;

	public CartItemQuantityUpdateRequest() {
	}

	public CartItemQuantityUpdateRequest(int quantity) {
		this.quantity = quantity;
	}

	public int getQuantity() {
		return quantity;
	}
}
