package cart.fixture;

import cart.domain.coupon.CouponType;
import cart.persistence.entity.CouponEntity;

public class CouponFixture {

    public static CouponEntity 금액쿠폰_10000원이상_1000할인_엔티티 = new CouponEntity("10% 할인쿠폰", 10000, 1000, CouponType.AMOUNT,
            1000,
            null);
}
