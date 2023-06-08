package cart.exception;

public class ShoppingOrderResponse {

    private final String message;
    private final int code;

    public ShoppingOrderResponse(final String message, final int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
