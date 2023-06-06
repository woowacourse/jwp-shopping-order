package cart.dto.response;

public class ShoppingOrderResponse {
    private final String message;

    public ShoppingOrderResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
