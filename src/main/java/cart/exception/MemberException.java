package cart.exception;

import static cart.domain.Member.MINIMUM_PASSWORD_LENGTH;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class InvalidPasswordByNull extends MemberException {

        public InvalidPasswordByNull() {
            super("비밀번호는 빈 값일 수 없습니다.");
        }
    }

    public static class InvalidPasswordByLength extends MemberException {

        public InvalidPasswordByLength(String password) {
            super("비밀번호는 최소 " + MINIMUM_PASSWORD_LENGTH + "자 이상이어야 합니다; password=" + password);
        }
    }

    public static class InvalidEmailByNull extends MemberException {

        public InvalidEmailByNull() {
            super("이메일은 빈 값으로 입력할 수 없습니다.");
        }
    }

    public static class InvalidEmail extends MemberException {

        public InvalidEmail() {
            super("이메일 형식을 확인해주세요.");
        }
    }

    public static class InvalidIdByNull extends MemberException {

        public InvalidIdByNull() {
            super("멤버 아이디를 입력해야 합니다.");
        }
    }
}
