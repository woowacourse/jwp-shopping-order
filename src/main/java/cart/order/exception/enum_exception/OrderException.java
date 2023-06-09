package cart.order.exception.enum_exception;

import cart.exception.enum_exception.CustomException;
import cart.exception.enum_exception.CustomExceptionType;

public class OrderException extends CustomException {

  private final OrderExceptionType orderExceptionType;

  public OrderException(final OrderExceptionType orderExceptionType) {
    super(orderExceptionType.errorMessage());
    this.orderExceptionType = orderExceptionType;
  }

  @Override
  public CustomExceptionType getExceptionType() {
    return orderExceptionType;
  }
}
