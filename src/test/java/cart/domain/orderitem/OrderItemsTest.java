package cart.domain.orderitem;

import static cart.fixtures.OrderFixtures.Dooly_Order1;
import static cart.fixtures.OrderFixtures.Dooly_Order2;
import static cart.fixtures.OrderItemFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import cart.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemsTest {

    @Test
    @DisplayName("OrderItems를 List<OrderResponse>로 변환하여 반환한다.")
    void toOrderResponse() {
        // given
        OrderItems orderItems = new OrderItems(List.of(
                Dooly_Order_Item1.ENTITY(),
                Dooly_Order_Item2.ENTITY(),
                Dooly_Order_Item3.ENTITY()
        ));

        // when
        List<OrderResponse> orderResponses = orderItems.toOrderResponses();

        // then
        assertThat(orderResponses)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("orderedDateTime")
                .containsAnyOf(Dooly_Order1.RESPONSE(), Dooly_Order2.RESPONSE());
    }

    @Test
    @DisplayName("orderId를 받아서 OrderResponse로 변환하여 반환한다.")
    void getOrderResponse() {
        // given
        Long orderId = Dooly_Order1.ID;
        OrderItems orderItems = new OrderItems(List.of(
                Dooly_Order_Item2.ENTITY(),
                Dooly_Order_Item1.ENTITY()
        ));

        // when
        OrderResponse orderResponse = orderItems.getOrderResponse(orderId);

        // then
        assertThat(orderResponse).usingRecursiveComparison()
                .ignoringFields("orderedDateTime")
                .isEqualTo(Dooly_Order1.RESPONSE());
    }
}

