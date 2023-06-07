package cart.fixture;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.discount.PolicyDiscount;
import cart.domain.discount.PolicyPercentage;

import java.util.List;

public class CouponFixture {

    public static Coupon createDiscountCoupon() {
        return new Coupon(1L, "1000원 할인 쿠폰", new PolicyDiscount(1000));
    }

    public static Coupon createPercentageCoupon() {
        return new Coupon(2L, "10% 할인 쿠폰", new PolicyPercentage(10));
    }

    public static Coupons createCoupons() {
        return new Coupons(List.of(createDiscountCoupon(), createPercentageCoupon()));
    }

    public static Coupons createCouponsWithDeliveryFree() {
        return new Coupons(List.of(createDiscountCoupon(), createPercentageCoupon(), createDeliveryCoupon()));
    }

    public static Coupon createDeliveryCoupon() {
        return new Coupon(3L, "DELIVERY_전액", new PolicyDiscount(3000));
    }
}
