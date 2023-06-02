package cart.order_item.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import cart.coupon.domain.EmptyCoupon;
import cart.member.domain.Member;
import cart.order.dao.OrderDao;
import cart.order.domain.Order;
import cart.order_item.dao.entity.OrderItemEntity;
import cart.order_item.domain.OrderItem;
import cart.value_object.Money;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
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

  private OrderDao orderDao;

  @BeforeEach
  void setUp() {
    orderDao = mock(OrderDao.class);

    orderItemDao = new OrderItemDao(jdbcTemplate, orderDao);
  }

  @Test
  @DisplayName("batchSave() : 주문된 상품들을 한번에 저장할 수 있다.")
  void test_batchSave() throws Exception {
    //given
    final String imageUrl = "imageUrl";
    final String itemName = "itemName";
    final BigDecimal price = BigDecimal.valueOf(1000);
    final int quantity = 4;

    final OrderItemEntity orderItemEntity1 = new OrderItemEntity(3L, itemName, price, imageUrl,
        quantity);

    final OrderItemEntity orderItemEntity2 = new OrderItemEntity(3L, "itemName2",
        BigDecimal.valueOf(2000), "imageUrl2", 5);

    final List<OrderItemEntity> orderItemEntities = List.of(orderItemEntity1, orderItemEntity2);

    //when & then
    assertDoesNotThrow(() -> orderItemDao.save(orderItemEntities));
  }

  @Test
  @DisplayName("findByOrderId() : order Id를 통해 주문된 상품들을 조회할 수 있다.")
  void test_findByOrderId() throws Exception {
    //given
    final Long orderId = 1L;

    final Member member = new Member(1L, "email", "password");
    final Order order = new Order(1L, member, new Money(100), new EmptyCoupon());

    when(orderDao.findByOrderId(anyLong()))
        .thenReturn(order);

    //when
    final List<OrderItem> orderItems = orderItemDao.findByOrderId(orderId);

    //then
    assertEquals(3, orderItems.size());
  }

  @Test
  @DisplayName("deleteBatch() : 여러 id를 통해서 orderItem 을 삭제할 수 있다.")
  void test_deleteBatch() throws Exception {
    //given
    final long orderId = 1L;
    final List<Long> 삭제할_주문아이템_아이디 = orderItemDao.findByOrderId(orderId)
        .stream()
        .map(OrderItem::getId)
        .collect(Collectors.toList());

    //when
    orderItemDao.deleteBatch(삭제할_주문아이템_아이디);

    //then
    final List<OrderItem> deletedOrderItems = orderItemDao.findByOrderId(orderId);

    assertEquals(0, deletedOrderItems.size());
  }
}
