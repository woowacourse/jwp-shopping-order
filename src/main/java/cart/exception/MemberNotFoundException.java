package cart.exception;

public class MemberNotFoundException extends MemberException {
    public MemberNotFoundException(final Long id) {
        super("해당 사용자를 찾을 수 없습니다 : " + id);
    }

    public MemberNotFoundException(final String email) {
        super("해당 사용자를 찾을 수 없습니다 : " + email);
    }
}
