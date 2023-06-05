package cart.infrastructure.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import cart.entity.CouponEntity;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

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
    @DisplayName("id로 쿠폰을 조회할 수 있다.")
    void testFindById() {
        // when
        final CouponEntity result = couponDao.findById(1L).orElseThrow(NoSuchElementException::new);

        // then
        assertThat(result.getName()).isEqualTo("1000원 할인 쿠폰");
        assertThat(result.getMinAmount()).isEqualTo(15_000);
        assertThat(result.getDiscountAmount()).isEqualTo(1_000);
    }

    @Test
    @DisplayName("모든 쿠폰을 조회할 수 있다.")
    void testFindAll() {
        // given
        final CouponEntity expected1 = couponDao.findById(1L).orElseThrow(NoSuchElementException::new);
        final CouponEntity expected2 = couponDao.findById(2L).orElseThrow(NoSuchElementException::new);

        // when
        final List<CouponEntity> couponEntities = couponDao.findAll();

        // then
        assertAll(
                () -> assertThat(couponEntities).hasSize(2),
                () -> assertThat(couponEntities)
                        .usingRecursiveComparison()
                        .isEqualTo(List.of(expected1, expected2))
        );
    }

    @Test
    @DisplayName("쿠폰을 수정할 수 있다.")
    void testUpdate() {
        // given
        final CouponEntity updatedCoupon = CouponEntity.of(1L, "2000원 할인 쿠폰", 15_000, 2_000);

        // when
        couponDao.update(updatedCoupon);

        // then
        final CouponEntity result = couponDao.findById(1L).orElseThrow(NoSuchElementException::new);
        assertThat(result).usingRecursiveComparison().isEqualTo(updatedCoupon);
    }
}
