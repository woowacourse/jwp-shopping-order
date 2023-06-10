package cart.dao;

import cart.dao.entity.CouponEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

@JdbcTest
class CouponDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CouponDao couponDao;

    @BeforeEach
    public void setUp() {
        couponDao = new CouponDao(jdbcTemplate);
    }

    @Test
    public void 모든_쿠폰을_조회한다() {
        List<CouponEntity> coupons = couponDao.findAll();

        Assertions.assertThat(coupons).hasSize(2);
    }

    @Test
    public void 아이디로_조회한다() {
        Long id = 1L;

        Optional<CouponEntity> coupon = couponDao.findById(id);

        Assertions.assertThat(coupon.get().getName()).isEqualTo("테스트쿠폰1");
    }

    @Test
    public void 아이디로_쿠폰_조회_시_없으면_빈값을_반환한다() {
        Long id = 100L;

        Optional<CouponEntity> coupon = couponDao.findById(id);

        Assertions.assertThat(coupon).isEmpty();
    }

    @Test
    public void 아이디_목록으로_조회한다() {
        List<Long> ids = List.of(1L, 2L);

        List<CouponEntity> coupons = couponDao.findByIds(ids);

        Assertions.assertThat(coupons).hasSize(2);
    }
}
