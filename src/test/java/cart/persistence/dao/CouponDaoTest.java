package cart.persistence.dao;

import static cart.fixture.CouponFixture.금액쿠폰_10000원이상_1000할인_엔티티;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.persistence.entity.CouponEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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
    void 쿠폰을_조회_및_생성한다() {
        // given
        CouponEntity coupon = 금액쿠폰_10000원이상_1000할인_엔티티;

        // when
        long id = couponDao.create(coupon);

        // then
        Optional<CouponEntity> result = couponDao.findById(id);
        assertAll(
                () -> assertThat(result).isPresent(),
                () -> assertThat(result.get())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(coupon)
        );
    }
}
