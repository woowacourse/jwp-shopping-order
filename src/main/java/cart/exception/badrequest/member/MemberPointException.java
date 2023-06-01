package cart.exception.badrequest.member;

import cart.exception.badrequest.BadRequestException;

public class MemberPointException extends BadRequestException {

    public MemberPointException(String message) {
        super(message);
    }
}
