package cart.exception;

public class InvalidMoneyValueException extends CartException {

    public InvalidMoneyValueException() {
        super("돈은 음수가 될 수 없습니다.");
    }
}
