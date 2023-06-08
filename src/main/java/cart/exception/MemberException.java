package cart.exception;

public class MemberException extends ApiException {

    public MemberException(String message) {
        super(message);
    }

    public static class InvalidPassword extends MemberException {

        public InvalidPassword() {
            super("잘못된 비밀번호 형식입니다.");
        }
    }

    public static class InvalidEmail extends MemberException {

        public InvalidEmail(String email) {
            super("잘못된 이메일 형식입니다. " +
                    "email : " + email);
        }
    }

    public static class NotFound extends MemberException {

        public NotFound(Long id) {
            super("해당 멤버를 찾을 수 없습니다. " +
                    "id : " + id);
        }

        public NotFound(String email) {
            super("해당 멤버를 찾을 수 없습니다. " +
                    "email : " + email);
        }
    }
}
