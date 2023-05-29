package cart.domain.coupon.discountCondition;

import cart.domain.OrderPrice;

public class TotalItemsPriceCondition implements DiscountCondition {

    private final long minimumPrice;

    public TotalItemsPriceCondition(final long minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    @Override
    public boolean isCondition(final OrderPrice orderPrice) {
        return orderPrice.getTotalItemsPrice() >= minimumPrice;
    }

    @Override
    public long getMinimumPrice() {
        return minimumPrice;
    }
}
