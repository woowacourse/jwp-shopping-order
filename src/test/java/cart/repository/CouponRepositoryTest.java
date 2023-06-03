package cart.repository;

import cart.domain.Coupon;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JdbcTest
class CouponRepositoryTest extends RepositoryTest {
    private final CouponRepository couponRepository;

    @Autowired
    private CouponRepositoryTest(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
        this.couponRepository = new CouponRepository(couponDao, ordersCouponDao, memberCouponDao);
    }

    @Test
    @DisplayName("쿠폰을 발행한다.")
    void issueCoupon() {
        couponRepository.issueCoupon(2L, 1L);
        Assertions.assertThat(memberCouponDao.findByMemberId(2L).get(0)).isEqualTo(1L);
    }


    @Test
    @DisplayName("모든 쿠폰을 찾는다")
    void findAllCoupons() {
        Assertions.assertThat(couponRepository.findAllCoupons(1L)).hasSize(6);
    }

    @Test
    @DisplayName("모든 쿠폰중 사용자가 발급 가능한 쿠폰인지 확인한다.")
    void findAllCouponsTest() {
        Map<Coupon, Boolean> couponMap = couponRepository.findAllCoupons(1L);
        List<Boolean> issuable = couponMap.values().stream().collect(Collectors.toList());
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(issuable.get(0)).isFalse();
            softly.assertThat(issuable.get(1)).isFalse();
            softly.assertThat(issuable.get(2)).isFalse();
            softly.assertThat(issuable.get(3)).isTrue();
            softly.assertThat(issuable.get(4)).isTrue();
            softly.assertThat(issuable.get(5)).isTrue();
        });
    }

    @Test
    @DisplayName("사용자 id로 쿠폰을 찾는다.")
    void findCouponsByMemberId() {
        Assertions.assertThat(couponRepository.findCouponsByMemberId(1L)).hasSize(3);
    }


    @Test
    @DisplayName("주문 내역의 쿠폰을 찾는다.")
    void findByOrdersId() {
        Assertions.assertThat(couponRepository.findCouponsByMemberId(1L).get(0).getName()).isEqualTo("10% 할인");
    }

    @Test
    @DisplayName("주문 내역의 쿠폰을 추가한다.")
    void addOrdersCoupon() {
        couponRepository.addOrdersCoupon(1L, List.of(4L));
        Assertions.assertThat(ordersCouponDao.finAllByOrdersId(1L)).hasSize(2);
    }

    @Test
    @DisplayName("사용자에게서 쿠폰을 회수한다")
    void withDrawCouponWithId() {
        couponRepository.withDrawCouponWithId(1L, couponRepository.findById(1L));
        Assertions.assertThat(memberCouponDao.findByMemberId(1L)).hasSize(2);
    }
}
