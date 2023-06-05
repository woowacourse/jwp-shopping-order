package cart.order.application.dto;

import cart.coupon.application.dto.CouponResponse;
import cart.order_item.application.dto.OrderItemResponse;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SpecificOrderResponse {

  private Long orderId;
  private List<OrderItemResponse> products;
  private BigDecimal totalPrice;
  private BigDecimal deliveryFee;
  private BigDecimal totalPayments;
  private CouponResponse couponResponse;
  private String createdAt;
  private String orderStatus;

  private SpecificOrderResponse() {
  }

  public SpecificOrderResponse(
      final Long orderId, final List<OrderItemResponse> products,
      final BigDecimal totalPrice, final BigDecimal deliveryFee,
      final BigDecimal totalPayments, final CouponResponse couponResponse,
      final ZonedDateTime createdAt, final String orderStatus
  ) {
    this.orderId = orderId;
    this.products = products;
    this.totalPrice = totalPrice;
    this.deliveryFee = deliveryFee;
    this.totalPayments = totalPayments;
    this.couponResponse = couponResponse;
    this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    this.orderStatus = orderStatus;
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

  public BigDecimal getTotalPayments() {
    return totalPayments;
  }

  public CouponResponse getCouponResponse() {
    return couponResponse;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public String getOrderStatus() {
    return orderStatus;
  }
}
