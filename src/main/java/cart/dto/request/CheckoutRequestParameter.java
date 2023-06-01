package cart.dto.request;

import java.util.List;

public class CheckoutRequestParameter {

    private final List<Long> checkoutIds;

    public CheckoutRequestParameter(final List<Long> checkoutIds) {
        this.checkoutIds = checkoutIds;
    }

    public List<Long> getCheckoutIds() {
        return checkoutIds;
    }
}
