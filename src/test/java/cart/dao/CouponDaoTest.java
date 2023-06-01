package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

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
    void findAll() {
        Coupon coupon1 = new Coupon("오픈 기념 쿠폰", new Discount("rate", 10));
        couponDao.create(coupon1);
        Coupon coupon2 = new Coupon("오픈 기념 쿠폰", new Discount("rate", 1000));
        couponDao.create(coupon2);

        List<Coupon> coupons = couponDao.findAll();
        Assertions.assertThat(coupons).hasSizeGreaterThan(2);
    }

    @Test
    void create() {
        Coupon coupon = new Coupon("오픈 기념 쿠폰", new Discount("rate", 10));
        Long id = couponDao.create(coupon);

        Assertions.assertThat(id).isPositive();
    }

    @Test
    void delete() {
        Coupon coupon = new Coupon("오픈 기념 쿠폰", new Discount("rate", 10));
        Long id = couponDao.create(coupon);
        couponDao.delete(id);
        Integer count = jdbcTemplate.queryForObject("select count(*) from coupon where id = ?", Integer.class, id);
        Assertions.assertThat(count).isZero();
    }
}
