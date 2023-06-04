package cart.dto;

import java.util.Optional;
import javax.validation.constraints.Positive;
import org.springframework.lang.Nullable;

public class CartItemRequest {

    @Positive
    private Long productId;

    @Nullable
    private Integer quantity;

    private CartItemRequest() {
    }

    public CartItemRequest(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public Optional<Integer> getQuantity() {
        return Optional.ofNullable(quantity);
    }

}
