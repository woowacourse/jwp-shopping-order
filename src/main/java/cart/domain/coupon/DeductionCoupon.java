package cart.domain.coupon;

import cart.domain.Order;

public class DeductionCoupon extends Coupon {
    public DeductionCoupon(Long id, String name, Integer discountAmount) {
        super(id, name, DiscountType.DEDUCTION, 0, discountAmount);
    }

    public DeductionCoupon(Long id, String name, Integer discountAmount, Integer minimumPrice) {
        super(id, name, DiscountType.DEDUCTION, 0, discountAmount, minimumPrice);
    }

    @Override
    public int discount(Order order) {
        return order.getOriginalPrice() - getDiscountAmount();
    }
}
