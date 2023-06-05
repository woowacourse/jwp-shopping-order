package cart.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import cart.order.application.dto.SpecificOrderResponse;
import cart.order.exception.CanNotSearchNotMyOrderException;
import java.math.BigDecimal;
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
class OrderQueryServiceTest {

  @Autowired
  private OrderQueryService orderQueryService;

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("searchOrders() : 사용자가 주문한 주문 목록들을 조회할 수 있다.")
  void test_searchOrders() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    //when
    final List<OrderResponse> orderResponses = orderQueryService.searchOrders(member);

    //then
    assertAll(
        () -> assertEquals(3, orderResponses.size()),
        () -> assertEquals(3, orderResponses.get(0).getProducts().size()),
        () -> assertEquals(2, orderResponses.get(1).getProducts().size()),
        () -> assertEquals(1, orderResponses.get(2).getProducts().size())
    );
  }

  @Test
  @DisplayName("searchOrder() : 사용자의 주문이 아닌 다른 사용자의 주문을 조회할 경우 CanNotSearchNotMyOrderException이 발생한다.")
  void test_searchOrder_CanNotSearchNotMyOrderException() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    //when & then
    assertThatThrownBy(() -> orderQueryService.searchOrder(member, 4L))
        .isInstanceOf(CanNotSearchNotMyOrderException.class);
  }

  @Test
  @DisplayName("searchOrder() : 특정한 주문 목록을 조회할 수 있다.")
  void test_searchOrder() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final long orderId = 2L;

    //when
    final SpecificOrderResponse specificOrderResponse =
        orderQueryService.searchOrder(member, orderId);

    //then
    assertAll(
        () -> assertEquals(0,
            specificOrderResponse.getTotalPrice().compareTo(BigDecimal.valueOf(199500))),
        () -> assertEquals(specificOrderResponse.getOrderId(), orderId)
    );
  }
}
