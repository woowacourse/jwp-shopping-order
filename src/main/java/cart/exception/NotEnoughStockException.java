package cart.exception;

public final class NotEnoughStockException extends RuntimeException {

    public NotEnoughStockException() {
        super("재고가 부족합니다.");
    }
}
