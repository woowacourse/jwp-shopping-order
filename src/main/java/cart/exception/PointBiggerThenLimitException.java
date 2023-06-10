package cart.exception;

public class PointBiggerThenLimitException extends PointException {
    public PointBiggerThenLimitException(final int limitPoint, final int usedPoint) {
        super("사용 가능한 최대 포인트를 초과하였습니다. 사용 가능 최대 포인트: " + limitPoint + ", 사용 포인트: " + usedPoint);
    }
}
