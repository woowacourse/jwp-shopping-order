package cart.order_item.application;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderItemCommandServiceTest {

  @Autowired
  private OrderItemCommandService orderItemCommandService;

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("registerOrderItem() : 주문된 상품을 저장할 수 있다.")
  void test_registerOrderItem() throws Exception {
    //given
    final long orderId = 3L;
    final List<Long> cartIdemIds = List.of(1L, 2L);
    final Member member = memberDao.getMemberById(1L);

    //when & then
    assertDoesNotThrow(
        () -> orderItemCommandService.registerOrderItem(cartIdemIds, orderId, member));
  }
}
