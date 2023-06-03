package cart.exception;

import cart.domain.member.Email;

public class MemberException extends RuntimeException {

    public MemberException(final String message) {
        super(message);
    }

    public static class NotFound extends MemberException {
        public NotFound(final Long id) {
            super(id + " ID를 가진 멤버를 찾을 수 없습니다.");
        }

        public NotFound(final Email email) {
            super(email.email() + " 이메일을 가진 유저를 찾을 수 없습니다.");
        }
    }
}
