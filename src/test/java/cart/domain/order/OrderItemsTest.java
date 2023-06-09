package cart.domain.order;

import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.Product;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderItemsTest {

    @DisplayName("상품들의 총 가격을 반환한다.")
    @Test
    void getTotalPrice() {
        //given
        final OrderItem orderItem = OrderItem.notPersisted(new Product("product", 1000, "imageUrl"), 10);
        final OrderItems orderItems = new OrderItems(List.of(orderItem));

        //when
        final Long totalPrice = orderItems.getTotalPrice();

        //then
        assertThat(totalPrice).isEqualTo(10000L);
    }
}
