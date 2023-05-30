package cart.order.application.dto;

import java.math.BigDecimal;
import java.util.List;

public class SpecificOrderResponse {

  private Long orderId;
  private List<OrderItemResponse> products;
  private BigDecimal totalPrice;
  private BigDecimal deliveryFee;

  private SpecificOrderResponse() {
  }

  public SpecificOrderResponse(
      final Long orderId,
      final List<OrderItemResponse> products,
      final BigDecimal totalPrice,
      final BigDecimal deliveryFee
  ) {
    this.orderId = orderId;
    this.products = products;
    this.totalPrice = totalPrice;
    this.deliveryFee = deliveryFee;
  }

  public Long getOrderId() {
    return orderId;
  }

  public List<OrderItemResponse> getProducts() {
    return products;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public BigDecimal getDeliveryFee() {
    return deliveryFee;
  }
}
