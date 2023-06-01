package cart.exception.badrequest.member;

import cart.exception.badrequest.BadRequestException;

public class MemberEmailException extends BadRequestException {

    public MemberEmailException(String message) {
        super(message);
    }
}
