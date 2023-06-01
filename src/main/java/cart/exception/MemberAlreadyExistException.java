package cart.exception;

public class MemberAlreadyExistException extends RuntimeException {

    public MemberAlreadyExistException() {
        super("이미 존재하는 유저입니다.");
    }
}
