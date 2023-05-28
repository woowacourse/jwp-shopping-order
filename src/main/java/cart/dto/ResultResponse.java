package cart.dto;

public class ResultResponse<T> extends Response {
    private final T result;

    public ResultResponse(String message, T result) {
        super(message);
        this.result = result;
    }

    public T getResult() {
        return result;
    }
}
