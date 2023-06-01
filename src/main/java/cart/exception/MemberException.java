package cart.exception;

public class MemberException extends ShoppingException {

    public MemberException(String message) {
        super(message);
    }

    public static class NotFound extends MemberException {

        public NotFound() {
            super("해당 멤버가 존재하지 않습니다.");
        }
    }

    public static class EmailEmpty extends MemberException {

        public EmailEmpty() {
            super("멤버 이메일은 필수입니다.");
        }
    }

    public static class EmailOverLength extends MemberException {

        public EmailOverLength(int currentLength, int maxLength) {
            super("멤버 이메일은 최대 " + maxLength + "글자까지 가능합니다. 현재 길이: " + currentLength);
        }
    }

    public static class PasswordEmpty extends MemberException {

        public PasswordEmpty() {
            super("멤버 비밀번호는 필수입니다.");
        }
    }

    public static class PasswordOverLength extends MemberException {

        public PasswordOverLength(int currentLength, int maxLength) {
            super("멤버 비밀번호는 최대 " + maxLength + "글자까지 가능합니다. 현재 길이: " + currentLength);
        }
    }

    public static class NegativePoint extends MemberException {

        public NegativePoint() {
            super("멤버의 포인트는 0보다 작을 수 없습니다.");
        }
    }

    public static class PasswordNotMatch extends MemberException {

        public PasswordNotMatch() {
            super("멤버의 비밀번호가 올바르지 않습니다.");
        }
    }
}
