package cart.cartItem.application.dto;

public class CartItemQuantityUpdateDto {

    private int quantity;

    public CartItemQuantityUpdateDto(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
