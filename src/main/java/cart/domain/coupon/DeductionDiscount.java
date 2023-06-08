package cart.domain.coupon;


import cart.exception.CouponException;

public class DeductionDiscount implements CouponTypes {
    private static final int MINIMUM = 0;

    @Override
    public int calculatePrice(int totalPrice, int minimumPrice, int discountPrice, double discountRate) {
        if (totalPrice < minimumPrice || totalPrice - discountPrice < MINIMUM) {
            throw new CouponException("사용할 수 없는 쿠폰 범위 입니다.");
        }
        return totalPrice - discountPrice;
    }

    @Override
    public String getCouponTypeName() {
        return DiscountType.DEDUCTION_DISCOUNT.getTypeName();
    }
}
