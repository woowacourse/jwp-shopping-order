package cart.exception;

public class MemberException extends RuntimeException {

    public MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {

        public NotFound() {
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

    public static class NotEnoughMoney extends MemberException {

        public NotEnoughMoney() {
            super("회원의 금액이 부족합니다.");
        }
    }

    public static class NotEnoughPoint extends MemberException {

        public NotEnoughPoint() {
            super("회원의 포인트가 부족합니다.");
        }
    }
}
