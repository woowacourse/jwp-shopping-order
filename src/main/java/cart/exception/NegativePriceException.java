package cart.exception;

public class NegativePriceException extends IllegalArgumentException {

    private static final String MESSAGE = "가격은 음수일 수 없습니다.";

    public NegativePriceException() {
        super(MESSAGE);
    }
}
