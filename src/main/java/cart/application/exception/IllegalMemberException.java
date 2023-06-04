package cart.application.exception;

public class IllegalMemberException extends ApplicationException {

    private static final String MESSAGE = "해당 장바구니에 접근할 수 없습니다.";

    public IllegalMemberException() {
        super(MESSAGE);
    }
}
