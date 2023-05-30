package cart.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    
    @Test
    @DisplayName("상품의 할인된 가격을 구한다.")
    void calculate_discount_price() {
        // given
        OrderItem orderItem = new OrderItem(1L, "포카칩", 1000, "이미지", 5, 10);
        int expect =(int) (orderItem.getPrice() * (1 - (double) orderItem.getDiscountRate() / 100) * orderItem.getQuantity());

        // when
        int result = orderItem.getPurchasedItemsPrice();

        // then
        assertThat(result).isEqualTo(expect);
    }
}
