package cart.domain.order;

import cart.fixtures.OrderFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderItemsTest {


    @Test
    @DisplayName("List<OrderItem>의 가격 합을 구할 수 있다.")
    void getTotalPriceTest() {
        // given
        OrderItem orderItem1 = OrderFixtures.ORDER_ITEM1;
        OrderItem orderItem2 = OrderFixtures.ORDER_ITEM2;
        OrderItems orderItems = new OrderItems(List.of(orderItem1, orderItem2));
        int orderItem1TotalPrice = orderItem1.getTotalPrice();
        int orderItem2TotalPrice = orderItem2.getTotalPrice();
        int expectTotalPrice = orderItem1TotalPrice + orderItem2TotalPrice;

        // when
        int totalPrice = orderItems.getTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(expectTotalPrice);
    }
}