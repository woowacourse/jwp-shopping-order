package cart.application.event;

import java.util.List;

import cart.domain.QuantityAndProduct;

public class CartItemDeleteEvent {

    private final long memberId;
    private final List<QuantityAndProduct> quantityAndProducts;

    public CartItemDeleteEvent(long memberId, List<QuantityAndProduct> quantityAndProducts) {
        this.memberId = memberId;
        this.quantityAndProducts = quantityAndProducts;
    }

    public long getMemberId() {
        return memberId;
    }

    public List<QuantityAndProduct> getQuantityAndProducts() {
        return quantityAndProducts;
    }
}
