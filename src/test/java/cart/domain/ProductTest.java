package cart.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ProductTest {

    @DisplayName("할인율에 따른 할인 금액을 계산한다.")
    @Test
    void calculateDiscountedPrice() {
        // given
        Product product = new Product("치킨", 10_000, "image.url", true, 20);

        // when
        int discountedPrice = product.calculateDiscountedPrice();

        // then
        assertThat(discountedPrice).isEqualTo(8_000);
    }
}
