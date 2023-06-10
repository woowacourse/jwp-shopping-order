package cart.application;

import static cart.fixture.CartItemsFixture.CART_ITEMS_1;
import static cart.fixture.MemberFixture.MEMBER_1;
import static cart.fixture.OrderFixture.ORDER_1;
import static cart.fixture.OrderFixture.ORDER_2;
import static cart.fixture.OrderRequestFixture.ORDER_REQUEST_1;
import static cart.fixture.PaymentFixture.PAYMENT_1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willReturn;

import cart.dao.MemberDao;
import cart.domain.Order;
import cart.domain.PaymentGenerator;
import cart.dto.order.OrderResponse;
import cart.dto.order.OrderSimpleResponse;
import cart.repository.OrderRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(OrderService.class)
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @MockBean
    OrderRepository orderRepository;

    @MockBean
    PaymentGenerator paymentGenerator;

    @MockBean
    MemberDao memberDao;

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // given
        willReturn(CART_ITEMS_1).given(orderRepository).findCartItemsByMemberId(MEMBER_1);
        willReturn(PAYMENT_1).given(paymentGenerator).generate(any());
        willReturn(1L).given(orderRepository).createOrder(any(), any());

        // when
        Long orderId = orderService.createOrder(MEMBER_1, ORDER_REQUEST_1);

        // then
        assertThat(orderId).isEqualTo(1L);
    }

    @Test
    @DisplayName("단건 주문 내역을 조회한다.")
    void findOrder() {
        // given
        willReturn(ORDER_1).given(orderRepository).findOrder(ORDER_1.getId(), MEMBER_1);

        // when
        OrderResponse orderResponse = orderService.findOrder(ORDER_1.getId(), MEMBER_1);

        // then
        assertThat(orderResponse)
                .usingRecursiveComparison()
                .isEqualTo(OrderResponse.from(ORDER_1));
    }

    @Test
    @DisplayName("특정 사용자의 전체 주문 내역을 조회한다.")
    void findAllByMember() {
        // given
        List<Order> orders = List.of(ORDER_1, ORDER_2);
        willReturn(orders).given(orderRepository).findAllByMember(MEMBER_1);

        // when
        List<OrderSimpleResponse> responses = orderService.findAllByMember(MEMBER_1);

        // then
        assertThat(responses)
                .usingRecursiveComparison()
                .isEqualTo(
                        orders.stream()
                                .map(OrderSimpleResponse::from)
                                .collect(Collectors.toList()));
    }
}
