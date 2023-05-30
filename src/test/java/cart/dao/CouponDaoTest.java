package cart.dao;

import cart.domain.Coupon;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CouponDaoTest {
    private CouponDao couponDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    @DisplayName("쿠폰을 생성한다.")
    void createCoupon() {
        Coupon coupon = new Coupon("쿠폰1", 10);

        Long id = couponDao.createCoupon(coupon);

        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("쿠폰을 삭제한다.")
    void deleteCoupon() {
        Coupon coupon = new Coupon("쿠폰1", 10);
        Long id = couponDao.createCoupon(coupon);

        couponDao.deleteCoupon(id);

        assertThatThrownBy(() -> couponDao.getCouponById(id))
                .isInstanceOf(EmptyResultDataAccessException.class);

    }

}