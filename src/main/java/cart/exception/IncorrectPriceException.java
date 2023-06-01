package cart.exception;

public class IncorrectPriceException extends RuntimeException {

    public IncorrectPriceException() {
        super("결제 요청 금액과 다릅니다.");
    }
}
