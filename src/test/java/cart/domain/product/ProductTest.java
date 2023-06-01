package cart.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProductTest {

    @Test
    @DisplayName("할인된 상품 가격을 구한다.")
    void calculate_discouted_price() {
        // given
        Product product = new Product("포카칩", 10_000, "이미지", true, 10);
        int expect = 9000;

        // when
        int result = product.getDiscountedPrice();

        // then
        assertThat(result).isEqualTo(expect);
    }
}
