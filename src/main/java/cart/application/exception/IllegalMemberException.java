package cart.application.exception;

import cart.application.domain.CartItem;
import cart.application.domain.Member;

public class IllegalMemberException extends RuntimeException {

    private static final String MESSAGE = "해당 멤버는 카트에 접근할 수 없습니다.";

    public IllegalMemberException() {
        super(MESSAGE);
    }
}
