package cart.service;

import cart.controller.dto.OrderRequest;
import cart.domain.Product;
import cart.domain.cart.CartItem;
import cart.domain.cart.CartItemRepository;
import cart.domain.cart.CartItems;
import cart.domain.coupon.CouponRepository;
import cart.domain.member.Member;
import cart.domain.order.Order;
import cart.domain.order.OrderRepository;
import cart.exception.network.AlreadyUsedCouponException;
import cart.exception.network.DifferentCartItemSizeException;
import cart.service.dto.OrderSaveDto;
import cart.service.order.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest extends CouponFixture {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private CouponRepository couponRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    private Member member;
    private Product chicken;
    private Product dessert;

    @BeforeEach
    void init() {
        member = new Member(1L, "a@a.com", "1234");
        chicken = new Product(1L, "치킨", 10000, "imgUrl");
        dessert = new Product(2L, "디저트", 5000, "imgUrl");
    }

    @Nested
    class 주문_할_때 {

        private OrderRequest request;
        private CartItems sizeOneCarItems;

        @BeforeEach
        void init() {
            request = new OrderRequest(List.of(1L, 2L), 15000, 1L);
            sizeOneCarItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member), new CartItem(2L, 1, dessert, member)));
        }

        @Test
        void 쿠폰이_존재하면_성공_한다() {
            // given
            given(cartItemRepository.findAllByCartItemIds(request.getCartItemIds())).willReturn(sizeOneCarItems);
            willDoNothing().given(cartItemRepository).deleteAllByIds(request.getCartItemIds());
            given(orderRepository.save(any(Order.class))).willReturn(1L);
            given(couponRepository.findCouponById(anyLong())).willReturn(unUsedCoupon);

            // when
            orderService.order(OrderSaveDto.from(request));

            // then
            assertAll(
                    () -> then(cartItemRepository).should(times(1)).findAllByCartItemIds(anyList()),
                    () -> then(cartItemRepository).should(times(1)).deleteAllByIds(anyList()),
                    () -> then(orderRepository).should(times(1)).save(any(Order.class))
            );
        }

        @Test
        void 쿠폰이_없어도_성공_한다() {
            // given
            request = new OrderRequest(List.of(1L, 2L), 15000, null);
            given(cartItemRepository.findAllByCartItemIds(request.getCartItemIds())).willReturn(sizeOneCarItems);
            willDoNothing().given(cartItemRepository).deleteAllByIds(request.getCartItemIds());
            given(orderRepository.save(any(Order.class))).willReturn(1L);

            // when
            orderService.order(OrderSaveDto.from(request));

            // then
            assertAll(
                    () -> then(cartItemRepository).should(times(1)).findAllByCartItemIds(anyList()),
                    () -> then(cartItemRepository).should(times(1)).deleteAllByIds(anyList()),
                    () -> then(orderRepository).should(times(1)).save(any(Order.class))
            );
        }

        @Test
        void 사용된_쿠폰을_사용하려하면_실패_한다() {
            // given
            given(cartItemRepository.findAllByCartItemIds(request.getCartItemIds())).willReturn(sizeOneCarItems);
            given(couponRepository.findCouponById(anyLong())).willReturn(usedCoupon);

            // when, then
            assertThatThrownBy(() -> orderService.order(OrderSaveDto.from(request)))
                    .isInstanceOf(AlreadyUsedCouponException.class);
        }

        @Test
        void 상품_아이디가_없으면_실패한다() {
            // given
            sizeOneCarItems = new CartItems(List.of(new CartItem(1L, 1, chicken, member)));

            given(cartItemRepository.findAllByCartItemIds(request.getCartItemIds())).willReturn(sizeOneCarItems);

            // when, then
            assertThatThrownBy(() -> orderService.order(OrderSaveDto.from(request)))
                    .isInstanceOf(DifferentCartItemSizeException.class);
        }
    }
}
