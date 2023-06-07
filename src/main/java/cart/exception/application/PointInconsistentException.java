package cart.exception.application;

public class PointInconsistentException extends ApplicationException {

    private static final String MESSAGE = "포인트 계산 결과가 다릅니다.";

    public PointInconsistentException() {
        super(MESSAGE);
    }
}
