package cart.domain.order;

import static cart.TestDataFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Price;
import cart.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderProductTest {

    @DisplayName("orderProduct의 가격을 계산하는 기능 테스트")
    @Test
    void calculateTotalPrice() {
        final OrderProduct orderProduct = new OrderProduct(PRODUCT_1, new Quantity(2));
        final Price price = orderProduct.calculateTotalPrice();

        assertThat(price)
                .isEqualTo(orderProduct.getProduct().calculatePrice(orderProduct.getQuantity()));
    }
}
