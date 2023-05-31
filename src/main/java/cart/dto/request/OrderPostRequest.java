package cart.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class OrderPostRequest {
    private final List<Long> cartItems;
    @JsonProperty("totalPrice")
    private final int finalPrice;

    public OrderPostRequest(final List<Long> cartItems, final int finalPrice) {
        this.cartItems = cartItems;
        this.finalPrice = finalPrice;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public int getFinalPrice() {
        return finalPrice;
    }
}
