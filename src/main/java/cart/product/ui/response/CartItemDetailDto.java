package cart.product.ui.response;

public class CartItemDetailDto {

    private Long id;
    private int quantity;

    public CartItemDetailDto(final Long id, final int quantity) {
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
