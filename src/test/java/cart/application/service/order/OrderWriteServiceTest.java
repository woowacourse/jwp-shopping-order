package cart.application.service.order;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cart.application.repository.CartItemRepository;
import cart.application.repository.CouponRepository;
import cart.application.repository.PointRepository;
import cart.application.repository.order.OrderRepository;
import cart.application.repository.order.OrderedItemRepository;
import cart.domain.Member;
import cart.domain.PointHistory;
import cart.domain.Product;
import cart.domain.cartitem.CartItem;
import cart.domain.cartitem.CartItems;
import cart.domain.coupon.Coupon;
import cart.domain.discountpolicy.PointPolicy;
import cart.domain.order.Order;
import cart.ui.MemberAuth;
import cart.ui.order.dto.CreateOrderDiscountDto;
import cart.ui.order.dto.CreateOrderDto;
import cart.ui.order.dto.CreateOrderItemDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
class OrderWriteServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderedItemRepository orderedItemRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private PointRepository pointRepository;
    @Mock
    PointPolicy pointPolicy;

    @InjectMocks
    OrderWriteService orderWriteService;

    @Test
    @DisplayName("주문을 올바르게 생성할 수 있다.")
    void createOrder() {
        // given
        MemberAuth memberAuth = new MemberAuth(5L, "레오", "leo@gmail.com", "leo123");
        List<CreateOrderItemDto> createOrderItemDtos = List.of(
                new CreateOrderItemDto(10L, 10L, 2),
                new CreateOrderItemDto(11L, 11L, 3)
        );
        CreateOrderDiscountDto createOrderDiscountDto = new CreateOrderDiscountDto(List.of(1L), 1000);
        CreateOrderDto createOrderDto = new CreateOrderDto(
                createOrderItemDtos,
                createOrderDiscountDto
        );

        Member member = new Member(memberAuth.getId(),
                memberAuth.getName(),
                memberAuth.getEmail(),
                memberAuth.getPassword()
        );
        CartItem cartItem1 = new CartItem(10L, 2, new Product("testProduct", 10000, "testUrl"), member);
        CartItem cartItem2 = new CartItem(11L, 3, new Product("testProduct2", 20000, "testUrl2"), member);

        when(cartItemRepository.findById(10L)).thenReturn(java.util.Optional.of(cartItem1));
        when(cartItemRepository.findById(11L)).thenReturn(java.util.Optional.of(cartItem2));
        when(couponRepository.findById(anyLong())).thenReturn(new Coupon(1L, "testCoupon", 1000, 10, 0));
        when(pointPolicy.calculateEarnedPoint(anyInt())).thenReturn(0);
        when(orderRepository.createOrder(any())).thenReturn(1L);


        // when
        Long orderId = orderWriteService.createOrder(memberAuth, createOrderDto);

        // then
        verify(orderRepository).createOrder(any(Order.class));
        verify(orderedItemRepository, only()).createOrderItems(eq(1L), anyList());
        verify(cartItemRepository, times(2)).deleteById(anyLong());
    }

}
