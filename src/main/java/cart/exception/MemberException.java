package cart.exception;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super(message);
    }

    public static class NoExist extends MemberException {
        public NoExist(String message) {
            super(message);
        }
    }

    public static class EmptyEmail extends MemberException {
        public EmptyEmail() {
            super("회원의 이메일이 비었습니다.");
        }
    }

    public static class IllegalEmail extends MemberException {
        public IllegalEmail() {
            super("이메일은 0 ~ 50자 가능합니다.");
        }
    }

    public static class EmptyPassword extends MemberException {
        public EmptyPassword() {
            super("회원의 비밀번호가 비어있습니다.");
        }
    }

    public static class IllegalPassword extends MemberException {
        public IllegalPassword() {
            super("비밀번호는 4 ~ 50자 가능합니다.");
        }
    }

    public static class EmptyNickname extends MemberException {
        public EmptyNickname() {
            super("회원의 닉네임이 비어있습니다.");
        }
    }

    public static class IllegalNickname extends MemberException {
        public IllegalNickname() {
            super("닉네임은 0 ~ 20자 가능합니다.");
        }
    }
}
