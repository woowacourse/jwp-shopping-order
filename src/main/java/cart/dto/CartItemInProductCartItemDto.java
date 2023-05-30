package cart.dto;

public class CartItemInProductCartItemDto {

    private Long id;
    private int quantity;

    public CartItemInProductCartItemDto() {
    }

    public CartItemInProductCartItemDto(final Long id, final int quantity) {
        this.id = id;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }
}
