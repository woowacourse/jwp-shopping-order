package cart.exception.point;

import cart.exception.GlobalException;

public class PointAbusedException extends GlobalException {

    private static final String message = "해당 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용할 수 없습니다. 보유한 포인트: %d, 입력한 포인트: %d";

    public PointAbusedException(final Integer original, final Integer input) {
        super(String.format(message, original, input));
    }
}
