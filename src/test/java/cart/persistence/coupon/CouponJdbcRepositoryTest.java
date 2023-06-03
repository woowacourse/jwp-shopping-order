package cart.persistence.coupon;

import cart.application.service.coupon.dto.MemberCouponDto;
import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.CouponPolicy;
import cart.domain.order.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static cart.fixture.MemberFixture.비버_ID포함;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class CouponJdbcRepositoryTest {

    // 더미데이터 존재
    // INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount)
    // VALUES (1L, '웰컴 쿠폰 - 10%할인', 10000, 10, 0);
    // INSERT INTO coupon (id, `name`, min_amount, discount_percent, discount_amount)
    // VALUES (2L, '또 와요 쿠폰 - 3000원 할인', 15000, 0, 3000);

    // INSERT INTO member_coupon (id, member_id, coupon_id, status)
    // VALUES (1L, 2L, 1L, 1);
    // INSERT INTO member_coupon (id, member_id, coupon_id, status)
    // VALUES (2L, 2L, 1L, 0);
    // INSERT INTO member_coupon (id, member_id, coupon_id, status)
    // VALUES (3L, 2L, 1L, 1);
    // INSERT INTO member_coupon (id, member_id, coupon_id, status)
    // VALUES (4L, 2L, 2L, 1);

    private CouponJdbcRepository couponJdbcRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponJdbcRepository = new CouponJdbcRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("사용자의 따라 저장되어있는 쿠폰 조회 테스트")
    void findByMemberId() {
        List<MemberCouponDto> coupons = couponJdbcRepository.findByMemberId(2L);
        List<String> couponNames = coupons.stream().map(MemberCouponDto::getCouponName).collect(Collectors.toList());

        Assertions.assertAll(
                () -> assertThat(coupons).hasSize(3),
                () -> assertThat(couponNames).containsExactlyInAnyOrder("웰컴 쿠폰 - 10%할인", "웰컴 쿠폰 - 10%할인", "또 와요 쿠폰 - 3000원 할인")
        );
    }

    @Test
    @DisplayName("memberCouponId가 정률 할인인 쿠폰을 조회한다.")
    void findPercentCouponByIdTest() {
        Optional<CouponPolicy> percentCoupon = couponJdbcRepository.findPercentCouponById(3L);
        CouponPolicy coupon = percentCoupon.get();
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
        List<MemberCouponDto> usableCoupons = couponJdbcRepository.findByMemberId(2L);
        assertThat(usableCoupons).hasSize(2);
    }

    @Test
    @DisplayName("사용자가 주문에 사용한 쿠폰을 저장한다.")
    void createOrderedCoupon() {
        Order order = new Order(1L, 10000, 11000, 1000, 비버_ID포함, null);
        assertThat(couponJdbcRepository.createOrderedCoupon(order.getId(), 2L)).isPositive();
    }

    @Test
    @DisplayName("주문에 사용한 쿠폰 목록을 조회한다.")
    void findCouponsOnOrder() {
        couponJdbcRepository.createOrderedCoupon(1L, 1L);
        List<Coupon> usedCoupons = couponJdbcRepository.findUsedCouponByOrderId(1L);
        Coupon coupon = usedCoupons.get(0);
        Assertions.assertAll(
                () -> assertThat(usedCoupons).hasSize(1),
                () -> assertThat(coupon.getCouponName()).isEqualTo("웰컴 쿠폰 - 10%할인"),
                () -> assertThat(coupon.getDiscountAmount()).isEqualTo(0),
                () -> assertThat(coupon.getDiscountPercent()).isEqualTo(10),
                () -> assertThat(coupon.getMinAmount()).isEqualTo(10000)
        );
    }

    @Test
    @DisplayName("쿠폰을 사용하지 않은 주문의 경우 빈 리스트 반환")
    void findCouponWhenNoUseCouponInOrder() {
        List<Coupon> usedCoupons = couponJdbcRepository.findUsedCouponByOrderId(1L);
        assertThat(usedCoupons).isEmpty();
    }
}
