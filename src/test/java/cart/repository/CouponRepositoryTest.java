package cart.repository;

import static cart.fixture.CouponFixture._3만원_이상_2천원_할인_쿠폰;
import static cart.fixture.CouponFixture._3만원_이상_배달비_3천원_할인_쿠폰;
import static cart.fixture.CouponFixture.쿠폰_발급;
import static cart.fixture.MemberFixture.사용자1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.coupon.Coupon;
import cart.domain.member.Member;
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

    @Test
    void 쿠폰을_저장한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));

        // when
        final Coupon savedCoupon = couponRepository.save(coupon);

        // then
        assertThat(couponRepository.findById(savedCoupon.getId())).isPresent();
    }

    @Test
    void 사용자_아이디를_입력받아_전체_쿠폰을_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon1 = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));
        final Coupon coupon2 = couponRepository.save(쿠폰_발급(_3만원_이상_배달비_3천원_할인_쿠폰, member.getId()));

        // when
        final List<Coupon> result = couponRepository.findAllByUsedAndMemberId(false, member.getId());

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(coupon1, coupon2));
    }

    @Test
    void 단일_쿠폰을_조회한다() {
        // given
        final Member member = memberRepository.save(사용자1);
        final Coupon coupon = couponRepository.save(쿠폰_발급(_3만원_이상_2천원_할인_쿠폰, member.getId()));

        // when
        final Optional<Coupon> result = couponRepository.findById(coupon.getId());

        // then
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get()).usingRecursiveComparison().isEqualTo(coupon)
        );
    }
}
