package cart.exception.global.point;

import cart.domain.member.MemberPoint;
import cart.exception.global.GlobalException;

public class PointAbusedException extends GlobalException {

    private static final String message = "해당 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용할 수 없습니다. 보유한 포인트: %d, 입력한 포인트: %d";

    public PointAbusedException(final MemberPoint original, final MemberPoint input) {
        super(String.format(message, original.getPoint(), input.getPoint()));
    }
}
