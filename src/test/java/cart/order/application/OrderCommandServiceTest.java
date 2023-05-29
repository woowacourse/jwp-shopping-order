package cart.order.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderCommandServiceTest {

  @Autowired
  private OrderCommandService orderCommandService;

  @Autowired
  private MemberDao memberDao;

  @Test
  @DisplayName("registerOrder() : 주문을 새로 생성할 수 있다.")
  void test_registerOrder() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    final List<Long> cartItemIds = List.of(1L, 2L);
    final BigDecimal totalPrice = BigDecimal.valueOf(10000);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);

    final RegisterOrderRequest registerOrderRequest =
        new RegisterOrderRequest(
            cartItemIds,
            totalPrice,
            deliveryFee
        );

    //when
    final Long savedId = orderCommandService.registerOrder(member, registerOrderRequest);

    //then
    assertEquals(3L, savedId);
  }
}
