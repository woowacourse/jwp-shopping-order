package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderPostRequest {
    @NotEmpty
    @JsonProperty("cartItems")
    private final List<Long> cartItemIds;
    @NotNull
    private final Integer paymentPrice;

    public OrderPostRequest(final List<Long> cartItemIds, final Integer paymentPrice) {
        this.cartItemIds = cartItemIds;
        this.paymentPrice = paymentPrice;
    }

    public List<Long> getCartItemIds() {
        return cartItemIds;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }
}
