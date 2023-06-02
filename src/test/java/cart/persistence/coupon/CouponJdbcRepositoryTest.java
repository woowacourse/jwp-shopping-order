package cart.persistence.coupon;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponPolicy;
import cart.domain.order.Order;
import cart.fixture.MemberFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

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
        List<Coupon> coupons = couponJdbcRepository.findByMemberId(2L);
        assertThat(coupons).hasSize(3);
    }

    @Test
    @DisplayName("memberCouponId가 정률 할인인 쿠폰을 조회한다.")
    void findPercentCouponByIdTest() {
        Optional<CouponPolicy> percentCoupon = couponJdbcRepository.findPercentCouponById(3L);
        assertThat(percentCoupon).isNotEmpty();
    }

    @Test
    @DisplayName("memberCouponId가 정액 할인인 경우 Optional.empty()반환")
    void findPercentCouponByIdEmptyTest() {
        Optional<CouponPolicy> percentCoupon = couponJdbcRepository.findPercentCouponById(4L);
        assertThat(percentCoupon).isEmpty();
    }

    @Test
    @DisplayName("memberCouponId가 정액 할인인 쿠폰을 조회한다.")
    void findAmountCouponByIdTest() {
        Optional<CouponPolicy> amountCoupon = couponJdbcRepository.findAmountCouponById(4L);
        assertThat(amountCoupon).isNotEmpty();
    }

    @Test
    @DisplayName("memberCouponId가 정액 할인인 경우 Optional.empty()반환")
    void findAmountCouponByIdEmptyTest() {
        Optional<CouponPolicy> amountCoupon = couponJdbcRepository.findAmountCouponById(3L);
        assertThat(amountCoupon).isEmpty();
    }

    @Test
    @DisplayName("사용자 쿠폰의 상태를 사용 상태로 변경한다.")
    void convertToUseMemberCouponTest() {
        couponJdbcRepository.convertToUseMemberCoupon(4L);
        List<Coupon> usableCoupons = couponJdbcRepository.findByMemberId(2L);
        assertThat(usableCoupons).hasSize(2);
    }

    @Test
    @DisplayName("사용자가 주문에 사용한 쿠폰을 저장한다.")
    void createOrderedCoupon() {
        Order order = new Order(1L, MemberFixture.비버_ID포함, 10000, 11000, 1000);
        assertThat(couponJdbcRepository.createOrderedCoupon(order.getId(), 2L)).isPositive();
    }
}
