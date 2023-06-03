package cart.exception.payment;

public class TotalPriceDoesNotMatchException extends PaymentException {
    private static final String MESSAGE = "총 결제 금액이 다릅니다. 입력된 금액 : %d / 실제 금액 : %d";

    public TotalPriceDoesNotMatchException(int input, int real) {
        super(String.format(MESSAGE, input, real));
    }
}
