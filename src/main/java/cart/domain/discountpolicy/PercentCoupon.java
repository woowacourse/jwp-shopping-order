package cart.domain.discountpolicy;

public class PercentCoupon implements CouponPolicy{
    private final int minAmount;
    private final int discountPercent;

    public PercentCoupon(final int minAmount, final int discountPercent) {
        this.minAmount = minAmount;
        this.discountPercent = discountPercent;
    }

    @Override
    public int applyDiscount(final int totalPrice) {
        if (totalPrice >= minAmount) {
            return  (int) (totalPrice * (discountPercent / 100.0));
        }
        return 0;
    }
}
