package cart.order.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

public class RegisterOrderRequest {

  @JsonProperty("cartItemIdList")
  private List<Long> cartItemIds;
  private BigDecimal totalPrice;
  private BigDecimal deliveryFee;

  private RegisterOrderRequest() {
  }

  public RegisterOrderRequest(final List<Long> cartItemIds, final BigDecimal totalPrice,
      final BigDecimal deliveryFee) {
    this.cartItemIds = cartItemIds;
    this.totalPrice = totalPrice;
    this.deliveryFee = deliveryFee;
  }

  public List<Long> getCartItemIds() {
    return cartItemIds;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public BigDecimal getDeliveryFee() {
    return deliveryFee;
  }
}
