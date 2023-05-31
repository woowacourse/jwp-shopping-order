package cart.dto.order;

public class OrderItemDto {
    private Long cartItemId;
    private Long quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(final Long cartItemId, final Long quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
