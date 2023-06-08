package fixture;

import static fixture.CouponFixture.정액_할인_쿠폰;
import static fixture.CouponFixture.할인율_쿠폰;
import static fixture.MemberFixture.유저_1;
import static fixture.MemberFixture.유저_2;

import cart.domain.MemberCoupon;

public class MemberCouponFixture {

    public static final MemberCoupon 쿠폰_유저_1_정액_할인_쿠폰 = new MemberCoupon(1L, 유저_1, 정액_할인_쿠폰);
    public static final MemberCoupon 쿠폰_유저_1_할인율_쿠폰 = new MemberCoupon(2L, 유저_1, 할인율_쿠폰);
    public static final MemberCoupon 쿠폰_유저_2_정액_할인_쿠폰 = new MemberCoupon(3L, 유저_2, 정액_할인_쿠폰);
    public static final MemberCoupon 쿠폰_유저_2_할인율_쿠폰 = new MemberCoupon(4L, 유저_2, 할인율_쿠폰);

}
