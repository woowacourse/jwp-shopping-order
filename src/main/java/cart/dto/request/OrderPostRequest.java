package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderPostRequest {
    private final List<Long> cartItems;
    @JsonProperty("paymentAmount")
    private final Integer finalPrice;

    public OrderPostRequest(final List<Long> cartItems, final Integer finalPrice) {
        this.cartItems = cartItems;
        this.finalPrice = finalPrice;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public Integer getFinalPrice() {
        return finalPrice;
    }
}
