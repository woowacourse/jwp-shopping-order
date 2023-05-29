package cart.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.MemberCouponDao;
import cart.domain.coupon.AmountDiscountPolicy;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.DeliveryFeeDiscountPolicy;
import cart.domain.coupon.MinimumPriceDiscountCondition;
import cart.domain.coupon.NoneDiscountCondition;
import cart.domain.member.Member;
import cart.entity.MemberCouponEntity;
import cart.test.RepositoryTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@RepositoryTest
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberCouponDao memberCouponDao;

    @Test
    void 쿠폰을_저장한다() {
        // given
        final Coupon coupon = new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        );

        // when
        final Coupon savedCoupon = couponRepository.save(coupon);

        // then
        assertThat(couponRepository.findById(savedCoupon.getId())).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_전체_쿠폰을_조회한다() {
        // given
        final Coupon coupon1 = couponRepository.save(new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Coupon coupon2 = couponRepository.save(new Coupon(
                "배달비_할인_쿠폰",
                new DeliveryFeeDiscountPolicy(),
                new MinimumPriceDiscountCondition(30000)
        ));
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        memberCouponDao.insert(new MemberCouponEntity(member.getId(), coupon1.getId(), false));
        memberCouponDao.insert(new MemberCouponEntity(member.getId(), coupon2.getId(), false));

        // when
        final List<Coupon> result = couponRepository.findAllByMemberId(member.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(coupon1, coupon2));
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final Coupon coupon = couponRepository.save(new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));

        // when
        final Optional<Coupon> result = couponRepository.findById(coupon.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(coupon)
        );
    }

    @Test
    void 단일_쿠폰을_사용자_아이디와_함께_조회한다() {
        // given
        final Coupon coupon = couponRepository.save(new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));
        memberCouponDao.insert(new MemberCouponEntity(member.getId(), coupon.getId(), false));

        // when
        final Optional<Coupon> result = couponRepository.findByIdAndMemberId(coupon.getId(), member.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(coupon)
        );
    }

    @Test
    void 단일_쿠폰을_사용자_아이디와_함께_조회할_때_올바른_쿠폰이_아니면_빈값을_반환한다() {
        // given
        final Coupon coupon = couponRepository.save(new Coupon(
                "30000원 이상 2000원 할인 쿠폰",
                new AmountDiscountPolicy(2000L),
                new NoneDiscountCondition()
        ));
        final Member member = memberRepository.save(new Member("pizza1@pizza.com", "password"));

        // when
        final Optional<Coupon> result = couponRepository.findByIdAndMemberId(coupon.getId(), member.getId());

        // then
        assertThat(result).isNotPresent();
    }
}
