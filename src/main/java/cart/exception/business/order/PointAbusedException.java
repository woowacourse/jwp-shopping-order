package cart.exception.business.order;

import cart.domain.member.MemberPoint;
import cart.exception.business.BusinessException;

public class PointAbusedException extends BusinessException {

    private static final String MESSAGE = "해당 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용할 수 없습니다. 보유한 포인트: %d, 입력한 포인트: %d";

    public PointAbusedException(final MemberPoint original, final MemberPoint input) {
        super(String.format(MESSAGE, original.getPoint(), input.getPoint()));
    }
}
