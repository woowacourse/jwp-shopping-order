package cart.order_item.application;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.coupon.domain.EmptyCoupon;
import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order.domain.OrderStatus;
import cart.order_item.domain.OrderItem;
import cart.order_item.exception.CanNotOrderNotInCart;
import cart.value_object.Money;
import java.time.ZonedDateTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@SpringBootTest
@Sql(scripts = "/schema.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/data.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class OrderItemCommandServiceTest {

  @Autowired
  private OrderItemCommandService orderItemCommandService;

  @Autowired
  private MemberDao memberDao;

  @Autowired
  private OrderItemQueryService orderItemQueryService;

  @Test
  @DisplayName("registerOrderItem() : 주문된 상품을 저장할 수 있다.")
  void test_registerOrderItem() throws Exception {
    //given
    final List<Long> cartIdemIds = List.of(1L, 2L);
    final Member member = memberDao.getMemberById(1L);
    final Order order = new Order(
        1L, member,
        new Money(100), new EmptyCoupon(),
        OrderStatus.CANCEL, ZonedDateTime.now());

    //when & then
    assertDoesNotThrow(
        () -> orderItemCommandService.registerOrderItem(cartIdemIds, order, member));
  }

  @Test
  @DisplayName("registerOrderItem() : 장바구니에 담지 않은 물품을 주문할 경우에는 CanNotOrderNotInCart이 발생합니다.")
  void test_registerOrderItem_CanNotOrderNotInCart() throws Exception {
    //given
    final List<Long> cartIdemIds = List.of(1L, 2L, 3L, 4L, 8L);
    final Member member = memberDao.getMemberById(1L);
    final Order order = new Order(
        1L, member,
        new Money(100), new EmptyCoupon(),
        OrderStatus.CANCEL, ZonedDateTime.now());

    //when & then
    assertThatThrownBy(
        () -> orderItemCommandService.registerOrderItem(cartIdemIds, order, member))
        .isInstanceOf(CanNotOrderNotInCart.class);
  }

  @Test
  @DisplayName("deleteBatch() : Order가 삭제될 때 포함되어 있는 OrderItem 을 삭제할 수 있다.")
  void test_deleteBatch() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final Order order = new Order(
        1L, member,
        new Money(100), new EmptyCoupon(),
        OrderStatus.CANCEL, ZonedDateTime.now());

    //when
    orderItemCommandService.deleteBatch(order);

    //then
    final List<OrderItem> orderItems = orderItemQueryService.searchOrderItemsByOrderId(order);

    assertThat(orderItems).isEmpty();
  }
}
