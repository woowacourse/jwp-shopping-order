package cart.exception;

public class MoneyException extends RuntimeException {

    public MoneyException(final String message) {
        super(message);
    }

    public static class IllegalValue extends MoneyException {

        public IllegalValue(final long value) {
            super("Illegal money value; value=" + value);
        }
    }
}
