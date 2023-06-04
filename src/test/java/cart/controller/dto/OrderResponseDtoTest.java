package cart.controller.dto;

import static fixture.OrderFixture.ORDER_1;
import static fixture.OrderFixture.TIME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import cart.controller.response.OrderProductResponseDto;
import cart.controller.response.OrderResponseDto;
import cart.dto.ProductResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderResponseDtoTest {

    @Test
    @DisplayName("Order 를 이용해 OrderResponseDto 를 생성한다.")
    void from() {
        OrderResponseDto orderResponseDto = OrderResponseDto.from(ORDER_1);

        assertThat(orderResponseDto)
                .extracting(OrderResponseDto::getTimestamp, OrderResponseDto::getOriginPrice, OrderResponseDto::getDiscountPrice, OrderResponseDto::getTotalPrice)
                .containsExactly(TIME.toString(), 86_000, 5000, 81_000);
        assertThat(orderResponseDto.getOrderProducts())
                .extracting(OrderProductResponseDto::getProductResponseDto)
                .extracting(ProductResponseDto::getName, ProductResponseDto::getPrice, ProductResponseDto::getImageUrl)
                .containsExactly(
                        tuple("치킨", 10000, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                        tuple("샐러드", 20000, "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2370&q=80"),
                        tuple("피자", 13000, "https://images.unsplash.com/photo-1595854341625-f33ee10dbf94?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=1740&q=80")
                );
        assertThat(orderResponseDto.getOrderProducts())
                .extracting(OrderProductResponseDto::getQuantity)
                .containsExactly(2, 2, 2);
    }

}
