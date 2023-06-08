package cart.exception;

public class NotEnoughQuantityException extends IllegalArgumentException {

    private static final String MESSAGE = "재고가 부족합니다.";

    public NotEnoughQuantityException() {
        super(MESSAGE);
    }
}
