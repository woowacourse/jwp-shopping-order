package cart.order.dao.entity;

import cart.order.domain.Order;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderItemEntity {

  private final Long id;
  private final Long orderId;
  private final String name;
  private final BigDecimal price;
  private final String imageUrl;
  private final int quantity;

  public OrderItemEntity(
      final Long id, final Long orderId,
      final String name, final BigDecimal price,
      final String imageUrl, final int quantity
  ) {
    this.id = id;
    this.orderId = orderId;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
  }

  public OrderItemEntity(
      final Long orderId, final String name,
      final BigDecimal price, final String imageUrl,
      final int quantity
  ) {
    this(null, orderId, name, price, imageUrl, quantity);
  }

  public static List<OrderItemEntity> makeOrderItemEntities(final Order order) {
    return order.getOrderedItems().getOrderItems()
        .stream()
        .map(it -> new OrderItemEntity(
            order.getId(),
            it.getName(),
            it.getPrice().getValue(),
            it.getImageUrl(),
            it.getQuantity()
        ))
        .collect(Collectors.toList());
  }

  public Long getId() {
    return id;
  }

  public Long getOrderId() {
    return orderId;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getQuantity() {
    return quantity;
  }
}
