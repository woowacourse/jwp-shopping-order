package cart.application.dto;

public class PatchCartItemQuantityUpdateRequest {
    private int quantity;

    public PatchCartItemQuantityUpdateRequest() {
    }

    public PatchCartItemQuantityUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
