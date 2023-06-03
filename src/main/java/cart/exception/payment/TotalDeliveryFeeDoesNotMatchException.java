package cart.exception.payment;

public class TotalDeliveryFeeDoesNotMatchException extends PaymentException{
    private static final String MESSAGE = "총 배달비가 다릅니다. 입력된 금액 : %d / 실제 금액 : %d";

    public TotalDeliveryFeeDoesNotMatchException(int input, int real) {
        super(String.format(MESSAGE, input, real));
    }
}
