package cart.exception;

public class NotEnoughMoneyToPurchaseException extends RuntimeException {

    public NotEnoughMoneyToPurchaseException() {
        super("잔액이 부족합니다.");
    }
}
