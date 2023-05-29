package cart.order_item.domain;

import cart.order.domain.Order;
import cart.value_object.Money;

public class OrderItem {

  private Long id;

  private Order order;

  private String name;

  private Money price;

  private String imageUrl;

  private int quantity;
}
