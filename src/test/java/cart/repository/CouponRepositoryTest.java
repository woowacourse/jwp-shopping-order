package cart.repository;

import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.DiscountPolicyProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

import static cart.fixture.CouponFixture.AMOUNT_1000_COUPON;
import static cart.fixture.CouponFixture.RATE_10_COUPON;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@ComponentScan(basePackageClasses = {CouponRepository.class, DiscountPolicyProvider.class})
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @DisplayName("쿠폰 생성 조회 테스트")
    void insert_and_find_coupon_test() {
        // given

        // when
        Long couponId = couponRepository.insert(RATE_10_COUPON);
        Coupon insertedCoupon = couponRepository.findById(couponId);

        // then
        assertThat(insertedCoupon.getName()).isEqualTo(RATE_10_COUPON.getName());
    }

    @Test
    @DisplayName("쿠폰 전체 조회 테스트")
    void find_all_coupon_test() {
        // given
        couponRepository.insert(RATE_10_COUPON);
        couponRepository.insert(AMOUNT_1000_COUPON);

        // when
        final List<Coupon> coupons = couponRepository.findAll();

        // then
        assertThat(coupons).hasSize(2);
    }

}
