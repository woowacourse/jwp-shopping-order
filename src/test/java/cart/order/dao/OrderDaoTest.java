package cart.order.dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.order.dao.entity.OrderEntity;
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
class OrderDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private OrderDao orderDao;

  @BeforeEach
  void setUp() {
    orderDao = new OrderDao(jdbcTemplate);
  }

  @Test
  @DisplayName("findByMemberId() : member Id를 통해 사용자가 주문한 주문 목록들을 조회할 수 있다.")
  void test_findByMemberId() throws Exception {
    //given
    final long memberId = 1L;

    //when
    List<OrderEntity> orderEntities = orderDao.findByMemberId(memberId);

    //then
    assertEquals(1, orderEntities.size());
  }
}
