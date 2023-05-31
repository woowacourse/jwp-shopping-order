package cart.dao;

import cart.domain.coupon.Coupon;
import cart.domain.coupon.Discount;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

@JdbcTest
class CouponDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;
    private CouponDao couponDao;

    @BeforeEach
    void setUp() {
        couponDao = new CouponDao(jdbcTemplate, namedJdbcTemplate);
    }

    @Test
    void findAll() {
        Coupon coupon1 = new Coupon("오픈 기념 쿠폰", new Discount("rate", 10));
        couponDao.create(coupon1);
        Coupon coupon2 = new Coupon("오픈 기념 쿠폰", new Discount("rate", 1000));
        couponDao.create(coupon2);

        List<Coupon> coupons = couponDao.findAll();
        Assertions.assertThat(coupons).doesNotContainNull();
    }

    @Test
    void findById() {
        Coupon coupon = new Coupon("오픈 기념 쿠폰", new Discount("rate", 10));
        Long id = couponDao.create(coupon);

        Coupon findCoupon = couponDao.findById(id);
        Assertions.assertThat(findCoupon)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(coupon);
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
