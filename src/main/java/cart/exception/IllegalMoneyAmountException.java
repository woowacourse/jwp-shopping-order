package cart.exception;

public class IllegalMoneyAmountException extends RuntimeException {

    public IllegalMoneyAmountException() {
        super("금액이 음수가 될 수 없습니다.");
    }
}
