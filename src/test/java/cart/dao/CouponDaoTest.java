package cart.dao;

import static org.assertj.core.api.Assertions.assertThat;

import cart.entity.CouponEntity;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
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
    void 쿠폰을_저장한다() {
        // given
        CouponEntity couponEntity = new CouponEntity("쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.valueOf(0));

        // when
        Long id = couponDao.save(couponEntity);

        // then
        assertThat(id).isPositive();
    }

    @Test
    void 쿠폰을_id로_조회한다() {
        // given
        CouponEntity couponEntity = new CouponEntity("쿠폰", "RATE", BigDecimal.valueOf(10), BigDecimal.valueOf(0));
        Long id = couponDao.save(couponEntity);

        // when
        Optional<CouponEntity> findCouponEntity = couponDao.findById(id);

        // then
        assertThat(findCouponEntity.get()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(couponEntity);

    }
}
