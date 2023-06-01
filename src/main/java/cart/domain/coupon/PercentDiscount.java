package cart.domain.coupon;

import cart.exception.CouponException;

public class PercentDiscount implements CouponTypes {

    @Override
    public int calculatePrice(int totalPrice, int minimumPrice, int discountPrice, double discountRate) {
        if (totalPrice < minimumPrice) {
            throw new CouponException("사용할 수 없는 쿠폰 범위 입니다.");
        }
        return (int) (totalPrice * (1 - discountRate));
    }


    @Override
    public String getCouponTypeName() {
        return DiscountType.percentDiscount.getTypeName();
    }


}
