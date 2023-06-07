package cart.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException() {
        super("패스워드는 빈 값이면 안됩니다. ex) 1234");
    }
}
