package cart.dto.cartItem;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartItemRequest {

    @JsonProperty("productId")
    private Long id;

    private CartItemRequest() {
    }

    public CartItemRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
