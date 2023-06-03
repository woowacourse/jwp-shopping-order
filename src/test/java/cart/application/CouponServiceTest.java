package cart.application;

import cart.domain.Member;
import cart.domain.Money;
import cart.domain.coupon.MemberCoupon;
import cart.dto.CouponIssueRequest;
import cart.dto.CouponResponse;
import cart.dto.CouponsResponse;
import cart.exception.CouponException;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static cart.domain.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.domain.fixture.CouponFixture.RATE_10_COUPON;
import static cart.domain.fixture.MemberFixture.MEMBER_A;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Transactional
@AutoConfigureTestDatabase
class CouponServiceTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Long rateCouponId;
    private Long amountCouponId;
    private Member member;

    @BeforeEach
    void setUp() {
        rateCouponId = couponRepository.insert(RATE_10_COUPON);
        amountCouponId = couponRepository.insert(AMOUNT_1000_COUPON);
        member = getInsertedMember();
    }

    private Member getInsertedMember() {
        memberRepository.addMember(MEMBER_A);
        return memberRepository.getMemberByEmail(MEMBER_A.getEmail());
    }

    @Test
    @DisplayName("쿠폰들 정보를 조회한다.")
    void find_all_coupon_test() {
        // given

        // when
        CouponsResponse couponResponse = couponService.getAllCoupons();

        // then
        List<Long> ids = couponResponse.getCoupons().stream()
                .map(CouponResponse::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactlyInAnyOrder(rateCouponId, amountCouponId);
    }

    @Test
    @DisplayName("사용자에게 쿠폰을 추가한다.")
    void issue_coupon_to_member_test() {
        // given

        // when
        final Long issuedCouponId = couponService.issueCouponToMember(member, new CouponIssueRequest(rateCouponId));

        // then
        final List<MemberCoupon> issuedCoupons = memberCouponRepository.findAllByMemberId(member.getId());
        final List<Long> ids = issuedCoupons.stream()
                .map(MemberCoupon::getId)
                .collect(Collectors.toList());
        assertThat(ids).containsExactlyInAnyOrder(issuedCouponId);
    }

    @Test
    @DisplayName("사용자에게 이미 가지고 있는 쿠폰을 추가한다.")
    void issue_same_coupon_to_member_test() {
        // given
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(rateCouponId);
        couponService.issueCouponToMember(member, couponIssueRequest);

        // when
        // then
        assertThatThrownBy(() -> couponService.issueCouponToMember(member, couponIssueRequest))
                .isInstanceOf(CouponException.AlreadHaveSameCouponException.class);
    }

    @Test
    @DisplayName("사용한 쿠폰은 재발급 받을 수 있다.")
    void issue_used_coupon_to_member_test() {
        // given
        CouponIssueRequest couponIssueRequest = new CouponIssueRequest(rateCouponId);
        Long issuedId = couponService.issueCouponToMember(member, couponIssueRequest);
        MemberCoupon issuedCoupon = memberCouponRepository.findByIdForUpdate(issuedId);


        // when
        issuedCoupon.use(new Money(100000));
        memberCouponRepository.updateCouponStatus(issuedCoupon);

        // then
        assertThatNoException().isThrownBy(() -> couponService.issueCouponToMember(member, couponIssueRequest));
    }
}
