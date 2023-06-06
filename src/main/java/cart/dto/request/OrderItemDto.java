package cart.dto.request;

import javax.validation.constraints.NotNull;

public class OrderItemDto {

    @NotNull
    private Long cartItemId;

    @NotNull
    private Long quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(final Long cartItemId, final long quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public long getQuantity() {
        return quantity;
    }
}
