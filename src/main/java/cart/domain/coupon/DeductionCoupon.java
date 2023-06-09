package cart.domain.coupon;

public class DeductionCoupon extends Coupon {
    public DeductionCoupon(Long id, String name, Integer discountAmount) {
        super(id, name, DiscountType.DEDUCTION, 0.0, discountAmount);
    }

    public DeductionCoupon(Long id, String name, Integer discountAmount, Integer minimumPrice) {
        super(id, name, DiscountType.DEDUCTION, 0.0, discountAmount, minimumPrice);
    }

    @Override
    public int discount(int money) {
        return money - getDiscountAmount();
    }
}
