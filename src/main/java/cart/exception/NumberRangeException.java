package cart.exception;

public class NumberRangeException extends RuntimeException {
    private final String field;

    public NumberRangeException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}
