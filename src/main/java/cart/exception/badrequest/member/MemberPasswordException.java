package cart.exception.badrequest.member;

import cart.exception.badrequest.BadRequestException;

public class MemberPasswordException extends BadRequestException {

    public MemberPasswordException(String message) {
        super(message);
    }
}
