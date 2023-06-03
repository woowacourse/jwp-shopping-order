package cart.application.exception;

import cart.application.domain.CartItem;
import cart.application.domain.Member;

public class IllegalMemberException extends ExpectedException {

    private static final String MESSAGE = "해당 장바구니에 접근할 수 없습니다.";

    public IllegalMemberException() {
        super(MESSAGE);
    }
}
