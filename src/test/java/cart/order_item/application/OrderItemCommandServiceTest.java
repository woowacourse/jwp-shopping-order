package cart.order_item.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order_item.exception.CanNotOrderNotInCart;
import cart.value_object.Money;
import java.util.List;
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

  @Test
  @DisplayName("registerOrderItem() : 주문된 상품을 저장할 수 있다.")
  void test_registerOrderItem() throws Exception {
    //given
    final List<Long> cartIdemIds = List.of(1L, 2L);
    final Member member = memberDao.getMemberById(1L);
    final Order order = new Order(1L, member, new Money(3000));

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
    final Order order = new Order(1L, member, new Money(3000));

    //when & then
    assertThatThrownBy(
        () -> orderItemCommandService.registerOrderItem(cartIdemIds, order, member))
        .isInstanceOf(CanNotOrderNotInCart.class);
  }
}
