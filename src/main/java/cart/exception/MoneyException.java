package cart.exception;

public class MoneyException extends ApiException {

    public MoneyException(String message) {
        super(message);
    }

    public static class InvalidRange extends MoneyException {

        public InvalidRange(long money) {
            super("잘못된 금액입니다. " +
                    "money : " + money);
        }
    }
}
