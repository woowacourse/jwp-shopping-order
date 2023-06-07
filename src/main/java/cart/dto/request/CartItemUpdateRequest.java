package cart.dto.request;

public class CartItemUpdateRequest {
    private final int quantity;
    private final boolean checked;

    public CartItemUpdateRequest(final int quantity, final boolean checked) {
        this.quantity = quantity;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public int getQuantity() {
        return quantity;
    }
}
