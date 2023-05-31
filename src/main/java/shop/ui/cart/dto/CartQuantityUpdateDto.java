package shop.ui.cart.dto;

public class CartQuantityUpdateDto {
    private int quantity;

    public CartQuantityUpdateDto() {
    }

    public CartQuantityUpdateDto(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
