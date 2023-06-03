package cart.exception;

public class NotEnoughMoneyException extends RuntimeException {

    public NotEnoughMoneyException() {
        super("잔액이 부족합니다.");
    }
}
