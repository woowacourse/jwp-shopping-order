package cart.exception;

public class EmailInvalidException extends RuntimeException {

    public EmailInvalidException(final String email) {
        super("이메일의 형식이 올바르지 않습니다. ex) sosow0212@woowa.course / 입력하신 이메일 : " + email);
    }
}
