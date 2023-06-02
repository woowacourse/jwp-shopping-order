package cart.repository;

import cart.dao.CouponDao;
import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import cart.domain.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@JdbcTest
@Import({CouponJdbcRepository.class, CouponDao.class})
class CouponJdbcRepositoryTest {

    @Autowired
    private CouponJdbcRepository couponJdbcRepository;

    @Test
    void 쿠폰을_발급한다() {
        // given
        final Member member = new Member(1L, "a@a.com", "1234");

        // when
        final Long saveId = couponJdbcRepository.issue(member, 1L);

        // then
        assertThat(saveId).isNotNull();
    }

    @Test
    void 쿠폰_상태를_변경한다 () {
        // when, then
        assertThatCode(() -> couponJdbcRepository.changeStatusTo(1L, false))
                .doesNotThrowAnyException();
    }

    @Test
    void 회원이_소유한_쿠폰을_반환한다() {
        // when
        final Coupons coupons = couponJdbcRepository.findCouponsByMemberId(1L);

        // then
        final List<Coupon> results = coupons.getCoupons();
        assertAll(
                () -> assertThat(results).hasSize(3),
                () -> {
                    final List<Integer> discountAmounts = results.stream()
                            .map(it -> it.getDiscountAmount().getDiscountAmount())
                            .collect(Collectors.toList());
                    assertThat(discountAmounts).containsExactly(1000, 3000, 5000);
                }
        );
    }

    @Test
    void 전체_쿠폰을_반환한다() {
        // when
        final Coupons coupons = couponJdbcRepository.findCouponAll();

        // then
        final List<Coupon> results = coupons.getCoupons();
        assertAll(
                () -> assertThat(results).hasSize(4),
                () -> {
                    final List<Integer> discountAmounts = results.stream()
                            .map(it -> it.getDiscountAmount().getDiscountAmount())
                            .collect(Collectors.toList());
                    assertThat(discountAmounts).containsExactly(1000, 3000, 5000, 10000);
                }
        );
    }

    @Test
    void 쿠폰을_삭제한다() {
        // when
        couponJdbcRepository.deleteCoupon(2L);

        // then
        assertThatThrownBy(() -> couponJdbcRepository.findCouponById(2L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
