package cart.dto.response;

public class ShoppingOrderResultResponse<T> extends ShoppingOrderResponse {
    private final T result;

    public ShoppingOrderResultResponse(String message, T result) {
        super(message);
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
