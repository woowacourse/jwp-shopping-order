package cart.exception.authorization;

import cart.domain.member.MemberEmail;

public class OrderAccessForbiddenException extends AuthorizationException {

    private static final String MESSAGE = "해당 주문에 대한 권한이 없습니다. 현재 사용자 email: %s";

    public OrderAccessForbiddenException(final MemberEmail email) {
        super(String.format(MESSAGE, email.getEmail()));
    }
}
