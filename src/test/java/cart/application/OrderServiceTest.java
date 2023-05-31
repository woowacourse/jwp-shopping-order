package cart.application;

import cart.domain.*;
import cart.dto.OrderRequest;
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
        final Product desert = new Product(1L, "desert", 5000, "imgUrl");
        final CartItems cartItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member), new CartItem(2L, 1, desert, member)));

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
        final Product desert = new Product(1L, "desert", 5000, "imgUrl");
        final CartItems cartItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member)));

        given(cartItemRepository.findAllByCartItemIds(request.getId())).willReturn(cartItems);

        // when, then
        assertThatThrownBy(() -> orderService.order(request))
                .isInstanceOf(NoSuchElementException.class);
    }
}
