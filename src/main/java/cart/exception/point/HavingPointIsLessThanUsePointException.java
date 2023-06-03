package cart.exception.point;

public class HavingPointIsLessThanUsePointException extends PointException {
    private static final String MESSAGE = "보유한 포인트가 사용하고자 하는 포인트보다 적습니다.";

    public HavingPointIsLessThanUsePointException() {
        super(MESSAGE);
    }
}
