package cart.exception;

public class NegativeStockException extends IllegalArgumentException {

    private static final String MESSAGE = "재고는 음수일 수 없습니다.";

    public NegativeStockException() {
        super(MESSAGE);
    }
}
