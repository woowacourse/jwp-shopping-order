package cart.repository;

import cart.domain.Money;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.discountPolicy.PricePolicy;
import cart.domain.member.Member;
import cart.domain.member.MemberCoupon;
import cart.test.RepositoryTest;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@RepositoryTest
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;
    @Autowired
    private CouponRepository couponRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 사용자_쿠폰을_저장한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "pizza"));
        final Coupon coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), BigDecimal.valueOf(3000L), new Money(30000L)));
        final MemberCoupon memberCoupon = new MemberCoupon(member, coupon, false);

        // when
        memberCouponRepository.save(memberCoupon);

        // then
        final MemberCoupon saved = memberCouponRepository.findById(1L).get();
        assertAll(
                () -> assertThat(saved.getCoupon()).isEqualTo(coupon),
                () -> assertThat(saved.getMember()).isEqualTo(member),
                () -> assertThat(saved.isUsed()).isFalse()
        );
    }

    @Test
    void 사용자_쿠폰을_조회한다() {
        // given
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "pizza"));
        final Coupon coupon = couponRepository.save(new Coupon("30000원 이상 3000원 할인 쿠폰", new PricePolicy(), BigDecimal.valueOf(3000L), new Money(30000L)));
        final MemberCoupon memberCoupon = new MemberCoupon(member, coupon, false);
        final MemberCoupon save = memberCouponRepository.save(memberCoupon);

        // when
        final MemberCoupon saved = memberCouponRepository.findById(save.getId()).get();

        // then
        assertAll(
                () -> assertThat(saved.getCoupon()).isEqualTo(coupon),
                () -> assertThat(saved.getMember()).isEqualTo(member),
                () -> assertThat(saved.isUsed()).isFalse()
        );
    }
}