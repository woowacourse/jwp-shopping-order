package cart.dto.request;

import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderPostRequest {
    @NotEmpty
    private final List<Long> cartItems;
    @NotNull
    private final Integer paymentPrice;

    public OrderPostRequest(final List<Long> cartItems, final Integer paymentPrice) {
        this.cartItems = cartItems;
        this.paymentPrice = paymentPrice;
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public Integer getPaymentPrice() {
        return paymentPrice;
    }
}
