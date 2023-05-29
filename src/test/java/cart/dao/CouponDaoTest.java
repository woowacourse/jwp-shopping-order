package cart.dao;

import static cart.domain.CouponType.FIXED;
import static cart.domain.CouponType.RATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.domain.Coupon;
import java.util.List;
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
        Coupon coupon = new Coupon("1000원 할인 쿠폰", FIXED, 1000);

        // when
        Long actual = couponDao.save(coupon);

        // then
        assertThat(actual).isPositive();
    }

    @Test
    void 아이디를_통해_쿠폰을_조회한다() {
        // given
        Coupon expected = new Coupon("1000원 할인 쿠폰", FIXED, 1000);
        Long id = couponDao.save(expected);

        // when
        Coupon actual = couponDao.findById(id).get();

        // then
        asserThatWithoutId(actual, expected);
    }

    @Test
    void 쿠폰_조회시_아이디가_존재하지_않으면_빈값() {
        // when
        Optional<Coupon> actual = couponDao.findById(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 모든_쿠폰을_조회한다() {
        // given
        Coupon fixed = new Coupon("1000원 할인 쿠폰", FIXED, 1000);
        Coupon rate = new Coupon("10% 할인 쿠폰", RATE, 10);
        couponDao.save(fixed);
        couponDao.save(rate);

        // when
        List<Coupon> actual = couponDao.findAll();

        // then
        assertAll(
                () -> asserThatWithoutId(actual.get(0), fixed),
                () -> asserThatWithoutId(actual.get(1), rate)
        );
    }

    private void asserThatWithoutId(Coupon actual, Coupon expected) {
        assertThat(actual).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }
}
