package cart.dto.response;

public class CartItemUpdateResponse {
    private final int quantity;
    private final boolean checked;

    public CartItemUpdateResponse(final int quantity, final boolean checked) {
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
