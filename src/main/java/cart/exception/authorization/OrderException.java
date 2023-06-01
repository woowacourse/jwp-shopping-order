package cart.exception.authorization;

import cart.domain.member.MemberEmail;

public class OrderException extends AuthorizationException {

    private static final String message = "해당 주문에 대한 권한이 없습니다. 현재 사용자 email: %s";

    public OrderException(final MemberEmail email) {
        super(String.format(message, email.getEmail()));
    }
}
