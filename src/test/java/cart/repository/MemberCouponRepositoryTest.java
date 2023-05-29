package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.cart.MemberCoupon;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.NoneDiscountCondition;
import cart.domain.member.Member;
import cart.test.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class MemberCouponRepositoryTest {

    @Autowired
    private MemberCouponRepository memberCouponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void 모든_사용자_쿠폰을_저장한다() {
        // given
        final Coupon coupon = couponRepository.save(new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Member member1 = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        final Member member2 = memberRepository.save(new Member("pizza2@pizza.com", "password"));

        // when
        memberCouponRepository.saveAll(List.of(
                new MemberCoupon(member1, coupon),
                new MemberCoupon(member2, coupon)
        ));

        // then
        assertAll(
                () -> assertThat(couponRepository.findAllByMemberId(member1.getId())).hasSize(1),
                () -> assertThat(couponRepository.findAllByMemberId(member2.getId())).hasSize(1)
        );
    }
}
