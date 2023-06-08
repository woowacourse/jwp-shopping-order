package cart.controller.request;

public class CartItemQuantityUpdateRequestDto {
    private int quantity;

    private CartItemQuantityUpdateRequestDto() {
    }

    public CartItemQuantityUpdateRequestDto(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

}
