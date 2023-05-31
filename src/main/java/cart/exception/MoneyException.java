package cart.exception;

public class MoneyException extends RuntimeException {

    public MoneyException(String message) {
        super(message);
    }

    public static class NotNegative extends MoneyException {
        public NotNegative() {
            super("금액은 음수일 수 없습니다.");
        }
    }
}
