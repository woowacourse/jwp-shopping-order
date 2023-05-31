package cart.domain.coupon;

public class PercentCoupon extends Coupon {

    public PercentCoupon(Long id, String name, Double discountPercent) {
        super(id, name, DiscountType.PERCENTAGE, discountPercent, 0);
    }

    public PercentCoupon(Long id, String name, Double discountPercent, Integer minimumPrice) {
        super(id, name, DiscountType.PERCENTAGE, discountPercent, 0, minimumPrice);
    }

    @Override
    public int discount(int money) {
        return money - (int) (money * getDiscountPercent());
    }
}
