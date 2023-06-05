package cart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.dao.CouponDao;
import cart.dao.MemberCouponDao;
import cart.domain.Coupon;
import cart.integration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class CouponServiceTest extends IntegrationTest {

    private final CouponService couponService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    CouponServiceTest(JdbcTemplate jdbcTemplate) {
        this.couponService = new CouponService(new CouponDao(jdbcTemplate), new MemberCouponDao(jdbcTemplate));
    }

    @Test
    void ID로_쿠폰_조회() {
        Coupon coupon = couponService.findById(1);
        assertAll(
                () -> assertThat(coupon.getName()).isEqualTo("퍼센트 쿠폰"),
                () -> assertThat(coupon.getAmount()).isEqualTo(10)
        );
    }
}
