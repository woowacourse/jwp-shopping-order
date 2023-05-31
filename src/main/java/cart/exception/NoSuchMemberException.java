package cart.exception;

public final class NoSuchMemberException extends RuntimeException {

    public NoSuchMemberException() {
        super("찾을 수 없는 멤버입니다.");
    }
}
