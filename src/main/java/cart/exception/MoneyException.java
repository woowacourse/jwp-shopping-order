package cart.exception;

public abstract class MoneyException extends RuntimeException {

    private MoneyException(String message) {
        super(message);
    }

    private MoneyException(String message, Throwable cause) {
        super(message, cause);
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

    public static class DecimalMoney extends MoneyException {

        public DecimalMoney(Throwable cause) {
            super("금액에 소수점이 존재할 수 없습니다.", cause);
        }
    }
}
