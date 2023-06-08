package cart.exception.notfound;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "해당 사용자가 존재하지 않습니다. 입력한 이메일: %s";

    public MemberNotFoundException() {
        super("해당 사용자가 존재하지 않습니다.");
    }

    public MemberNotFoundException(final String email) {
        super(String.format(MESSAGE, email));
    }
}
