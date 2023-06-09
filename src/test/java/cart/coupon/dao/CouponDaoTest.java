package cart.coupon.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import cart.coupon.domain.Coupon;
import cart.coupon.domain.EmptyCoupon;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
class CouponDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private CouponDao couponDao;

  @BeforeEach
  void setUp() {
    couponDao = new CouponDao(jdbcTemplate);
  }

  @Test
  @DisplayName("findByIdsIn() : coupon id 들을 통해 IN 쿼리를 사용하여 쿠폰들을 조회할 수 있다.")
  void test_findByIdsIn() throws Exception {
    //given
    final List<Long> couponIds = List.of(1L, 2L, 4L);

    //when
    final List<Coupon> coupons = couponDao.findByIdsIn(couponIds);

    //then
    assertEquals(2, coupons.size());
  }

  @Test
  @DisplayName("findById() : couponId가 0이면 EmptyCoupon을 반환한다.")
  void test_findById_EmptyCoupon() throws Exception {
    //given
    final long couponId = 0L;

    //when
    final Optional<Coupon> coupon = couponDao.findById(couponId);

    //then
    assertAll(
        () -> assertThat(coupon).isPresent(),
        () -> assertInstanceOf(EmptyCoupon.class, coupon.get())
    );
  }

  @Test
  @DisplayName("findById() : coupon Id 를 통해 조회할 때 해당 쿠폰이 없다면 Optional Empty를 반환한다.")
  void test_findById_optionalEmpty() throws Exception {
    //given
    final long couponId = 10L;

    //when
    final Optional<Coupon> coupon = couponDao.findById(couponId);

    //then
    assertThat(coupon).isEmpty();
  }
}
