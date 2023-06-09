package cart.order.exception.order;

import cart.order.exception.order.OrderException;

public class OutOfStockException extends OrderException {
    private static final String MESSAGE = "상품 재고가 부족합니다.";

    public OutOfStockException() {
        super(MESSAGE);
    }
}
