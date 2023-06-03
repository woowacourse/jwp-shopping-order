package cart.exception.point;

public class HavingPointIsLessThanUsePointException extends PointException {
    private static final String MESSAGE = "보유한 포인트가 사용하고자 하는 포인트보다 적습니다. 보유 포인트 : %d / 사용하고자 하는 포인트 : %d";

    public HavingPointIsLessThanUsePointException(int having, int using) {
        super(String.format(MESSAGE, having, using));
    }
}
