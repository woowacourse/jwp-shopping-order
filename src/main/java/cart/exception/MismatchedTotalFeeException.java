package cart.exception;

public class MismatchedTotalFeeException extends RuntimeException {

    public MismatchedTotalFeeException() {
        super("계산된 금액이 잘못되었습니다.");
    }
}
