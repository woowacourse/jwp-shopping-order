package cart.exception.point;

public class UsePointIsLessThanStandardPointException extends PointException{
    private static final String MESSAGE = "사용하고자 하는 포인트가 최소 사용 가능 포인트보다 적습니다. 사용하고자 하는 포인트 : %d / 최소 사용 가능 포인트 : %d";

    public UsePointIsLessThanStandardPointException(int using, int standard) {
        super(String.format(MESSAGE, using, standard));
    }
}
