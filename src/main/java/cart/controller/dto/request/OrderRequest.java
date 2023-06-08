package cart.controller.dto.request;

import cart.exception.EmptyCartItemsException;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {

    private List<Long> cartItems;
    @NotNull(message = "paymentAmount 가 null 입니다.")
    private Integer paymentAmount;

    public OrderRequest() {
    }

    public OrderRequest(final List<Long> cartItems, final int paymentAmount) {
        this.cartItems = cartItems;
        this.paymentAmount = paymentAmount;
    }

    private void validateCartItemsNotEmpty(final List<Long> cartItems) {
        if (cartItems.isEmpty()) {
            throw new EmptyCartItemsException();
        }
    }

    public List<Long> getCartItems() {
        return cartItems;
    }

    public int getPaymentAmount() {
        return paymentAmount;
    }
}
