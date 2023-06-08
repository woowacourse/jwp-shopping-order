package cart.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static cart.fixtures.OrderFixtures.ORDER_ITEM1;
import static org.assertj.core.api.Assertions.assertThat;

class OrderItemTest {

    @Test
    @DisplayName("상품 가격과 개수를 곱해서 해당 주문 아이템에 대한 최종 가격을 구할 수 있다.")
    void getTotalPriceTest() {
        // given
        OrderItem orderItem = ORDER_ITEM1;
        int orderItemPrice = orderItem.getProduct().getPrice();
        int orderItemQuantity = orderItem.getQuantity();
        int expectTotalPrice = orderItemPrice * orderItemQuantity;

        // when
        int totalPrice = orderItem.getTotalPrice();

        // then
        assertThat(totalPrice).isEqualTo(expectTotalPrice);
    }

}