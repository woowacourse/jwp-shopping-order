package cart.dao;

import static cart.domain.CouponType.PRICE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Coupon;
import cart.entity.CouponEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("CouponDao 은(는)")
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
        Coupon coupon = new Coupon("1000원 할인 쿠폰", PRICE, 1000);

        // when
        Long actual = couponDao.save(coupon);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 아이디를_통해_쿠폰을_조회한다() {
        // given
        Coupon expected = new Coupon("1000원 할인 쿠폰", PRICE, 1000);
        Long id = couponDao.save(expected);

        // when
        CouponEntity actual = couponDao.findById(id).get();

        // then
        assertCouponEntity(actual, expected);
    }

    @Test
    void 쿠폰_조회시_아이디가_존재하지_않으면_빈값() {
        // when
        Optional<CouponEntity> actual = couponDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    private void assertCouponEntity(CouponEntity actual, Coupon expected) {
        assertAll(
                () -> assertThat(actual.getId()).isPositive(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getType()).isEqualTo(expected.getType().name()),
                () -> assertThat(actual.getDiscountAmount()).isEqualTo(expected.getDiscountAmount())
        );
    }
}
