package cart.order.application;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import cart.cartitem.domain.CartItem;
import cart.cartitem.domain.CartItemRepository;
import cart.common.execption.BaseExceptionType;
import cart.coupon.domain.CouponRepository;
import cart.coupon.domain.CouponValidator;
import cart.member.domain.Member;
import cart.order.application.dto.PlaceOrderCommand;
import cart.order.domain.OrderRepository;
import cart.order.domain.OrderValidator;
import cart.order.domain.service.OrderPlaceService;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderService 은(는)")
class OrderServiceTest {

    private final OrderRepository orderRepository = mock(OrderRepository.class);
    private final CartItemRepository cartItemRepository = mock(CartItemRepository.class);
    private final CouponRepository couponRepository = mock(CouponRepository.class);
    private final OrderValidator orderValidator = mock(OrderValidator.class);

    private final CouponValidator couponValidator = mock(CouponValidator.class);

    private final OrderPlaceService orderPlaceService =
            new OrderPlaceService(orderRepository, cartItemRepository, orderValidator, couponValidator);

    private final OrderService orderService = new OrderService(orderPlaceService, cartItemRepository, couponRepository);

    @Test
    void 상품을_주문한다() {
        // given
        doNothing().when(orderValidator).validate(eq(1L), any());
        PlaceOrderCommand command = new PlaceOrderCommand(1L, List.of(
                1L, 2L
        ), Collections.emptyList());
        Product product1 = new Product("말랑", 1000, "image");
        Product product2 = new Product("코코닥", 2000, "image2");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem1 = new CartItem(product1, member);
        CartItem cartItem2 = new CartItem(product2, member);
        given(cartItemRepository.findById(1L)).willReturn(cartItem1);
        given(cartItemRepository.findById(2L)).willReturn(cartItem2);
        given(orderRepository.save(any())).willReturn(1L);

        // when
        Long place = orderService.place(command);

        // then
        assertThat(place).isEqualTo(1L);
    }

    @Test
    void 상품_정보가_변경되었으면_주문할_수_없다() {
        // given
        willThrow(new OrderException(MISMATCH_PRODUCT))
                .given(orderValidator).validate(eq(1L), any());
        PlaceOrderCommand command = new PlaceOrderCommand(1L, List.of(
                1L, 2L
        ), Collections.emptyList());

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderService.place(command)
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }

    @Test
    void 주문_이후_장바구니를_비운다() {
        // given
        doNothing().when(orderValidator).validate(eq(1L), any());
        PlaceOrderCommand command = new PlaceOrderCommand(1L, List.of(
                1L, 2L
        ), Collections.emptyList());
        Product product1 = new Product("말랑", 1000, "image");
        Product product2 = new Product("코코닥", 2000, "image2");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem1 = new CartItem(product1, member);
        CartItem cartItem2 = new CartItem(product2, member);
        given(cartItemRepository.findById(1L)).willReturn(cartItem1);
        given(cartItemRepository.findById(2L)).willReturn(cartItem2);
        given(orderRepository.save(any())).willReturn(1L);

        // when
        Long place = orderService.place(command);

        // then
        assertThat(place).isEqualTo(1L);
        then(cartItemRepository)
                .should(times(2))
                .deleteById(any());
    }
}
