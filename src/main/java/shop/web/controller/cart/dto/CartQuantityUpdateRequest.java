package shop.web.controller.cart.dto;

public class CartQuantityUpdateRequest {
    private int quantity;

    public CartQuantityUpdateRequest() {
    }

    public CartQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
