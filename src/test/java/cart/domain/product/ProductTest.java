package cart.domain.product;

import static cart.TestDataFixture.PRODUCT_1;
import static org.assertj.core.api.Assertions.assertThat;

import cart.domain.vo.Price;
import cart.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("Quantity를 주면 총 가격을 계산하는 기능 테스트")
    @Test
    void calculateTest() {
        final Quantity quantity = new Quantity(5);

        final Price price = PRODUCT_1.calculatePrice(quantity);

        assertThat(price.getValue())
                .isEqualTo(5 * PRODUCT_1.getPrice().getValue());
    }
}
