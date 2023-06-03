package cart.exception.payment;

public class TotalProductPriceDoesNotMatchException extends PaymentException{
    private static final String MESSAGE = "상품의 총 금액이 다릅니다. 입력된 금액 : %d / 실제 금액 : %d";

    public TotalProductPriceDoesNotMatchException(int input, int real) {
        super(String.format(MESSAGE, input, real));
    }
}
