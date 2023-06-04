package cart.order.application;

import cart.cartitem.domain.CartItemTest;
import cart.cartitem.dto.CartItemOrderRequest;
import cart.cartitem.repository.CartItemEntity;
import cart.cartitem.repository.CartItemRepository;
import cart.member.domain.Member;
import cart.member.domain.MemberTest;
import cart.member.repository.MemberRepository;
import cart.order.domain.Order;
import cart.order.dto.OrderDetailResponse;
import cart.order.dto.OrderRequest;
import cart.order.dto.OrderResponse;
import cart.order.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static cart.cartitem.domain.CartItemTest.*;
import static cart.member.domain.MemberTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void 주문한다() {
        // given
        final OrderRequest orderRequest = new OrderRequest(List.of(1L), 50000L, 5000L, 5000L);
        given(memberRepository.getMemberByEmail(any())).willReturn(MEMBER);
        given(cartItemRepository.findAllByIds(any())).willReturn(Set.of(CART_ITEM));
        given(orderRepository.save(any(), any())).willReturn(1L);

        // when
        final Long orderId = orderService.order(MEMBER, orderRequest);

        // then
        assertThat(orderId).isEqualTo(1L);
    }

    @Test
    void member로_주문_목록을_조회한다() {
        // given
        final List<Order> orders = List.of(new Order(1L, List.of(), 0L, 0L, 0L));
        given(orderRepository.findByMember(MEMBER)).willReturn(orders);

        // when
        final List<OrderResponse> orderResponses = orderService.findByMember(MEMBER);

        // then
        assertThat(orderResponses).hasSize(1);
    }

    @Test
    void member와_orderId로_주문상세를_조회한다() {
        // given
        final Order order = new Order(1L, List.of(), 0L, 0L, 0L);
        given(orderRepository.findByMemberAndId(MEMBER, 1L)).willReturn(order);

        // when
        final OrderDetailResponse orderDetailResponse = orderService.findByMemberAndId(MEMBER, 1L);

        // then
        assertThat(orderDetailResponse.getOrderId()).isOne();
    }
}
