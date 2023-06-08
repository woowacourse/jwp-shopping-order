package cart.exception;

public class PointNotEnoughException extends PointException {

    public PointNotEnoughException(final int memberPoint, final int usedPoint) {
        super("보유한 포인트가 부족합니다. 보유 포인트: " + memberPoint + ", 사용 포인트: " + usedPoint);
    }
}
