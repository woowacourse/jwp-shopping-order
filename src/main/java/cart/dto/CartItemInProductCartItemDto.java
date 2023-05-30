package cart.dto;

public class CartItemInProductCartItemDto {

    private Long id;
    private int quantity;

    private CartItemInProductCartItemDto(final Long id, final int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public static CartItemInProductCartItemDto of(final Long id, final int quantity) {
        return new CartItemInProductCartItemDto(id, quantity);
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
