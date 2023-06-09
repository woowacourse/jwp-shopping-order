package cart.application;

import cart.domain.coupon.MemberCoupon;
import cart.domain.order.Order;
import cart.dto.request.OrderCartItemRequest;
import cart.dto.request.OrderRequest;
import cart.dto.response.AllOrderResponse;
import cart.dto.response.OrderDetailResponse;
import cart.fixture.OrderFixture;
import cart.repository.CartItemRepository;
import cart.repository.MemberCouponRepository;
import cart.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static cart.fixture.CartItemFixture.장바구니1;
import static cart.fixture.CartItemFixture.장바구니2;
import static cart.fixture.CouponFixture.회원쿠폰1;
import static cart.fixture.MemberFixture.라잇;
import static cart.fixture.OrderFixture.주문1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MemberCouponRepository memberCouponRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Test
    public void 주문한다() {
        OrderRequest request = new OrderRequest(
                List.of(
                        new OrderCartItemRequest(1L, 2, "지구", 1000, "주소"),
                        new OrderCartItemRequest(2L, 4, "화성", 200000, "주소")
                ),
                1L
        );
        when(memberCouponRepository.findById(1L)).thenReturn(회원쿠폰1);
        when(cartItemRepository.findByIds(anyList())).thenReturn(List.of(장바구니1, 장바구니2));

        Long orderId = orderService.createOrder(라잇, request);

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 회원으로_조회한다() {
        when(orderRepository.findAllByMember(라잇)).thenReturn(List.of(주문1));

        AllOrderResponse allOrderByMember = orderService.findAllOrderByMember(라잇);

        assertThat(allOrderByMember.getOrders()).hasSize(1);
    }

    @Test
    void 회원과_아이디로_조회한다() {
        when(orderRepository.findById(1L)).thenReturn(주문1);

        OrderDetailResponse orderByIdAndMember = orderService.findOrderByIdAndMember(1L, 라잇);

        assertThat(orderByIdAndMember.getOrderId()).isEqualTo(1L);
    }

    @Test
    void 취소한다() {
        when(orderRepository.findById(1L)).thenReturn(주문1);

        orderService.cancelOrder(1L, 라잇);

        verify(memberCouponRepository).update(any(MemberCoupon.class));
        verify(orderRepository).delete(any(Order.class));
    }
}

