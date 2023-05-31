package cart.exception;

public class InvalidMoneyException extends RuntimeException {

    public InvalidMoneyException() {
        super("금액은 양의 정수이어야 합니다.");
    }
}
