package cart.dto;

import javax.validation.constraints.NotNull;

public class CartItemQuantityUpdateRequest {

    @NotNull(message = "수량을 입력하세요")
    private Integer quantity;

    private CartItemQuantityUpdateRequest(){
    }

    private CartItemQuantityUpdateRequest(final Integer quantity) {
        this.quantity = quantity;
    }

    public static CartItemQuantityUpdateRequest from(final Integer quantity) {
        return new CartItemQuantityUpdateRequest(quantity);
    }

    public Integer getQuantity() {
        return quantity;
    }
}
