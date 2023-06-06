package cart.application.dto.request;

public class PayItemRequest {

    private Long cartItemId;

    public PayItemRequest() {
    }

    public PayItemRequest(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}
