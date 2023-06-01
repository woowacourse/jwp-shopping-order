package cart.exception;

public abstract class MoneyException extends RuntimeException {

    private MoneyException(final String message) {
        super(message);
    }

    public static class Negative extends MoneyException {

        public Negative() {
            super("금액은 음수가 될 수 없습니다.");
        }
    }

    public static class MultiplyZeroOrNegative extends MoneyException {

        public MultiplyZeroOrNegative() {
            super("금액에 0 이하의 값을 곱할 수 없습니다.");
        }
    }
}
