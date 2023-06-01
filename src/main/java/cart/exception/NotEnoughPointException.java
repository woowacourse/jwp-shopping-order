package cart.exception;

public class NotEnoughPointException extends IllegalArgumentException {

    private static final String MESSAGE = "보유 포인트 : %s 보다 사용 포인트 : %s 가 더 많습니다.";

    public NotEnoughPointException(final int leftPoint, final int usedPoint) {
        super(String.format(MESSAGE, leftPoint, usedPoint));
    }
}
