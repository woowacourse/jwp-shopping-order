package cart.domain.discountpolicy;

public class PercentCoupon implements CouponPolicy {
    private final int minAmount;
    private final int discountPercent;

    public PercentCoupon(int minAmount, int discountPercent) {
        this.minAmount = minAmount;
        this.discountPercent = discountPercent;
    }

    @Override
    public int applyDiscount(int totalPrice) {
        if (totalPrice >= minAmount) {
            return (int) (totalPrice * (discountPercent / 100.0));
        }
        return 0;
    }
}
