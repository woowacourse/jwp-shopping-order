package cart.exception.member;

public class MemberException extends RuntimeException {
    public MemberException(String message) {
        super(message);
    }

    public static class NotExistMember extends MemberException {
        public NotExistMember() {
            super("존재하지 않는 회원입니다");
        }
    }
}
