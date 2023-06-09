package cart.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;

import cart.domain.Coupon;
import cart.domain.CouponType;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "classpath:data.sql", config = @SqlConfig(encoding = "utf-8"), executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CouponDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CouponDao couponDao;

    @BeforeEach
    void setup() {
        couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    @DisplayName("쿠폰 ID로 쿠폰을 조회한다.")
    void findById_ShouldReturnCouponForGivenId() {
        Long couponId = 1L;
        Coupon expectedCoupon = new Coupon(couponId, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);

        Coupon actualCoupon = couponDao.findById(couponId);

        assertThat(actualCoupon.getId()).isEqualTo(expectedCoupon.getId());
    }

    @Test
    @DisplayName("쿠폰을 저장하고 키값을 반환한다.")
    void save_ShouldSaveCouponAndReturnGeneratedId() {
        Coupon coupon = new Coupon(null, "New Coupon", 500, CouponType.FIXED_AMOUNT, 500, null, null);

        Long generatedId = couponDao.save(coupon);

        assertThat(generatedId).isNotNull();
    }
}
