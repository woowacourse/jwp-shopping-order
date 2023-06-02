package cart.member_coupon.dao;

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
class MemberCouponDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private MemberCouponDao memberCouponDao;

  @BeforeEach
  void setUp() {
    memberCouponDao = new MemberCouponDao(jdbcTemplate);
  }

  @Test
  @DisplayName("findByMemberId() : member Id를 통해 member가 사용하지 않고 보유하고 있는 쿠폰들을 조회할 수 있다.")
  void test_findByMemberId() throws Exception {
    //given
    final long memberId = 1L;

    //when
    final List<MemberCouponEntity> memberCouponEntities = memberCouponDao.findByMemberId(memberId);

    //then
    assertEquals(2, memberCouponEntities.size());
  }
}
