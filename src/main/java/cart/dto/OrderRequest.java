package cart.dto;

import java.util.List;

public class OrderRequest {

    private final List<Long> cartItems;
    private final Long paymentAmount;

    public OrderRequest(List<Long> cartItems, Long paymentAmount) {
        this.cartItems = cartItems;
        this.paymentAmount = paymentAmount;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public Long getPaymentAmount() {
        return paymentAmount;
    }
}
