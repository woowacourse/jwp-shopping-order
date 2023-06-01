package cart.order_item.application.dto;

import java.math.BigDecimal;

public class OrderItemResponse {

  private Long id;
  private String name;
  private String imageUrl;
  private int quantity;
  private BigDecimal totalPrice;

  private OrderItemResponse() {
  }

  public OrderItemResponse(
      final Long id, final String name,
      final String imageUrl, final int quantity,
      final BigDecimal totalPrice
  ) {
    this.id = id;
    this.name = name;
    this.imageUrl = imageUrl;
    this.quantity = quantity;
    this.totalPrice = totalPrice;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getQuantity() {
    return quantity;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }
}
