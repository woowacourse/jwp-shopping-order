package cart.coupon.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
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
    final List<CouponEntity> couponEntities = couponDao.findByIdsIn(couponIds);

    //then
    assertEquals(2, couponEntities.size());
  }
}
