package fixture;

import static fixture.CouponFixture.COUPON_1_NOT_NULL_PRICE;
import static fixture.CouponFixture.COUPON_2_NOT_NULL_RATE;
import static fixture.MemberFixture.MEMBER_1;
import static fixture.MemberFixture.MEMBER_2;

import cart.domain.MemberCoupon;

public class MemberCouponFixture {

    public static final MemberCoupon MEMBER_COUPON_1 = new MemberCoupon(1L, MEMBER_1, COUPON_1_NOT_NULL_PRICE);
    public static final MemberCoupon MEMBER_COUPON_2 = new MemberCoupon(2L, MEMBER_1, COUPON_2_NOT_NULL_RATE);
    public static final MemberCoupon MEMBER_COUPON_3 = new MemberCoupon(3L, MEMBER_2, COUPON_1_NOT_NULL_PRICE);
    public static final MemberCoupon MEMBER_COUPON_4 = new MemberCoupon(4L, MEMBER_2, COUPON_2_NOT_NULL_RATE);

}
