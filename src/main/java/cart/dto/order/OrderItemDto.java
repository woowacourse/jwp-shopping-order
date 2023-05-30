package cart.dto.order;

public class OrderItemDto {

    private Long cartItemId;
    private int quantity;

    public OrderItemDto() {
    }

    public OrderItemDto(Long cartItemId, int quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
