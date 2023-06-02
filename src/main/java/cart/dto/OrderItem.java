package cart.dto;

public class OrderItem {
    private Long cartItemId;

    public OrderItem() {
    }

    public OrderItem(final Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getCartItemId() {
        return cartItemId;
    }
}
