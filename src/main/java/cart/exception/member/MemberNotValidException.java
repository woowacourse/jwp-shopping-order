package cart.exception.member;

import cart.exception.common.CartException;

public class MemberNotValidException extends CartException {

    public MemberNotValidException(final String message) {
        super(message);
    }
}
