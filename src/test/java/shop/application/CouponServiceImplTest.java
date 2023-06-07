package shop.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import shop.application.coupon.CouponService;
import shop.application.member.dto.MemberCouponDto;
import shop.domain.coupon.CouponType;
import shop.domain.member.Member;
import shop.domain.repository.MemberRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CouponServiceImplTest extends ServiceTest {
    private Long memberId;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponService couponService;

    @BeforeEach
    void setUp() {
        memberId = memberRepository.save(ServiceTestFixture.member);
    }

    @DisplayName("쿠폰을 발급할 수 있다.")
    @Test
    void issueCouponTest() {
        //given
        Member findMember = memberRepository.findByName(ServiceTestFixture.member.getName());
        //when
        couponService.issueCoupon(memberId, CouponType.WELCOME_JOIN);

        //then
        List<MemberCouponDto> memberCoupons = couponService.getAllCouponsOfMember(findMember);
        assertThat(memberCoupons.size()).isEqualTo(1);
        MemberCouponDto memberCoupon = memberCoupons.get(0);
        assertThat(memberCoupon.getName()).isEqualTo(CouponType.WELCOME_JOIN.getName());
        assertThat(memberCoupon.getDiscountRate()).isEqualTo(CouponType.WELCOME_JOIN.getDiscountRate());
    }
}
