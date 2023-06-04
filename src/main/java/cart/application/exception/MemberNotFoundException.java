package cart.application.exception;

public class MemberNotFoundException extends ApplicationException {

    private static final String MESSAGE = "해당하는 멤버를 찾을 수 없습니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
