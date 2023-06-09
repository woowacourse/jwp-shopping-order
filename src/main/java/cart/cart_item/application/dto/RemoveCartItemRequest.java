package cart.cart_item.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RemoveCartItemRequest {

  @JsonProperty("cartItemIdList")
  private List<Long> cartItemIds;

  private RemoveCartItemRequest() {
  }

  public RemoveCartItemRequest(final List<Long> cartItemIds) {
    this.cartItemIds = cartItemIds;
  }

  public List<Long> getCartItemIds() {
    return cartItemIds;
  }
}
