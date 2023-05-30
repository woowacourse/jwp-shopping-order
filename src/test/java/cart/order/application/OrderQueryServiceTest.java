package cart.order.application;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.OrderResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
        () -> assertEquals(1, orderResponses.size()),
        () -> assertEquals(3, orderResponses.get(0).getProducts().size())
    );
  }
}
