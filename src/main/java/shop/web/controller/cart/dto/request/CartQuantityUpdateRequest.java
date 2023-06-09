package shop.web.controller.cart.dto.request;

public class CartQuantityUpdateRequest {
    private int quantity;

    private CartQuantityUpdateRequest() {
    }

    public CartQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
