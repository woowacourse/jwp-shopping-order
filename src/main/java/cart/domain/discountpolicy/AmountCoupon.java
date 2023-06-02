package cart.domain.discountpolicy;

public class AmountCoupon implements CouponPolicy{
    private final int minAmount;
    private final int discountAmount;

    public AmountCoupon(final int minAmount, final int discountAmount) {
        this.minAmount = minAmount;
        this.discountAmount = discountAmount;
    }

    @Override
    public int applyDiscount(final int totalPrice) {
        if (totalPrice >= minAmount) {
            return discountAmount;
        }
        return 0;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getDiscountAmount() {
        return discountAmount;
    }
}
