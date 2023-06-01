package cart.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
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
    void findById_ShouldReturnCouponForGivenId() {
        Long couponId = 1L;
        Coupon expectedCoupon = new Coupon(couponId, "10프로 할인", 1000, CouponType.FIXED_PERCENTAGE, null, 0.1, 10000);

        Coupon actualCoupon = couponDao.findById(couponId);

        assertThat(actualCoupon.getId()).isEqualTo(expectedCoupon.getId());
    }

    @Test
    void save_ShouldSaveCouponAndReturnGeneratedId() {
        Coupon coupon = new Coupon(null, "New Coupon", 500, CouponType.FIXED_AMOUNT, 500, null, null);

        Long generatedId = couponDao.save(coupon);

        assertThat(generatedId).isNotNull();
    }
}
