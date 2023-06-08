package cart.step2.order.exception;

import cart.step2.error.exception.ErrorCode;
import cart.step2.error.exception.ShoppingOrderException;

public class OrderItemNotFoundException extends ShoppingOrderException {

    public static final ShoppingOrderException THROW = new OrderItemNotFoundException();

    public OrderItemNotFoundException() {
        super(new ErrorCode(404, "주문한 상품을 조회할 수 없습니다. 올바른 OrderItemId인지 확인해주세요."));
    }

}
