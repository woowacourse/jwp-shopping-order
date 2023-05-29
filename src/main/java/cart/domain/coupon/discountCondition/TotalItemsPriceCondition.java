package cart.domain.coupon.discountCondition;

import cart.domain.TotalPrice;

public class TotalItemsPriceCondition implements DiscountCondition {

    private final long minimumPrice;

    public TotalItemsPriceCondition(final long minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    @Override
    public boolean isCondition(final TotalPrice totalPrice) {
        return totalPrice.getOrderPrice() >= minimumPrice;
    }

    @Override
    public long getMinimumPrice() {
        return minimumPrice;
    }
}
