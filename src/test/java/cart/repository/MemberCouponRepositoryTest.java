package cart.repository;

import static cart.TestDataFixture.DISCOUNT_50_PERCENT;
import static cart.TestDataFixture.MEMBER_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import cart.RepositoryTest;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.exception.MemberCouponNotFoundException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepositoryTest
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private CouponRepository couponRepository;

    @DisplayName("멤버가 갖고 있는 모든 멤버쿠폰을 조회한다.")
    @Test
    void findByMemberId() {
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(savedCoupon, MEMBER_1.getId());
        memberCouponRepository.insert(memberCoupon);

        final List<MemberCoupon> coupons = memberCouponRepository.findByMemberId(MEMBER_1.getId());

        assertThat(coupons)
                .extracting(MemberCoupon::getCoupon)
                .containsExactly(savedCoupon);
    }

    @DisplayName("id로 멤버쿠폰을 조회한다.")
    @Test
    void findById() {
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(savedCoupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        final MemberCoupon findMemberCoupon = memberCouponRepository.findById(savedMemberCoupon.getId());

        assertThat(findMemberCoupon).isEqualTo(savedMemberCoupon);
    }

    @DisplayName("멤버쿠폰을 저장한다.")
    @Test
    void insert() {
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(savedCoupon, MEMBER_1.getId());

        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        assertThat(savedMemberCoupon)
                .extracting(MemberCoupon::getCoupon, MemberCoupon::getMemberId)
                .containsExactly(savedCoupon, memberCoupon.getMemberId());
    }

    @DisplayName("멤버쿠폰을 사용상태로 변경한다.")
    @Test
    void useMemberCoupon() {
        final Coupon savedCoupon = couponRepository.insert(DISCOUNT_50_PERCENT);
        final MemberCoupon memberCoupon = new MemberCoupon(savedCoupon, MEMBER_1.getId());
        final MemberCoupon savedMemberCoupon = memberCouponRepository.insert(memberCoupon);

        memberCouponRepository.useMemberCoupon(savedMemberCoupon);

        final Long memberCouponId = savedMemberCoupon.getId();
        assertThatThrownBy(() -> memberCouponRepository.findById(memberCouponId))
                .isInstanceOf(MemberCouponNotFoundException.class);
    }
}
