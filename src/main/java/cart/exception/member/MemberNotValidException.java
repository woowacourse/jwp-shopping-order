package cart.exception.member;

import cart.exception.CartException;

public class MemberNotValidException extends CartException {

    public MemberNotValidException(final String message) {
        super(message);
    }
}
