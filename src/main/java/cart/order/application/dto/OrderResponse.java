package cart.order.application.dto;

import cart.order_item.application.dto.OrderItemResponse;
import java.util.List;

public class OrderResponse {

  private Long orderId;
  private List<OrderItemResponse> products;

  private OrderResponse() {
  }

  public OrderResponse(final Long orderId, final List<OrderItemResponse> products) {
    this.orderId = orderId;
    this.products = products;
  }

  public Long getOrderId() {
    return orderId;
  }

  public List<OrderItemResponse> getProducts() {
    return products;
  }
}
