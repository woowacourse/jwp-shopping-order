package cart.order.exception;

import cart.common.execption.BaseException;
import cart.common.execption.BaseExceptionType;

public class OrderException extends BaseException {

    private final OrderExceptionType orderExceptionType;

    public OrderException(OrderExceptionType orderExceptionType) {
        this.orderExceptionType = orderExceptionType;
    }

    @Override
    public BaseExceptionType exceptionType() {
        return orderExceptionType;
    }
}
