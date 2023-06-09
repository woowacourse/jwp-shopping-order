package cart.order.domain;

import cart.value_object.Money;

public class OrderItem {

  private Long id;

  private final String name;

  private final Money price;

  private final String imageUrl;

  private final int quantity;

  public OrderItem(
      final Long id, final String name,
      final Money price, final String imageUrl,
      final int quantity
  ) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
  }

  OrderItem(
      final String name,
      final Money price,
      final String imageUrl,
      final int quantity
  ) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
  }

  Money calculatePrice() {
    return price.multiply(quantity);
  }

  public Long getId() {
    return id;
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
