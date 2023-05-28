package cart.dto;

public class CartItemCreatedResponse {
    private final int quantity;
    private final boolean checked;

    public CartItemCreatedResponse(final int quantity, final boolean checked) {
        this.quantity = quantity;
        this.checked = checked;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isChecked() {
        return checked;
    }
}
