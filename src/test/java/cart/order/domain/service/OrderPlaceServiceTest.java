package cart.order.domain.service;

import static cart.order.exception.OrderExceptionType.MISMATCH_PRODUCT;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.willThrow;

import cart.cartitem.domain.CartItem;
import cart.common.execption.BaseExceptionType;
import cart.member.domain.Member;
import cart.order.domain.Order;
import cart.order.domain.OrderItem;
import cart.order.domain.OrderValidator;
import cart.order.exception.OrderException;
import cart.product.domain.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("OrderPlaceService 은(는)")
class OrderPlaceServiceTest {

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderPlaceService orderPlaceService;

    @Test
    void 장바구니_상품을_주문한다() {
        // given
        doNothing().when(orderValidator).validate(eq(1L), any());
        Product product = new Product(1L, "말랑", 1000, "image");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem = new CartItem(2L, 10, product, member);

        // when
        Order order = orderPlaceService.placeOrder(member.getId(), of(cartItem));

        // then
        List<OrderItem> orderItems = of(new OrderItem(10, 1L, "말랑", 1000, "image"));
        assertThat(order.getOrderItems()).usingRecursiveComparison()
                .ignoringExpectedNullFields()
                .isEqualTo(orderItems);
    }

    @Test
    void 상품이_변경되었으면_주문할_수_없다() {
        // given
        willThrow(new OrderException(MISMATCH_PRODUCT))
                .given(orderValidator).validate(eq(1L), any());
        Product product = new Product(1L, "말랑", 1000, "image");
        Member member = new Member(1L, "email", "1234");
        CartItem cartItem = new CartItem(2L, 10, product, member);

        // when
        BaseExceptionType baseExceptionType = assertThrows(OrderException.class, () ->
                orderPlaceService.placeOrder(member.getId(), of(cartItem))
        ).exceptionType();

        // then
        assertThat(baseExceptionType).isEqualTo(MISMATCH_PRODUCT);
    }
}
