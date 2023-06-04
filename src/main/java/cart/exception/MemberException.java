package cart.exception;

import cart.ui.advcie.ErrorType;

public class MemberException extends RuntimeException {
    public MemberException() {
    }

    public MemberException(String message) {
        super(message);
    }

    public static class NoExist extends MemberException {
        public NoExist(String message) {
            super(message);
        }
    }
}
