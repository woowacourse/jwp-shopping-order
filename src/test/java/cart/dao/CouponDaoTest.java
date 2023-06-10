package cart.dao;

import cart.entity.CouponEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
        CouponEntity coupon1 = new CouponEntity("오픈 기념 쿠폰", "rate", 10);
        couponDao.create(coupon1);
        CouponEntity coupon2 = new CouponEntity("오픈 기념 쿠폰", "rate", 20);
        couponDao.create(coupon2);

        List<CouponEntity> coupons = couponDao.findAll();
        Assertions.assertThat(coupons).hasSizeGreaterThan(2);
    }

    @Test
    void create() {
        CouponEntity coupon = new CouponEntity("오픈 기념 쿠폰", "rate", 10);
        Long id = couponDao.create(coupon);

        Assertions.assertThat(id).isPositive();
    }

    @Test
    void delete() {
        CouponEntity coupon = new CouponEntity("오픈 기념 쿠폰", "rate", 10);
        Long id = couponDao.create(coupon);
        couponDao.delete(id);
        Integer count = jdbcTemplate.queryForObject("select count(*) from coupon where id = ?", Integer.class, id);
        Assertions.assertThat(count).isZero();
    }
}
