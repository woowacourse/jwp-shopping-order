package cart.service;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.dto.MemberCouponResponse;
import cart.repository.CouponRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Transactional
@SpringBootTest
class MemberCouponServiceTest {

    @Autowired
    private MemberCouponService memberCouponService;

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 사용자_아이디에_해당하는_사용자_쿠폰을_조회한다() {
        // given
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password"));
        final Coupon coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), 3000L, new Money(30000L)));
        memberCouponRepository.save(new MemberCoupon(member1, coupon, false));
        memberCouponRepository.save(new MemberCoupon(member1, coupon, false));
        memberCouponRepository.save(new MemberCoupon(member2, coupon, false));

        // when
        final List<MemberCouponResponse> result = memberCouponService.findAllByMemberId(member1.getId());

        // then
        assertThat(result.size()).isEqualTo(2);
    }
}