package cart.exception.authorization;

import cart.domain.member.MemberEmail;

public class CartItemException extends AuthorizationException {

    private static final String MESSAGE = "장바구니 상품에 대한 권한이 없습니다. 현재 사용자 email: %s";

    public CartItemException(final MemberEmail email) {
        super(String.format(MESSAGE, email.getEmail()));
    }
}
