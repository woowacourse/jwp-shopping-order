package cart.exception;

public class PriceInconsistencyException extends RuntimeException {

    public PriceInconsistencyException(String message) {
        super(message);
    }
}
