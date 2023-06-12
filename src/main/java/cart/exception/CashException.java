package cart.exception;

public class CashException extends RuntimeException {

    public CashException(String message) {
        super(message);
    }

    public static class SmallerThanMinimum extends CashException {

        public SmallerThanMinimum() {
            super("사용자 지불 금액은 0보다 커야합니다.");
        }
    }
}
