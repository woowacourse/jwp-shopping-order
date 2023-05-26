package cart.exception;

public class EmailInvalidException extends RuntimeException {

    public EmailInvalidException() {
        super("이메일의 형식이 올바르지 않습니다. ex) sosow0212@woowa.course");
    }
}
