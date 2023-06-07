package cart.dao;

import cart.domain.Coupon;
import cart.domain.Money;
import cart.exception.CouponNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@JdbcTest
class CouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    void findById() {
        Coupon coupon = couponDao.findUnusedById(1L);
        assertThat(coupon.getDiscountPrice()).isEqualTo(Money.from(1000));
    }

    @Test
    void delete() {
        //when
        couponDao.updateToUsed(1L);

        //then
        assertThatThrownBy(() -> couponDao.findUnusedById(1L)).isInstanceOf(CouponNotFoundException.class);
    }
}