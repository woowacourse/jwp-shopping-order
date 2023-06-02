package cart.order_item.domain;

import cart.value_object.Money;
import java.util.List;

public class OrderedItems {

  private final List<OrderItem> orderItems;

  public OrderedItems(final List<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public Money calculateAllItemPrice() {
    return orderItems.stream()
        .map(it -> it.getPrice().multiply(it.getQuantity()))
        .reduce(Money.ZERO, (Money::add));
  }
}