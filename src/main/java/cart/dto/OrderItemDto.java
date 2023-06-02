package cart.dto;

public class OrderItemDto {

    private Long cartItemId;

    public OrderItemDto() {
    }

    public OrderItemDto(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}
