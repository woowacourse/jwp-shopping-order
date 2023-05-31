package cart.exception;

import cart.domain.CartItem;
import cart.domain.Member;

public class IllegalAccessCartException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "다른 회원의 장바구니에 접속을 시도 중입니다.";

    public IllegalAccessCartException(final String message) {
        super(message);
    }

    public IllegalAccessCartException(final CartItem cartItem, final Member member) {
        super(DEFAULT_MESSAGE + " cartItemId=" + cartItem.getId() + ", memberId=" + member.getId());
    }

}
