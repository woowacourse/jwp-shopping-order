package cart.repository;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.MemberCoupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;
import java.util.stream.Collectors;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.MemberFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ComponentScan(basePackageClasses = {MemberCouponRepository.class, DiscountPolicyProvider.class})
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Coupon insertedCoupon;
    private Member insertedMember;

    @BeforeEach
    void setup() {
        insertedCoupon = getCouponInRepository();
        insertedMember = getInsertedMember();
    }

    private Member getInsertedMember() {
        memberRepository.addMember(MEMBER_A);
        return memberRepository.getMemberByEmail(MEMBER_A.getEmail());
    }

    private Coupon getCouponInRepository() {
        Long couponId = couponRepository.insert(AMOUNT_1000_COUPON);
        return couponRepository.findById(couponId);
    }

    @Test
    @DisplayName("사용자 쿠폰을 조회한다")
    void inser_member_coupon_test() {
        // given
        Long insertedId = memberCouponRepository.insert(insertedMember, insertedCoupon);

        // when
        final List<MemberCoupon> allByMemberId = memberCouponRepository.findAllByMemberId(insertedMember.getId());

        // then
        final List<Long> ids = allByMemberId.stream()
                .map(MemberCoupon::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactlyInAnyOrder(insertedId);
    }

    @Test
    @DisplayName("쿠폰 사용 정보를 업데이트 한다")
    void change_coupon_status_test() {
        // given
        Long insertedId = memberCouponRepository.insert(insertedMember, insertedCoupon);
        MemberCoupon insertedMemberCoupon = memberCouponRepository.findByIdForUpdate(insertedId);

        // when
        insertedMemberCoupon.use(new Money(100000));
        memberCouponRepository.updateCouponStatus(insertedMemberCoupon);

        // then
        MemberCoupon updatedCoupon = memberCouponRepository.findByIdForUpdate(insertedId);
        assertThat(updatedCoupon.isUsed()).isTrue();
    }

}
