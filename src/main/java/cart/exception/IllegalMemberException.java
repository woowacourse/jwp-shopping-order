package cart.exception;

public class IllegalMemberException extends RuntimeException {

    public IllegalMemberException() {
        super("권한이 없는 사용자입니다.");
    }
}
