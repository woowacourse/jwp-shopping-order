package cart.exception.point;

import cart.exception.BadRequestException;

public class PointException {

    public static class OverThenMemberPoint extends BadRequestException {
        public OverThenMemberPoint() {
            super("포인트가 부족합니다");
        }
    }
}
