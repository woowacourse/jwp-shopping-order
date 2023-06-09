package cart.exception;

public class InvalidOrderCalculationException extends RuntimeException {
    public InvalidOrderCalculationException(String message) {
        super(message);
    }
}
