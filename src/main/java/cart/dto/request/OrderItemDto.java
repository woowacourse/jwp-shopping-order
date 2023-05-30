package cart.dto.request;

public class OrderItemDto {

    private Long cartItemId;
    private long quantity;

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
