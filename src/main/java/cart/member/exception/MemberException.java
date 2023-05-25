package cart.member.exception;

import cart.common.execption.BaseException;
import cart.common.execption.BaseExceptionType;

public class MemberException extends BaseException {

    private final MemberExceptionType memberExceptionType;

    public MemberException(MemberExceptionType memberExceptionType) {
        this.memberExceptionType = memberExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return memberExceptionType;
    }
}

