package cart.domain.order;

import static cart.TestDataFixture.ORDER_PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Price;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderProductTest {

    @DisplayName("orderProduct의 가격을 계산하는 기능 테스트")
    @Test
    void calculateTotalPrice() {
        final Price price = ORDER_PRODUCT_1.calculateTotalPrice();

        assertThat(price)
                .isEqualTo(ORDER_PRODUCT_1.getProduct().calculatePrice(ORDER_PRODUCT_1.getQuantity()));
    }
}
