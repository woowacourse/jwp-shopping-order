package cart.order.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import cart.member.dao.MemberDao;
import cart.member.domain.Member;
import cart.order.application.dto.RegisterOrderRequest;
import cart.order.exception.CanNotDeleteNotMyOrderException;
import cart.order.exception.NotSameTotalPriceException;
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
    final BigDecimal totalPrice = BigDecimal.valueOf(380400);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);
    final long couponId = 1L;

    final RegisterOrderRequest registerOrderRequest =
        new RegisterOrderRequest(
            cartItemIds,
            totalPrice,
            deliveryFee,
            couponId
        );

    //when
    final Long savedId = orderCommandService.registerOrder(member, registerOrderRequest);

    //then
    assertEquals(5L, savedId);
  }

  @Test
  @DisplayName("registerOrder() : 명시된 주문 총 금액과 계산한 총 금액이 다르면 NotSameTotalPriceException이 발생한다.")
  void test_registerOrder_NotSameTotalPriceException() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    final List<Long> cartItemIds = List.of(1L, 2L);
    final BigDecimal totalPrice = BigDecimal.valueOf(200000);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);
    final long couponId = 1L;

    final RegisterOrderRequest registerOrderRequest =
        new RegisterOrderRequest(
            cartItemIds,
            totalPrice,
            deliveryFee,
            couponId
        );

    //when & then
    assertThatThrownBy(() -> orderCommandService.registerOrder(member, registerOrderRequest))
        .isInstanceOf(NotSameTotalPriceException.class);
  }

  @Test
  @DisplayName("registerOrder() : 주문 중 쿠폰을 사용하지 않아도 생성할 수 있다.")
  void test_registerOrder_NotCoupon() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);

    final List<Long> cartItemIds = List.of(1L, 2L);
    final BigDecimal totalPrice = BigDecimal.valueOf(380400);
    final BigDecimal deliveryFee = BigDecimal.valueOf(3000);
    final Long couponId = null;

    final RegisterOrderRequest registerOrderRequest =
        new RegisterOrderRequest(
            cartItemIds,
            totalPrice,
            deliveryFee,
            couponId
        );

    //when
    final Long savedId = orderCommandService.registerOrder(member, registerOrderRequest);

    //then
    assertNotNull(savedId);
  }

  @Test
  @DisplayName("deleteOrder() : 다른 사용자의 주문 목록을 삭제하려고 하면 CanNotDeleteNotMyOrderException이 발생한다.")
  void test_deleteOrder_CanNotDeleteNotMyOrderException() throws Exception {
    //given
    final Member member = memberDao.getMemberById(1L);
    final Long 다른_사용자의_주문_아이디 = 4L;

    //when & then
    assertThatThrownBy(() -> orderCommandService.deleteOrder(member, 다른_사용자의_주문_아이디))
        .isInstanceOf(CanNotDeleteNotMyOrderException.class);
  }
}
