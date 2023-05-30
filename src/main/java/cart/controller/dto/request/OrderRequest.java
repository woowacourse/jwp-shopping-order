package cart.controller.dto.request;

import java.util.List;

public class OrderRequest {

    private List<Long> cartItems;
    private int paymentAmount;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItems, final int paymentAmount) {
        this.cartItems = cartItems;
        this.paymentAmount = paymentAmount;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }
}
