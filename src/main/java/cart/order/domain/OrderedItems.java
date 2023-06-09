package cart.order.domain;

import cart.order.exception.enum_exception.OrderException;
import cart.order.exception.enum_exception.OrderExceptionType;
import cart.value_object.Money;
import java.util.List;

public class OrderedItems {

  private final List<OrderItem> orderItems;

  private OrderedItems(final List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public static OrderedItems createdFromOrder(
      final List<OrderItem> orderItems,
      final Money targetTotalPrice
  ) {
    final Money totalPrice = orderItems.stream()
        .map(OrderItem::calculatePrice)
        .reduce(Money.ZERO, (Money::add));

    validateSameTotalPrice(targetTotalPrice, totalPrice);

    return new OrderedItems(orderItems);
  }

  public static OrderedItems createdFromLookUp(final List<OrderItem> orderItems) {
    return new OrderedItems(orderItems);
  }

  private static void validateSameTotalPrice(final Money targetTotalPrice, final Money totalPrice) {
    if (totalPrice.isNotSame(targetTotalPrice)) {
      throw new OrderException(OrderExceptionType.NOT_SAME_TOTAL_PRICE);
    }
  }

  public Money calculateAllItemPrice() {
    return orderItems.stream()
        .map(OrderItem::calculatePrice)
        .reduce(Money.ZERO, (Money::add));
  }

  public List<OrderItem> getOrderItems() {
    return orderItems;
  }
}
