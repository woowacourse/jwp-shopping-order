package cart.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    @NotNull(message = "cartItems는 null이 불가능합니다")
    private final List<Long> cartItems;
    @NotNull(message = "paymentAmount는 null이 불가능합니다")
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
