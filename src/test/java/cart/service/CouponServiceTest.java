package cart.service;

import static cart.TestDataFixture.DISCOUNT_50_PERCENT;
import static cart.TestDataFixture.MEMBER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.RepositoryTest;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.service.response.CouponResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

@RepositoryTest
@Import(CouponService.class)
class CouponServiceTest {

    @Autowired
    private CouponService couponService;
    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private CouponRepository couponRepository;

    @DisplayName("멤버가 가진 쿠폰들을 조회하는 기능 테스트")
    @Test
    void findMemberCoupons() {
        final Coupon coupon = couponRepository.insert(DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(coupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        final List<CouponResponse> memberCoupons = couponService.findMemberCoupons(MEMBER_1);

        assertThat(memberCoupons)
                .extracting(CouponResponse::getId, CouponResponse::getName)
                .containsExactly(
                        tuple(savedMemberCoupon.getId(), coupon.getName())
                );
    }
}
