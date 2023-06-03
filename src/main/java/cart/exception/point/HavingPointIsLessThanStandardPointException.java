package cart.exception.point;

public class HavingPointIsLessThanStandardPointException extends PointException{
    private static final String MESSAGE = "보유한 포인트가 최소 사용 가능 포인트보다 적습니다. 보유 포인트 : %d / 사용할 수 있는 최소 포인트 : %d";

    public HavingPointIsLessThanStandardPointException(int having, int standard) {
        super(String.format(MESSAGE, having, standard));
    }
}
