package cart.fixture;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.CouponInfo;
import cart.domain.coupon.CouponType;
import cart.persistence.entity.CouponEntity;

public class CouponFixture {

    public static class 금액_10000원이상_1000원할인 {

        private static final String NAME = "1000원 할인쿠폰";
        private static final int MIN_ORDER_PRICE = 10_000;
        private static final int MAX_DISCOUNT_PRICE = 10_000;
        private static final CouponType TYPE = CouponType.FIXED_AMOUNT;
        private static final Integer DISCOUNT_AMOUNT = 1000;
        private static final Double DISCOUNT_PERCENT = null;

        public static final CouponEntity ENTITY = new CouponEntity(NAME, MIN_ORDER_PRICE,
                MAX_DISCOUNT_PRICE, TYPE, DISCOUNT_AMOUNT, DISCOUNT_PERCENT);
        public static final CouponInfo COUPON_INFO = new CouponInfo(NAME, MIN_ORDER_PRICE, MAX_DISCOUNT_PRICE);
        public static final Coupon COUPON = new Coupon(COUPON_INFO, DISCOUNT_AMOUNT, TYPE);
    }
}
