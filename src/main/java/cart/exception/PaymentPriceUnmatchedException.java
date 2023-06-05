package cart.exception;

public class PaymentPriceUnmatchedException extends InvalidRequestValueException {
    private static final int ERROR_CODE = 4001;
    private static final String MESSAGE = "요금 정책과 다른 금액입니다";

    public PaymentPriceUnmatchedException() {
        super(ERROR_CODE, MESSAGE);
    }
}
