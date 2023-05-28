package cart.exception;

public class InvalidPointException extends GlobalException {

    private static final String message1 = "해당 유저가 가지고 있는 포인트보다 더 많은 포인트를 사용할 수 없습니다. 보유한 포인트: %d, 입력한 포인트: %d";
    private static final String message2 = "포인트는 음수값일 수 없습니다.";

    public InvalidPointException(final Integer original, final Integer input) {
        super(String.format(message1, original, input));
    }

    public InvalidPointException() {
        super(message2);
    }
}
