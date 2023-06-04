package cart.exception;

public class MismatchedTotalPriceException extends RuntimeException {

    public MismatchedTotalPriceException() {
        super("계산된 금액이 잘못되었습니다.");
    }
}
