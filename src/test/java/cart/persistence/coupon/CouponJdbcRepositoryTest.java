package cart.persistence.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Coupons;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class CouponJdbcRepositoryTest {

    private CouponJdbcRepository couponJdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponJdbcRepository = new CouponJdbcRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자의 따른 저장되어있는 쿠폰 조회 테스트")
    void findByMemberId() {
        Coupons coupons = couponJdbcRepository.findByMemberId(2L);
        assertThat(coupons.getCoupons()).hasSize(3);
    }

    @Test
    @DisplayName("사용자 쿠폰의 상태를 사용 상태로 변경한다.")
    void convertToUseMemberCouponTest() {
        Coupon coupon = new Coupon(1L, "웰컴 쿠폰 - 10%할인", 10000, 10, 0);
        couponJdbcRepository.convertToUseMemberCoupon(coupon);
        Coupons coupons = couponJdbcRepository.findByMemberId(2L);
        assertThat(coupons.getCoupons()).hasSize(2);
    }
}
