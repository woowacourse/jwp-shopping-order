package cart.exception;

public class PaymentAmountNotEqualException extends IllegalArgumentException {

    private static final String MESSAGE = "지불 금액이 다릅니다.";

    public PaymentAmountNotEqualException() {
        super(MESSAGE);
    }
}
