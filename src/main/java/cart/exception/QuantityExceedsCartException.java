package cart.exception;

public class QuantityExceedsCartException extends RuntimeException {

    public QuantityExceedsCartException() {
        super("요청하신 수량이 장바구니에 담긴 수량보다 큽니다.");
    }
}
