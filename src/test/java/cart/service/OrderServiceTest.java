package cart.service;

import static fixture.CartItemFixture.CART_ITEM_1;
import static fixture.CartItemFixture.CART_ITEM_2;
import static fixture.MemberCouponFixture.MEMBER_COUPON_1;
import static fixture.MemberFixture.MEMBER_1;
import static fixture.OrderFixture.ORDER_1;
import static fixture.OrderFixture.ORDER_2;
import static fixture.OrderFixture.TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.InstanceOfAssertFactories.map;

import anotation.ServiceTest;
import cart.controller.request.OrderRequestDto;
import cart.controller.response.OrderProductResponseDto;
import cart.controller.response.OrderResponseDto;
import cart.domain.Coupon;
import cart.dto.ProductResponseDto;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

@ServiceTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @ParameterizedTest(name = "{0}")
    @MethodSource("validateOrder")
    @DisplayName("주문을 진행한다.")
    void orderCartItemsNoCoupon(String testName, Long couponId, Integer discountPrice, Integer totalPrice) {
        OrderRequestDto orderRequestDto = new OrderRequestDto(
                List.of(CART_ITEM_1.getId(), CART_ITEM_2.getId()), couponId
        );

        OrderResponseDto orderResponseDto = orderService.orderCartItems(MEMBER_1, orderRequestDto);

        assertThat(orderResponseDto)
                .extracting(OrderResponseDto::getOriginPrice, OrderResponseDto::getDiscountPrice, OrderResponseDto::getTotalPrice)
                .containsExactly(100_000, discountPrice, totalPrice);
        assertThat(orderResponseDto.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponseDto)
                .extracting(ProductResponseDto::getName, ProductResponseDto::getPrice, ProductResponseDto::getImageUrl)
                .containsExactly(
                        tuple("치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                        tuple("샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80")
                );
        assertThat(orderResponseDto.getOrderProducts())
                .extracting(OrderProductResponseDto::getQuantity)
                .containsExactly(2, 4);
    }


    private static Stream<Arguments> validateOrder() {
        return Stream.of(
                Arguments.of("쿠폰 적용 x", null, 0, 100_000),
                Arguments.of("정액 할인 쿠폰 적용", MEMBER_COUPON_1.getId(), 5000, 95_000)
        );
    }

    @Test
    @DisplayName("특정 Member 와 Order id 를 가지고 조회를 진행한다.")
    void findOrderById() {
        OrderResponseDto orderResponseDto = orderService.findOrderById(MEMBER_1, 1L);

        assertThat(orderResponseDto)
                .usingRecursiveComparison()
                .isEqualTo(OrderResponseDto.from(ORDER_1));
    }

    @Test
    @DisplayName("특정 Member Order 전부를 조회한다.")
    void findAllOrder() {
        List<OrderResponseDto> orderResponseDtos = orderService.findAllOrder(MEMBER_1);

        assertThat(orderResponseDtos)
                .usingRecursiveComparison()
                .isEqualTo(
                        List.of(OrderResponseDto.from(ORDER_1), OrderResponseDto.from(ORDER_2))
                );
    }

}