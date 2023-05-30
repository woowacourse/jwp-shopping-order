package cart.repository;

import cart.dao.CouponDao;
import cart.domain.Coupon;
import cart.domain.Coupons;
import cart.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
        assertThatCode(() -> couponJdbcRepository.changeStatus(1L, 1L))
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
                            .map(Coupon::getDiscountAmount)
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
                            .map(Coupon::getDiscountAmount)
                            .collect(Collectors.toList());
                    assertThat(discountAmounts).containsExactly(1000, 3000, 5000, 10000);
                }
        );
    }
}
