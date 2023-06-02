package cart.presentation.dto.request;

public class CartItemQuantityRequest {

    private Long quantity;

    public CartItemQuantityRequest() {
    }

    public CartItemQuantityRequest(Long quantity) {
        this.quantity = quantity;
    }

    public Long getQuantity() {
        return quantity;
    }
}
