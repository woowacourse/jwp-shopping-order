package cart.controller.request;

import java.util.Optional;
import javax.validation.constraints.Positive;
import org.springframework.lang.Nullable;

public class CartItemRequestDto {

    @Positive
    private Long productId;

    @Nullable
    private Integer quantity;

    private CartItemRequestDto() {
    }

    public CartItemRequestDto(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }

    public Optional<Integer> getQuantity() {
        return Optional.ofNullable(quantity);
    }

}
