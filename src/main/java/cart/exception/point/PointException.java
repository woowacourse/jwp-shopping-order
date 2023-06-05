package cart.exception.point;

import cart.exception.policy.PolicyException;

public class PointException extends RuntimeException{
    public PointException(String message) {
        super(message);
    }

    public static class OverThenMemberPoint extends PointException {
        public OverThenMemberPoint() {
            super("포인트가 부족합니다");
        }
    }
}
