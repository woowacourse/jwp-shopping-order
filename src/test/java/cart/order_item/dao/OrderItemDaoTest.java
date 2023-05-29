package cart.order_item.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.order_item.dao.entity.OrderItemEntity;
import java.math.BigDecimal;
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
class OrderItemDaoTest {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  private OrderItemDao orderItemDao;

  @BeforeEach
  void setUp() {
    orderItemDao = new OrderItemDao(jdbcTemplate);
  }

  @Test
  @DisplayName("save() : 주문된 상품을 저장할 수 있다.")
  void test_save() throws Exception {
    //given
    final String imageUrl = "imageUrl";
    final String itemName = "itemName";
    final BigDecimal price = BigDecimal.valueOf(1000);
    final int quantity = 4;

    final OrderItemEntity orderItemEntity = new OrderItemEntity(1L, itemName, price, imageUrl,
        quantity);

    //when & then
    assertDoesNotThrow(() -> orderItemDao.save(orderItemEntity));
  }
}
