package cart.application;

import cart.domain.*;
import cart.domain.coupon.Coupon;
import cart.dto.OrderRequest;
import cart.dto.OrderResponse;
import cart.repository.CartItemRepository;
import cart.repository.OrderRepository;
import cart.repository.coupon.CouponRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @Test
    void 주문_한다() {
        // given
        final OrderRequest request = new OrderRequest(List.of(1L, 2L), 15000, 1L);
        final Member member = new Member(1L, "a@a.com", "1234");
        final Product chicken = new Product(1L, "치킨", 10000, "imgUrl");
        final Product dessert = new Product(1L, "디저트", 5000, "imgUrl");
        final CartItems cartItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member), new CartItem(2L, 1, dessert, member)));

        given(cartItemRepository.findAllByCartItemIds(request.getId())).willReturn(cartItems);
        willDoNothing().given(cartItemRepository).deleteAllByIds(request.getId());
        given(orderRepository.save(any(Order.class))).willReturn(1L);

        // when
        orderService.order(request);

        // then
        assertAll(
                () -> then(cartItemRepository).should(times(1)).findAllByCartItemIds(anyList()),
                () -> then(cartItemRepository).should(times(1)).deleteAllByIds(anyList()),
                () -> then(orderRepository).should(times(1)).save(any(Order.class))
        );
    }

    @Test
    void 상품_아이디가_없으면_실패한다() {
        // given
        final OrderRequest request = new OrderRequest(List.of(1L, 2L), 15000, 1L);
        final Member member = new Member(1L, "a@a.com", "1234");
        final Product chicken = new Product(1L, "치킨", 10000, "imgUrl");
        final Product dessert = new Product(1L, "디저트", 5000, "imgUrl");
        final CartItems cartItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member)));

        given(cartItemRepository.findAllByCartItemIds(request.getId())).willReturn(cartItems);

        // when, then
        assertThatThrownBy(() -> orderService.order(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 상품을_조회한다() {
        // given
        final Member member = new Member(1L, "a@a.com", "1234");
        final Product chicken = new Product(1L, "치킨", 10000, "imgUrl");
        final Product dessert = new Product(1L, "desert", 5000, "imgUrl");
        final OrderItem chickenOrderItem = new OrderItem(chicken, 1);
        final OrderItem desertOrderItem = new OrderItem(dessert, 1);
        final Coupon coupon = new Coupon(1L, "1000원 할인 쿠폰", "1000원이 할인 됩니다.", 1000, false);

        final Order order = new Order(List.of(chickenOrderItem, desertOrderItem), member, coupon, chicken.getPrice() + dessert.getPrice());
        given(orderRepository.findOrderByMemberId(anyLong())).willReturn(List.of(order));

        // when
        final List<OrderResponse> orderResponses = orderService.findOrderByMember(member);

        // then
        final OrderResponse result = orderResponses.get(0);
        assertAll(
                () -> assertThat(result.getCartItems()).hasSize(2),
                () -> assertThat(result.getDate()).isNotNull(),
                () -> assertThat(result.getPrice()).isEqualTo(15000)
        );
    }
}
