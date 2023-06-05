package cart.application.service;

import cart.application.domain.CartItem;
import cart.application.domain.Member;
import cart.application.domain.Order;
import cart.application.domain.OrderInfo;
import cart.application.domain.OrderInfos;
import cart.application.domain.Product;
import cart.application.exception.CartItemNotFoundException;
import cart.application.exception.IllegalMemberException;
import cart.application.exception.MemberNotFoundException;
import cart.application.repository.CartItemRepository;
import cart.application.repository.MemberRepository;
import cart.application.repository.OrderRepository;
import cart.presentation.dto.request.AuthInfo;
import cart.presentation.dto.request.OrderRequest;
import cart.presentation.dto.response.OrderResponse;
import cart.presentation.dto.response.SpecificOrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private OrderRepository orderRepository;

    private OrderRequest orderRequest;
    private AuthInfo authInfo;
    private Member member;
    private Product pizza;

    @BeforeEach
    void setup() {
        orderRequest = new OrderRequest(List.of(1L), 20000L, 5000L, 0L);
        authInfo = new AuthInfo("teo", "1234");
        member = new Member(1L, "teo", "1234", 10000);
        pizza = new Product(1L, "피자", 20000, "https://a.com", 0, true);

    }

    @Test
    @DisplayName("해당 정보의 멤버가 없다면 예외를 던진다")
    void issue_exception() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.empty());
        // when, then
        assertThatThrownBy(() -> orderService.issue(authInfo, orderRequest))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("정상적으로 주문하는 경우, 주문된 정보의 id를 반환한다")
    void issue() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(orderRepository.insert(any())).thenReturn(new Order(1L, member, null, 0L, 0L, 0L));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, member)));
        doNothing().when(memberRepository).update(any());
        doNothing().when(cartItemRepository).deleteByMemberId(any());
        // when
        long id = orderService.issue(authInfo, orderRequest);
        // then
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("주문 시 유효한 장바구니 물품 ID가 입력되지 않았다면 예외를 던진다")
    void issue_cartItemCheck() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(cartItemRepository.findById(any())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> orderService.issue(authInfo, orderRequest))
                .isInstanceOf(CartItemNotFoundException.class);
    }

    @Test
    @DisplayName("정상적으로 주문하는 경우, 멤버의 포인트가 조정된다")
    void issue_pointCheck() {
        // given
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(orderRepository.insert(any())).thenReturn(
                new Order(1L, member, null, 20000L, 5000L, 0L));
        when(cartItemRepository.findById(any())).thenReturn(Optional.of(
                new CartItem(1L, 1, pizza, member)));
        doNothing().when(memberRepository).update(any());
        doNothing().when(cartItemRepository).deleteByMemberId(any());

        // when
        orderService.issue(authInfo, orderRequest);
        // then
        assertThat(member.getPoint()).isEqualTo(5000L);
    }

    @Test
    @DisplayName("모든 주문을 조회할 수 있다")
    void getAllOrders() {
        // given
        OrderInfos orderInfos = new OrderInfos(List.of(
                new OrderInfo(1L, pizza, "피자", 20000L, "https://a.com", 1)));
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(orderRepository.findByMemberId(any())).thenReturn((List.of(
                new Order(1L, member, orderInfos, 20000L, 5000L, 0L))));
        // when
        List<OrderResponse> responses = orderService.getAllOrders(authInfo);
        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getOrderInfos().get(0).getName()).isEqualTo("피자");
    }

    @Test
    @DisplayName("특정 주문을 조회할 수 있다")
    void getSpecificOrder() {
        // given
        OrderInfos orderInfos = new OrderInfos(List.of(
                new OrderInfo(1L, pizza, "피자", 20000L, "https://a.com", 1)));
        when(memberRepository.findByEmail(any())).thenReturn(Optional.of(member));
        when(orderRepository.findById(any())).thenReturn(Optional.of(
                new Order(1L, member, orderInfos, 20000L, 5000L, 0L)));
        // when
        SpecificOrderResponse response = orderService.getSpecificOrder(authInfo, 1L);
        // then
        assertThat(response.getOrderInfos().get(0).getName()).isEqualTo("피자");
    }
}
