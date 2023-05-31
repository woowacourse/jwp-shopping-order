package cart.dao;

import static cart.fixture.TestFixture.COUPON_FIXED_2000;
import static cart.fixture.TestFixture.COUPON_PERCENTAGE_50;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import cart.domain.Coupon;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@JdbcTest
class CouponDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    CouponDao couponDao;

    @BeforeEach
    void setUp() {
        this.couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    void 쿠폰을_삽입한다() {
        Long id = couponDao.insert(COUPON_FIXED_2000);

        assertThat(id).isNotNull();
    }

    @Test
    void 쿠폰을_id로_조회한다() {
        Long id = couponDao.insert(COUPON_FIXED_2000);

        Coupon coupon = couponDao.selectBy(id);

        assertThat(coupon)
                .usingRecursiveComparison()
                .isEqualTo(COUPON_FIXED_2000);
    }

    @Test
    void 모든_쿠폰을_조회한다() {
        couponDao.insert(COUPON_FIXED_2000);
        couponDao.insert(COUPON_PERCENTAGE_50);

        assertThat(couponDao.selectAll())
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(COUPON_FIXED_2000, COUPON_PERCENTAGE_50);
    }
}
