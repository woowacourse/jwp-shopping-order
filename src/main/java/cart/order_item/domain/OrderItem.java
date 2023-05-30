package cart.order_item.domain;

import cart.order.domain.Order;
import cart.value_object.Money;

public class OrderItem {

  private Long id;

  private final Order order;

  private final String name;

  private final Money price;

  private final String imageUrl;

  private final int quantity;

  public OrderItem(
      final Long id, final Order order,
      final String name, final Money price,
      final String imageUrl, final int quantity
  ) {
    this.id = id;
    this.order = order;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
  }

  public OrderItem(
      final Order order, final String name,
      final Money price, final String imageUrl,
      final int quantity
  ) {
    this.order = order;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public Order getOrder() {
    return order;
  }

  public String getName() {
    return name;
  }

  public Money getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getQuantity() {
    return quantity;
  }
}
