package cart.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
class CouponDaoTest {
    private final CouponDao couponDao;
    @Autowired
    private CouponDaoTest(JdbcTemplate jdbcTemplate){
        couponDao = new CouponDao(jdbcTemplate);
    }
    @Test
    @DisplayName("쿠폰을 찾는다.")
    void findWithId() {
        Assertions.assertThat(couponDao.findWithId(1L).getName()).isEqualTo("10% 할인");
    }

    @Test
    @DisplayName("모든 쿠폰을 찾는다")
    void findAll() {
        Assertions.assertThat(couponDao.findAll()).hasSize(6);
    }
}
