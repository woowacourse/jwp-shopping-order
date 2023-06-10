package cart.exception;

public class PointNegativeException extends PointException {
    public PointNegativeException(final int point) {
        super("포인트는 음수일 수 없습니다. point: " + point);
    }
}
