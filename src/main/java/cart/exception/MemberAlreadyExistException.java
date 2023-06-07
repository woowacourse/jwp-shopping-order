package cart.exception;

public class MemberAlreadyExistException extends RuntimeException {

    public MemberAlreadyExistException(final String email) {
        super("이미 존재하는 유저입니다. / 입력하시면 이메일 : " + email);
    }
}
