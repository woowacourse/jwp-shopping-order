package cart.exception;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class NotExist extends MemberException {

        public NotExist() {
            super("존재하지 않는 회원입니다.");
        }
    }

    public static class InvalidPassword extends MemberException {

        public InvalidPassword() {
            super("잘못된 비밀번호 형식입니다.");
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
