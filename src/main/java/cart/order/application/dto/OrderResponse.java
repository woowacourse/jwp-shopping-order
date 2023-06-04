package cart.order.application.dto;

import cart.order_item.application.dto.OrderItemResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

public class OrderResponse {

  private Long orderId;
  private List<OrderItemResponse> products;
  private BigDecimal totalPayments;
  private ZonedDateTime createdAt;
  private String orderStatus;

  private OrderResponse() {
  }

  public OrderResponse(
      final Long orderId, final List<OrderItemResponse> products,
      final BigDecimal totalPayments, final ZonedDateTime createdAt,
      final String orderStatus
  ) {
    this.orderId = orderId;
    this.products = products;
    this.totalPayments = totalPayments;
    this.createdAt = createdAt;
    this.orderStatus = orderStatus;
  }

  public Long getOrderId() {
    return orderId;
  }

  public List<OrderItemResponse> getProducts() {
    return products;
  }

  public BigDecimal getTotalPayments() {
    return totalPayments;
  }

  public ZonedDateTime getCreatedAt() {
    return createdAt;
  }

  public String getOrderStatus() {
    return orderStatus;
  }
}
