package cart.exception;

public class NonExistMemberException extends RuntimeException {

    public NonExistMemberException() {
        super("존재하지 않는 사용자입니다.");
    }
}
